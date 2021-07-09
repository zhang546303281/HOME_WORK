package com.hw.demo.biz.filter;

import com.alibaba.fastjson.JSONObject;
import com.hw.demo.biz.tool.JwtUtil;
import io.jsonwebtoken.Claims;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import org.apache.commons.lang3.StringUtils;

public class HeaderHttpRequestFilter implements HttpRequestFilter {

    @Override
    public void filter(FullHttpRequest fullRequest, ChannelHandlerContext ctx) {
        fullRequest.headers().remove("token");//不再继续传递了
    }

    @Override
    public String preFilter(FullHttpRequest fullRequest) {
        return validaAndGetRouteKey(fullRequest);
    }

    /**
     * 校验token并且从中获取路由字段
     *
     * @param fullRequest FullHttpRequest
     * @return String
     */
    private String validaAndGetRouteKey(final FullHttpRequest fullRequest) {
        String token = fullRequest.headers().get("token");
        if (StringUtils.isBlank(token)) {
            return "";
        }

        Claims claims = JwtUtil.parseJWT(token);
        JSONObject json = JSONObject.parseObject(claims.getSubject());
        if (null == json || json.getString("userTrueName") == null) {
            throw new RuntimeException("invalid param!");
        }

        String routeKey = json.getString("userTrueName");
        return StringUtils.length(routeKey) > 4 ? StringUtils.substring(routeKey, routeKey.length() - 4, routeKey.length()) : routeKey;
    }
}
