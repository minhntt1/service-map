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
			Map<String, Long[]> distinctService, Map<Integer, HashSet<Integer>> conn, Map<Long, String[]> spanSvc,
			int depth, int prevIdx) {
		boolean contain = spanSvc.containsKey(spanId);

		int currNodeIdx = prevIdx;

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
		}

		Long currDur = contain ? spanDurStart.get(spanId)[0] : 0l;
		ArrayList<Long> childSpan = traceRef.get(spanId);

		if (contain && depth > 0) {
			Long currDur1 = currDur;

			if (childSpan != null)
				for (Long nextSpan : childSpan)
					currDur1 -= runTrace(nextSpan, traceRef, spanDurStart, distinctService, conn, spanSvc, 0,
							currNodeIdx);

			for (String svc : spanSvc.get(spanId)) {
				Long[] res = distinctService.get(svc);
				res[3] += currDur1;
			}

			return spanSvc.get(spanId)[0].contains("Service (subscriber)") ? 0l : currDur;
		}

		if (childSpan != null)
			for (Long nextSpan : childSpan)
				if (contain)
					currDur -= runTrace(nextSpan, traceRef, spanDurStart, distinctService, conn, spanSvc, depth + 1,
							currNodeIdx);
				else
					currDur += runTrace(nextSpan, traceRef, spanDurStart, distinctService, conn, spanSvc, depth + 1,
							currNodeIdx);

		if (contain)
			for (String svc : spanSvc.get(spanId)) {
				Long[] res = distinctService.get(svc);
				res[3] += currDur;
			}

		return currDur;
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
				return new String[] { "Service (subscriber)", trace.getProcess().getServiceName() };
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

				return new String[] { "Database",
						new StringBuilder().append(dbSystem).append(' ').append(dbName).toString() };
			}

			return null;
		}

		return null;
	}

	// get kind: chi tra ve neu la server, con dau thi la null het
	public static String[] getKind(Trace trace) {
		KeyValue spanKind = trace.getTags().stream().filter(x -> x.getKey().equals("span.kind")).findFirst()
				.orElse(null);

		if (trace.getProcess() == null || spanKind == null)
			return null;

		/*
		 * vs cac request http co path thi xac dinh theo server (thang nhan) database
		 * thi xac dinh theo client (thang gui di) producer la thang goi thi chi tra ve
		 * ten consumer la thang nhan tra ve rabbit mq + ten consumer
		 */

		/*
		 * String libName = trace.getTags().stream().filter(x ->
		 * x.getKey().equals("otel.library.name")).findFirst().orElse(null).
		 * getValueString();
		 * 
		 * if(NodeInfo.queue.stream().anyMatch(x->libName.matches(x))) {
		 * 
		 * } else if(NodeInfo.dbVer.stream().anyMatch(x->libName.matches(x))) {
		 * 
		 * } else {
		 * 
		 * }
		 */

		if (spanKind.getValueString().equals("client") || spanKind.getValueString().equals("producer")) {
			// server thi chi tra ve ten

			String libName = trace.getTags().stream().filter(x -> x.getKey().equals("otel.library.name")).findFirst()
					.orElse(null).getValueString();

			if (Utils.dbVer.stream().anyMatch(x -> libName.matches(x))) {
				String dbSystem = trace.getTags().stream().filter(x -> x.getKey().equals("db.system")).findFirst()
						.orElse(null).getValueString();
				String dbName = trace.getTags().stream().filter(x -> x.getKey().equals("db.name")).findFirst()
						.orElse(null).getValueString();

				return new String[] { "caller", "Service", trace.getProcess().getServiceName(), "called", "Database",
						new StringBuilder().append(dbSystem).append(" ").append(dbName).toString() };
			}

			return new String[] { "caller" };
		} else if (spanKind.getValueString().equals("server") || spanKind.getValueString().equals("consumer")) {
			// client thi tra ve path bi goi va ten bi goi
			// server la cai bi goi
			// client la cai goi no
			// cai bi goi thi tra ve ten va path goi no
			KeyValue route = trace.getTags().stream().filter(x -> x.getKey().equals("http.route")).findFirst()
					.orElse(null);

			if (route != null) {
				return new String[] { "caller", "API", route.getValueString(), "called", "Service",
						trace.getProcess().getServiceName() };
			}

			return new String[] { "called" };
		}

		return null;
//		if (spanKind != null && spanKind.getValueString().equals("client")) {
//			KeyValue lib = trace.getTags().stream().filter(x -> x.getKey().equals("otel.library.name")).findFirst()
//					.orElse(null);
//
//			if (lib != null && NodeInfo.dbVer.stream().anyMatch(x -> lib.getValueString().matches(x))) {
//				String dbSystem = trace.getTags().stream().filter(x -> x.getKey().equals("db.system")).findFirst()
//						.orElse(null).getValueString();
//				String dbName = trace.getTags().stream().filter(x -> x.getKey().equals("db.name")).findFirst()
//						.orElse(null).getValueString();
//
//				return new String[] { "Database", new StringBuilder().append(dbSystem).append(" ").append(dbName).toString() };
//			} else
//				return new String[] { "Service", trace.getProcess().getServiceName() };
//		} else {
//			KeyValue path = trace.getTags().stream().filter(x -> x.getKey().equals("http.route")).findFirst()
//					.orElse(null);
//			if (spanKind != null && spanKind.getValueString().equals("server") && path != null)
//				return new String[] { "Service", trace.getProcess().getServiceName(), "API", path.getValueString() };
//
//			return new String[] { "Service", trace.getProcess().getServiceName() };
//		}
	}
}
