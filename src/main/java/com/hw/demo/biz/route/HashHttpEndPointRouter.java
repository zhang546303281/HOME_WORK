package com.hw.demo.biz.route;

import com.google.common.collect.Range;
import com.google.common.hash.Hashing;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HashHttpEndPointRouter implements HttpEndpointRouter {

    private static final int buckets = 1024;
    private Map<Integer, Range<Integer>> shardDefinition;

    public HashHttpEndPointRouter() {
        shardDefinition = new HashMap<>();
        shardDefinition.put(0, Range.closed(0, 511));
        shardDefinition.put(1, Range.closed(512, 1023));
    }

    @Override
    public String route(List<String> endpoints) {
        RandomHttpEndpointRouter randomRoute = new RandomHttpEndpointRouter();
        return randomRoute.route(endpoints);
    }

    @Override
    public String route(List<String> endpoints, final String routeStr) {
        if (null == routeStr) {
            throw new RuntimeException("routeStr must not be null!");
        }

        if (CollectionUtils.isEmpty(endpoints)) {
            throw new RuntimeException("endpoints must not be null!");
        }

        int index = getShard(routeStr);
        for (int i = 0; i < endpoints.size(); i++) {
            //按奇偶数分片固定两片匹配上就取当前地址
            if (isOddNum(index) && isOddNum(i)) {
                return endpoints.get(i);
            }

            if (!isOddNum(index) && !isOddNum(i)) {
                return endpoints.get(i);
            }
        }
        return endpoints.get(0);
    }

    private int getDBRation(String shardText) {
        return Hashing.consistentHash(shardText.hashCode(), buckets);
    }

    public int getShard(String shardText) {
        //按1024分片利用jump consistent Hash计算出在0-buckets的随机数
        int ratio = getDBRation(shardText);
        //查询hash在哪个分片下
        for (Map.Entry<Integer, Range<Integer>> entry : shardDefinition.entrySet()) {
            Range<Integer> shardRange = entry.getValue();
            if (shardRange.contains(ratio)) {
                return entry.getKey();
            }
        }

        throw new RuntimeException("unable to route, pls check!");
    }

    private boolean isOddNum(int num) {
        return (num & 1) == 1;
    }
}
