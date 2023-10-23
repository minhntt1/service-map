package com.example.demo.component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class Calculation {
	public static long calculateBucket(long ts, long bucketSize) {
		return ts / bucketSize * bucketSize;
	}
	
	public static List<Long> calBuckets(long bucketSize, Long start, Long end) {
		List<Long> buckets = new ArrayList<Long>();
		long endBucket = Calculation.calculateBucket(end, bucketSize);
		for (long startBucket = Calculation.calculateBucket(start, bucketSize); startBucket <= endBucket; startBucket += bucketSize) {
			buckets.add(startBucket);
		}
		return buckets;
	}
	
	public static double round(double d) {
		return BigDecimal.valueOf(d).setScale(2,RoundingMode.HALF_UP).doubleValue();
	}
}
