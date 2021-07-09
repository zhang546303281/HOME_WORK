package com.hw.demo.biz.tool;

import com.hw.demo.biz.bean.UserInfo;
import org.apache.commons.codec.Charsets;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.security.cert.X509Certificate;

public class HttpClientSync {
    private final static Logger LOGGER = LoggerFactory.getLogger(HttpClientSync.class);
    private static final String DEFAULT_ENCODE = Charsets.UTF_8.name();
    private static final int DEFAULT_TIMEOUT = 3000;

    private static final int DEFAULT_SO_TIMEOUT = 5000;

    private static final int DEFAULT_MAX_CONN_TOTAL = 50;

    private static final int DEFAULT_MAX_CONN_PER_ROUTE = 20;

    private static final int DEFAULT_REQUEST_CONN_TIMEOUT = 5000;

    private HttpClient httpClient;

    private RequestConfig httpRequestConfig;

    public static void main(String[] args) {
        try {
            HttpClientSync httpClientTest = new HttpClientSync();
            httpClientTest.initHttpClientHelper();
            String result = httpClientTest.executeGet("http://127.0.0.1:8917/get", "now="+System.currentTimeMillis());
            Assert.notEmpty(new Object[]{result}, "result must be not null!");
        } catch (Exception e) {
            LOGGER.error("error:", e);
        }
    }

    public String executeGet(String url, String parameter) throws IOException {
        HttpGet httpRequest = null;
        try {
            httpRequest = createHttpRequest(url, parameter);
            HttpResponse response = httpClient.execute(httpRequest);
            return resolveResponse(response, null);
        } catch (IOException e) {
            throw e;
        } finally {
            if (httpRequest != null) {
                httpRequest.abort();
            }
        }
    }

    private HttpGet createHttpRequest(String url, String parameter) {
        HttpGet getTypeRequest = new HttpGet(url + "?" + parameter);
        getTypeRequest.setConfig(httpRequestConfig);
        String subject = JwtUtil.generalSubject(new UserInfo("jaeo", "jackeo"));
        getTypeRequest.setHeader("token", JwtUtil.createJWT("jwt", subject, 60 * 60 * 1000));
        return getTypeRequest;
    }

    private String resolveResponse(HttpResponse response, String resCharset) throws IOException {
        HttpEntity entity = response.getEntity();
        BufferedReader reader = null;
        StringBuilder strBuilder = new StringBuilder();
        try {
            if (!StringUtils.hasText(resCharset)) {
                reader = new BufferedReader(new InputStreamReader(entity.getContent(), DEFAULT_ENCODE));
            } else {
                reader = new BufferedReader(new InputStreamReader(entity.getContent(), resCharset));
            }
            String line;
            while ((line = reader.readLine()) != null) {
                strBuilder.append(line);
            }
        } catch (UnsupportedOperationException e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.error("", e);
            }
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
        EntityUtils.consumeQuietly(entity);

        return strBuilder.toString();
    }

    private void initHttpClientHelper() {
        httpRequestConfig = RequestConfig.custom().setConnectTimeout(DEFAULT_TIMEOUT).setSocketTimeout(DEFAULT_SO_TIMEOUT)
                .setConnectionRequestTimeout(DEFAULT_REQUEST_CONN_TIMEOUT).build();
        httpClient = HttpClients.custom().setMaxConnTotal(DEFAULT_MAX_CONN_TOTAL).setMaxConnPerRoute(DEFAULT_MAX_CONN_PER_ROUTE)
                .setDefaultRequestConfig(httpRequestConfig).setSSLSocketFactory(createSSLConnSocketFactory()).build();
    }

    private static SSLConnectionSocketFactory createSSLConnSocketFactory() {
        SSLConnectionSocketFactory sslsf = null;
        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, (chain, authType) -> true).build();
            sslsf = new SSLConnectionSocketFactory(sslContext, new X509HostnameVerifier() {

                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }

                @Override
                public void verify(String host, SSLSocket ssl) throws IOException {
                }

                @Override
                public void verify(String host, X509Certificate cert) throws SSLException {
                }

                @Override
                public void verify(String host, String[] cns, String[] subjectAlts) throws SSLException {
                }
            });
        } catch (GeneralSecurityException e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("", e);
            }
        }
        return sslsf;
    }
}
