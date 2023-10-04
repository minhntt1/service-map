package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GraphApiApplication {
	public static void main(String[] args) {
		SpringApplication.run(GraphApiApplication.class, args);
	}
//	@Bean
//	public CommandLineRunner commandLineRunner(ServiceNameIndexRepo serviceNameIndexRepo, TraceRepo traceRepo) {
//		return runner -> {
//			Map<String, Long[]> distinctService = new HashMap<String, Long[]>();
//			Map<Integer, HashSet<Integer>> conn = new HashMap<Integer, HashSet<Integer>>();
//			
//			ByteBuffer buffer = ByteUtils.fromHexString("0xa722c060fe8a1acda5736b4c759b7bfd");
//			Map<Long, ArrayList<Long>> traceRef = new HashMap<Long, ArrayList<Long>>();
//			Map<Long, Long[]> spanDurStart = new HashMap<Long, Long[]>();
//			Map<Long, String[]> spanSvc = new HashMap<Long, String[]>();
//			Long[] rootSpan = new Long[1];
//			
//			try (Stream<Trace> stream = traceRepo.findByTraceId(buffer)) {
//				stream.forEach(x -> {
//					String[] kind = Utils.getFirstCall(x);
//
//					if (kind != null)
//						if (kind.length == 2) {
//							StringBuilder builder = new StringBuilder();
//							builder.append(kind[0]).append(' ').append(kind[1]);
//
//							String title = builder.toString();
//
//							if (!distinctService.containsKey(title))
//								distinctService.put(title, new Long[] { distinctService.size() + 1l, 0l, 0l, 0l });
//
//							Long[] newUpdt = distinctService.get(title);
//
//							if (Utils.checkError(x))
//								++newUpdt[2];
//							++newUpdt[1];
//
//							spanSvc.put(x.getPk().getSpanId(), new String[] { title });
//						} else if (kind.length == 4) {
//							StringBuilder builder = new StringBuilder();
//							builder.append(kind[0]).append(' ').append(kind[3]).append(' ').append(kind[1]);
//
//							String title = builder.toString();
//							builder.setLength(0);
//							builder.append(kind[2]).append(' ').append(kind[3]);
//
//							String title1 = builder.toString();
//
//							if (!distinctService.containsKey(title))
//								distinctService.put(title, new Long[] { distinctService.size() + 1l, 0l, 0l, 0l });
//
//							if (!distinctService.containsKey(title1))
//								distinctService.put(title1, new Long[] { distinctService.size() + 1l, 0l, 0l, 0l });
//
//							Long[] newUpdt = distinctService.get(title);
//							Long[] newUpdt1 = distinctService.get(title1);
//
//							if (Utils.checkError(x)) {
//								++newUpdt[2];
//								++newUpdt1[2];
//							}
//
//							++newUpdt[1];
//							++newUpdt1[1];
//							spanSvc.put(x.getPk().getSpanId(), new String[] { title, title1 });
//						}
//
//					List<SpanRef> ref = x.getRefs();
//
//					if (ref != null) {
//						Long parSpan = ref.get(0).getSpanId();
//
//						if (!traceRef.containsKey(parSpan)) {
//							traceRef.put(parSpan, new ArrayList<Long>());
//						}
//
//						traceRef.get(parSpan).add(x.getPk().getSpanId());
//					} else {
//						rootSpan[0] = x.getPk().getSpanId();
//					}
//
//					spanDurStart.put(x.getPk().getSpanId(), new Long[] { x.getDuration(), x.getStartTime() });
//				});
//			}
//
//			Utils.runTrace(rootSpan[0], traceRef, spanDurStart, distinctService, conn, spanSvc, 0, 0);
//			
//			System.out.println();
//			
//			distinctService.put("user", new Long[] { 0l, 0l, 0l, 0l });
//
//			List<Edge> edges = new ArrayList<Edge>();
//			List<Node> nodes = new ArrayList<Node>();
//
//			// v0: index node
//			// v1: totalCall
//			// v2: totalCall Err
//			// v3: totalDurration
//
//			conn.forEach((u, pos) -> {
//				pos.forEach(v -> {
//					edges.add(new Edge(edges.size(), u, v));
//				});
//			});
//
//			distinctService.forEach((k, v) -> {
//				double avgResp = v[1] != 0 ? (double) v[3] / (double) v[1] / 1e6 : 0; // (secs)
//				double range = 0; // range in secs
//				double reqPerMin = range != 0 ? (double) v[1] * 60 / range : 0;
//				double errRate = v[1] != 0 ? (double) v[2] / (double) v[1] : 0;
//				nodes.add(new Node(v[0].intValue(), k, Utils.round(avgResp), Utils.round(reqPerMin), Utils.round(errRate)));
//			});
//			
//			System.out.println();
//		};
//	}
}
