package com.hw.demo.biz.filter;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

public interface HttpRequestFilter {

    /**
     * 前置检查
     * @param fullRequest
     */
    String preFilter(FullHttpRequest fullRequest);

    /**
     * 检查拦截
     * @param fullRequest
     * @param ctx
     */
    void filter(FullHttpRequest fullRequest, ChannelHandlerContext ctx);
    
}
