package com.hw.demo.biz.tool;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpClientAsync {

    private final static Logger LOGGER = LoggerFactory.getLogger(HttpClientAsync.class);

    private volatile static HttpClientAsync httpClient;

    private CloseableHttpAsyncClient closeableHttpAsyncClient;

    public static HttpClientAsync getInstance() {
        if (null == httpClient) {
            synchronized (HttpClientAsync.class) {
                if (null == httpClient) {
                    httpClient = new HttpClientAsync();
                }
            }
        }
        return httpClient;
    }

    private void initClient() {
        if (null != closeableHttpAsyncClient)
            return;

        int cores = Runtime.getRuntime().availableProcessors();
        IOReactorConfig ioConfig = IOReactorConfig.custom()
                .setConnectTimeout(1000)
                .setSoTimeout(1000)
                .setIoThreadCount(cores)
                .setRcvBufSize(32 * 1024)
                .build();

        closeableHttpAsyncClient = HttpAsyncClients.custom().setMaxConnTotal(40)
                .setMaxConnPerRoute(8)
                .setDefaultIOReactorConfig(ioConfig)
                .setKeepAliveStrategy((response, context) -> 6000)
                .build();
        closeableHttpAsyncClient.start();
    }

    public void executeGet(String url) {
        initClient();
        final HttpGet httpGet = new HttpGet(url);
        closeableHttpAsyncClient.execute(httpGet, new FutureCallback<>() {
            @Override
            public void completed(final HttpResponse endpointResponse) {
                try {
                    String result = handleResponse(endpointResponse);
                    LOGGER.info("executeGet result:{}", result);
                } catch (Exception e) {
                    LOGGER.error("executeGet completed error:", e);
                } finally {
                }
            }

            @Override
            public void failed(final Exception e) {
                LOGGER.error("executeGet failed error:", e);
                httpGet.abort();
            }

            @Override
            public void cancelled() {
                LOGGER.error("executeGet cancelled!");
                httpGet.abort();
            }
        });
    }

    private String handleResponse(final HttpResponse endpointResponse) {
        try {
            return EntityUtils.toString(endpointResponse.getEntity());
        } catch (Exception e) {
            LOGGER.error("executeGet handleResponse error:", e);
            return "";
        } finally {

        }
    }

    public static void main(String[] args) {
        HttpClientAsync asyncClient = HttpClientAsync.getInstance();
        asyncClient.executeGet("http://127.0.0.1:8888/test");
    }
}
