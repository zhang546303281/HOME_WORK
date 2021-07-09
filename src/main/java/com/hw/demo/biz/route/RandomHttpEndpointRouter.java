package com.hw.demo.biz.route;

import java.util.List;
import java.util.Random;

public class RandomHttpEndpointRouter implements HttpEndpointRouter {
    @Override
    public String route(List<String> urls) {
        int size = urls.size();
        Random random = new Random(System.currentTimeMillis());
        return urls.get(random.nextInt(size));
    }

    @Override
    public String route(List<String> endpoints, String routeStr) {
        HttpEndpointRouter hashRoute = new HashHttpEndPointRouter();
        return hashRoute.route(endpoints, routeStr);
    }
}
