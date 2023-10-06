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
		return BigDecimal.valueOf(d).setScale(2, RoundingMode.HALF_DOWN).doubleValue();
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

	public static void runTrace(long spanId, Map<Long, ArrayList<Long>> traceRef, Map<String, Long[]> distinctService,
			Map<Integer, HashSet<Integer>> conn, HashSet<Long> spanIsConsumer, Map<Long, String[]> spanSvc,
			int prevIdx) {
		int currNodeIdx = prevIdx;

		if (spanSvc.containsKey(spanId)) {
			int idx1 = distinctService.get(spanSvc.get(spanId)[0])[0].intValue();

			if (!conn.containsKey(prevIdx))
				conn.put(prevIdx, new HashSet<>());

			if (spanSvc.get(spanId).length > 1) {
				int idx2 = distinctService.get(spanSvc.get(spanId)[1])[0].intValue();
				
				if (!conn.containsKey(idx1))
					conn.put(idx1, new HashSet<>());
				conn.get(idx1).add(idx2);
				
				currNodeIdx = idx2;
				conn.get(prevIdx).add(idx1);
			} else {
				currNodeIdx = idx1;

				if (!spanIsConsumer.contains(spanId))
					conn.get(prevIdx).add(idx1);
			}
		}

		if (traceRef.get(spanId) != null)
			for (Long nextSpan : traceRef.get(spanId))
				runTrace(nextSpan, traceRef, distinctService, conn, spanIsConsumer, spanSvc, currNodeIdx);
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
