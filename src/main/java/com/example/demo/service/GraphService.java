package com.example.demo.service;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.example.demo.dto.Edge;
import com.example.demo.dto.GraphData;
import com.example.demo.dto.Node;
import com.example.demo.model.ServiceNameIndex;
import com.example.demo.model.SpanRef;
import com.example.demo.model.Trace;
import com.example.demo.repo.ServiceNameIndexRepo;
import com.example.demo.repo.TraceRepo;
import com.example.demo.util.Utils;

@Service
public class GraphService {
	private TraceRepo traceRepo;
	private ServiceNameIndexRepo serviceNameIndexRepo;

	public GraphService(TraceRepo repo, ServiceNameIndexRepo serviceNameIndexRepo) {
		// TODO Auto-generated constructor stub
		this.traceRepo = repo;
		this.serviceNameIndexRepo = serviceNameIndexRepo;
	}

	public GraphData graphData(Long start, Long end) {
		HashSet<ByteBuffer> distinctTraceId = new HashSet<ByteBuffer>();

		try (Stream<ServiceNameIndex> indexs = serviceNameIndexRepo.findByTimeRange("gateway", start * 1000, end * 1000)) {
			indexs.forEach(x -> distinctTraceId.add(x.getTraceId()));
		}

		Map<String, Long[]> distinctService = new HashMap<String, Long[]>();
		Map<Integer, HashSet<Integer>> conn = new HashMap<Integer, HashSet<Integer>>();

		for (ByteBuffer buffer : distinctTraceId) {
			Map<Long, ArrayList<Long>> traceRef = new HashMap<Long, ArrayList<Long>>();
			Map<Long, Long[]> spanDurStart = new HashMap<Long, Long[]>();
			Map<Long, String[]> spanSvc = new HashMap<Long, String[]>();
			HashSet<Long> spanIsConsumer = new HashSet<Long>();
			Long[] rootSpan = new Long[1];
			
			try (Stream<Trace> stream = traceRepo.findByTraceId(buffer)) {
				stream.forEach(x -> {
					String[] kind = Utils.getFirstCall(x);

					if (kind != null) {
						if (kind.length == 2) {
							StringBuilder builder = new StringBuilder();
							builder.append(kind[0]).append(' ').append(kind[1]);

							String title = builder.toString();

							if (!distinctService.containsKey(title))
								distinctService.put(title, new Long[] { distinctService.size() + 1l, 0l, 0l, 0l });

							Long[] newUpdt = distinctService.get(title);

							if (Utils.checkError(x))
								++newUpdt[2];
							++newUpdt[1];

							spanSvc.put(x.getPk().getSpanId(), new String[] { title });
						} else if (kind.length == 4) {
							StringBuilder builder = new StringBuilder();
							builder.append(kind[0]).append(' ').append(kind[3]).append(' ').append(kind[1]);

							String title = builder.toString();
							builder.setLength(0);
							builder.append(kind[2]).append(' ').append(kind[3]);

							String title1 = builder.toString();

							if (!distinctService.containsKey(title))
								distinctService.put(title, new Long[] { distinctService.size() + 1l, 0l, 0l, 0l });

							if (!distinctService.containsKey(title1))
								distinctService.put(title1, new Long[] { distinctService.size() + 1l, 0l, 0l, 0l });

							Long[] newUpdt = distinctService.get(title);
							Long[] newUpdt1 = distinctService.get(title1);

							if (Utils.checkError(x)) {
								++newUpdt[2];
								++newUpdt1[2];
							}

							++newUpdt[1];
							++newUpdt1[1];
							spanSvc.put(x.getPk().getSpanId(), new String[] { title, title1 });
						}
					}
					else if(Utils.isConsumer(x)) {
						String[] resolveSvc = Utils.getServiceCall(x);
						spanSvc.put(x.getPk().getSpanId(), new String[] {new StringBuilder().append(resolveSvc[0]).append(' ').append(resolveSvc[1]).toString()});
					}

					List<SpanRef> ref = x.getRefs();

					if (ref != null) {
						Long parSpan = ref.get(0).getSpanId();

						if (!traceRef.containsKey(parSpan)) {
							traceRef.put(parSpan, new ArrayList<Long>());
						}

						traceRef.get(parSpan).add(x.getPk().getSpanId());
					} else {
						rootSpan[0] = x.getPk().getSpanId();
					}

					if(Utils.isConsumer(x))
						spanIsConsumer.add(x.getPk().getSpanId());
					spanDurStart.put(x.getPk().getSpanId(), new Long[] { x.getDuration(), x.getStartTime() });
				});
			}

			Utils.runTrace(rootSpan[0], traceRef, spanDurStart, distinctService, conn, spanIsConsumer, spanSvc, 0, 0);
		}

		distinctService.put("user", new Long[] { 0l, 0l, 0l, 0l });

		List<Edge> edges = new ArrayList<Edge>();
		List<Node> nodes = new ArrayList<Node>();

		// v0: index node
		// v1: totalCall
		// v2: totalCall Err
		// v3: totalDurration

		conn.forEach((u, pos) -> {
			pos.forEach(v -> {
				edges.add(new Edge(edges.size(), u, v));
			});
		});

		distinctService.forEach((k, v) -> {
			double avgResp = v[1] != 0 ? (double) v[3] / (double) v[1] / 1e6 : 0; // (secs)
			double range = (end - start) / 1000; // range in secs
			double reqPerMin = range != 0 ? (double) v[1] * 60 / range : 0;
			double errRate = v[1] != 0 ? (double) v[2] / (double) v[1] : 0;
			nodes.add(new Node(v[0].intValue(), k, Utils.round(avgResp), Utils.round(reqPerMin), Utils.round(errRate)));
		});

		return new GraphData(edges, nodes);
	}
}
