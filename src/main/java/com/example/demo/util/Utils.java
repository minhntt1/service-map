package com.example.demo.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;

import com.example.demo.model.KeyValue;
import com.example.demo.model.Trace;

public class Utils {
	private static ArrayList<String> dbVer = new ArrayList<>();
	private static ArrayList<String> queue = new ArrayList<>();

	static {
		Utils.dbVer.add("^io\\.opentelemetry\\.jdbc(.+)?$");
		Utils.dbVer.add("^io\\.opentelemetry\\.mongo(.+)?$");
	}

	static {
		Utils.queue.add("^io\\.opentelemetry\\.spring-rabbit(.+)?$");
	}

	public static double round(double d) {
		return new BigDecimal(d).setScale(2, RoundingMode.HALF_DOWN).doubleValue();
	}

	public static boolean checkError(Trace trace) {
		KeyValue errKey1 = trace.getTags().stream().filter(x -> x.getKey().equals("error")).findFirst().orElse(null);
		if (errKey1 != null)
			return true;
		KeyValue errKey = trace.getTags().stream().filter(x -> x.getKey().equals("http.status_code")).findFirst().orElse(null);
		if (errKey == null)
			return false;
		return errKey.getValueLong() >= 400;
	}

	public static Long runTrace(Long spanId, Map<Long, ArrayList<Long>> traceRef, Map<Long, Long[]> spanDurStart,
			Map<String, Long[]> distinctService, Map<Integer, HashSet<Integer>> conn, HashSet<Long> spanIsConsumer,
			Map<Long, String[]> spanSvc, int depth, int prevIdx) {
		boolean contain = spanSvc.containsKey(spanId);

		int currNodeIdx = prevIdx;

		Long currDur = contain ? spanDurStart.get(spanId)[0] : 0l;
		ArrayList<Long> childSpan = traceRef.get(spanId);

		if (contain) {
			int idx1 = distinctService.get(spanSvc.get(spanId)[0])[0].intValue();
			
			if (spanSvc.get(spanId).length > 1) {
				int idx2 = distinctService.get(spanSvc.get(spanId)[1])[0].intValue();
				if (!conn.containsKey(idx1))
					conn.put(idx1, new HashSet<>());
				conn.get(idx1).add(idx2);
				currNodeIdx = idx2;
			} else {
				currNodeIdx = idx1;
			}
			
			if (!conn.containsKey(prevIdx))
				conn.put(prevIdx, new HashSet<>());
			conn.get(prevIdx).add(idx1);

			if (depth > 0) {
				Long currDur1 = currDur;

				if (childSpan != null)
					for (Long nextSpan : childSpan)
						currDur1 -= runTrace(nextSpan, traceRef, spanDurStart, distinctService, conn, spanIsConsumer,
								spanSvc, 0, currNodeIdx);

				for (String svc : spanSvc.get(spanId)) {
					Long[] res = distinctService.get(svc);
					res[3] += currDur1;
				}

				return spanIsConsumer.contains(spanId) ? 0l : currDur;
			}
		}

		if (childSpan != null)
			for (Long nextSpan : childSpan)
				if (contain)
					currDur -= runTrace(nextSpan, traceRef, spanDurStart, distinctService, conn, spanIsConsumer,
							spanSvc, depth + 1, currNodeIdx);
				else
					currDur += runTrace(nextSpan, traceRef, spanDurStart, distinctService, conn, spanIsConsumer,
							spanSvc, depth + 1, currNodeIdx);

		if (contain)
			for (String svc : spanSvc.get(spanId)) {
				Long[] res = distinctService.get(svc);
				res[3] += currDur;
			}

		return currDur;
	}

	public static boolean isConsumer(Trace trace) {
		KeyValue spanKind = trace.getTags().stream().filter(x -> x.getKey().equals("span.kind")).findFirst().orElse(null);
		if (spanKind == null)
			return false;
		return spanKind.getValueString().equals("consumer");
	}
	
	public static String[] getServiceCall(Trace trace) {
		return new String[] {"Service", trace.getProcess().getServiceName()};
	}

	public static String[] getFirstCall(Trace trace) {
		KeyValue spanKind = trace.getTags().stream().filter(x -> x.getKey().equals("span.kind")).findFirst()
				.orElse(null);

		if (spanKind == null)
			return null;

		if (spanKind.getValueString().equals("server")) {
			String route = trace.getTags().stream().filter(x -> x.getKey().equals("http.target")).findFirst()
					.orElse(null).getValueString();
			return new String[] { "API", route, "Service", trace.getProcess().getServiceName() };
		} else if (spanKind.getValueString().equals("consumer")) {
			String libName = trace.getTags().stream().filter(x -> x.getKey().equals("otel.library.name")).findFirst()
					.orElse(null).getValueString();

			if (Utils.queue.stream().anyMatch(x -> libName.matches(x))) {
				return new String[] { "Service", trace.getProcess().getServiceName() };
			}

			return null;
		} else if (spanKind.getValueString().equals("client")) {
			String libName = trace.getTags().stream().filter(x -> x.getKey().equals("otel.library.name")).findFirst()
					.orElse(null).getValueString();

			if (Utils.dbVer.stream().anyMatch(x -> libName.matches(x))) {
				String dbSystem = trace.getTags().stream().filter(x -> x.getKey().equals("db.system")).findFirst()
						.orElse(null).getValueString();
				String dbName = trace.getTags().stream().filter(x -> x.getKey().equals("db.name")).findFirst()
						.orElse(null).getValueString();

				return new String[] { "DB",
						new StringBuilder().append(dbSystem).append(' ').append(dbName).toString() };
			}

			return null;
		}

		return null;
	}
}
