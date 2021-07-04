package cn.com.kelaikewang.commons.web;

import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class HttpUtils {
    private HttpUtils(){

    }
    public static String get(String url, Map<String,String> parameters) throws UnsupportedEncodingException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet httpGet = null;
        if (parameters != null && parameters.size() > 0){
            StringBuilder stringBuilder = new StringBuilder(url);
            if (!url.contains("?")){
                stringBuilder.append("?");
            }
            for (Map.Entry<String,String> item : parameters.entrySet()){
                stringBuilder.append(item.getKey());
                stringBuilder.append("=");
                stringBuilder.append(URLEncoder.encode(item.getValue(),"UTF-8"));
            }
            httpGet = new HttpGet(stringBuilder.toString());
        }else {
            httpGet = new HttpGet(url);
        }
        try {
            CloseableHttpResponse response = httpClient.execute(httpGet);
            HttpEntity responseEntity = response.getEntity();
            return EntityUtils.toString(responseEntity);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }


    }
    public static String postForm(String url, List<BasicNameValuePair> parameters){
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost httpPost = null;
        if (parameters != null && parameters.size() > 0){

            httpPost = new HttpPost(url);
            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(parameters, StandardCharsets.UTF_8);
            httpPost.setEntity(urlEncodedFormEntity);
        }else {
            httpPost = new HttpPost(url);
        }
        try {
            CloseableHttpResponse response = httpClient.execute(httpPost);
            HttpEntity responseEntity = response.getEntity();
            return EntityUtils.toString(responseEntity);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
