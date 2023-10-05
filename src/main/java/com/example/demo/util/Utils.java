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
	private static ArrayList<String> broker = new ArrayList<String>();
	private static ArrayList<String> queueProcess = new ArrayList<>();

	static {
		Utils.dbVer.add("^io\\.opentelemetry\\.jdbc(.+)?$");
		Utils.dbVer.add("^io\\.opentelemetry\\.mongo(.+)?$");
		Utils.queueProcess.add("^io\\.opentelemetry\\.spring-rabbit(.+)?$");
		Utils.broker.add("^io\\.opentelemetry\\.rabbitmq(.+)?$");
	}

	public static double round(double d) {
		return new BigDecimal(d).setScale(2, RoundingMode.HALF_DOWN).doubleValue();
	}

	public static boolean checkError(Trace trace) {
		KeyValue errKey1 = trace.getTags().stream().filter(x -> x.getKey().equals("error")).findFirst().orElse(null);
		if (errKey1 != null)
			return true;
		KeyValue errKey = trace.getTags().stream().filter(x -> x.getKey().equals("http.status_code")).findFirst()
				.orElse(null);
		if (errKey == null)
			return false;
		return errKey.getValueLong() >= 400;
	}

	public static Long runTrace(long spanId, Map<Long, ArrayList<Long>> traceRef, Map<Long, Long[]> spanDurStart,
			Map<String, Long[]> distinctService, Map<Integer, HashSet<Integer>> conn, HashSet<Long> spanIsConsumer,
			Map<Long, String[]> spanSvc, int depth, int prevIdx) {
		boolean contain = spanSvc.containsKey(spanId);
		String[] arrSvc = contain ? spanSvc.get(spanId) : null;
		int lenSvc = arrSvc!=null ? arrSvc.length : 0;
		boolean isConsumer = spanIsConsumer.contains(spanId);

		int currNodeIdx = prevIdx;

		Long currDur = contain ? spanDurStart.get(spanId)[0] : 0l;
		ArrayList<Long> childSpan = traceRef.get(spanId);

		if (contain) {
			String svc0 = arrSvc[0];
			int idx1 = distinctService.get(svc0)[0].intValue();

			if (!conn.containsKey(prevIdx))
				conn.put(prevIdx, new HashSet<>());

			if (lenSvc > 1) {
				int idx2 = distinctService.get(arrSvc[1])[0].intValue();

				if (!conn.containsKey(idx1))
					conn.put(idx1, new HashSet<>());

				conn.get(idx1).add(idx2);
				currNodeIdx = idx2;
				conn.get(prevIdx).add(idx1);
			} else {
				currNodeIdx = idx1;

				if (!isConsumer)
					conn.get(prevIdx).add(idx1);
				else {
					Long[] res = distinctService.get(svc0);
					--res[1];
				}
			}

			if (depth > 0) {
				Long currDur1 = currDur;

				if (childSpan != null)
					for (Long nextSpan : childSpan)
						currDur1 -= runTrace(nextSpan, traceRef, spanDurStart, distinctService, conn, spanIsConsumer, spanSvc, 0, currNodeIdx);

				for (int i = 0; i < lenSvc; ++i) {
					Long[] res = distinctService.get(arrSvc[i]);
					if (i != 0 || !isConsumer || lenSvc != 2)
						res[3] += currDur;
				}

				return isConsumer ? 0l : currDur;
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

		if (contain) {
			for (int i = 0; i < lenSvc; ++i) {
				Long[] res = distinctService.get(arrSvc[i]);
				if (i != 0 || !isConsumer || lenSvc != 2)
					res[3] += currDur;
			}
		}

		return currDur;
	}

	public static boolean isConsumer(Trace trace) {
		KeyValue spanKind = trace.getTags().stream().filter(x -> x.getKey().equals("span.kind")).findFirst()
				.orElse(null);
		if (spanKind == null)
			return false;
		return spanKind.getValueString().equals("consumer");
	}

	public static String[] getServiceCall(Trace trace) {
		return new String[] { "Service", trace.getProcess().getServiceName() };
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

			if (Utils.queueProcess.stream().anyMatch(x -> libName.matches(x))) {
				return new String[] { "Service", trace.getProcess().getServiceName() };
			} else if (Utils.broker.stream().anyMatch(x -> libName.matches(x))) {
				KeyValue queueKey = trace.getTags().stream()
						.filter(x -> x.getKey().equals("messaging.destination.name")).findFirst().orElse(null);
				return new String[] { "Queue", queueKey.getValueString(), "Service",
						trace.getProcess().getServiceName() };
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
