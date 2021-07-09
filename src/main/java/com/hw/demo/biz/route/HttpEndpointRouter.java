package com.hw.demo.biz.route;

import java.util.List;

public interface HttpEndpointRouter {
    
    String route(List<String> endpoints);

    String route(List<String> endpoints, final String routeStr);
    
    // Load Balance
    // Random
    // RoundRibbon 
    // Weight
    // - server01,20
    // - server02,30
    // - server03,50
    
}
