package cn.com.kelaikewang.infrastructure.id.factory.leaf;

import cn.com.kelaikewang.infrastructure.id.factory.IdWorker;
import cn.com.kelaikewang.infrastructure.id.factory.leaf.config.LeafConfigProperties;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component("leaf")
public class Leaf implements IdWorker<Long>, ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    private LeafConfigProperties leafConfigProperties;
    private static String leafServerRequestUrl;
    @Override
    public Long nextId()  {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpGet httpget = new HttpGet(leafServerRequestUrl);
            CloseableHttpResponse response = httpclient.execute(httpget);
            try {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    String id = EntityUtils.toString(entity);
                    return Long.valueOf(id);
                }else {
                    throw new LeafIdClientException("entity为null");
                }
            }catch (ParseException parseException){
                throw new LeafIdClientException("获取id异常",parseException);
            } finally{
                response.close();
            }
        } catch (ClientProtocolException e) {
            throw new LeafIdClientException("获取id异常",e);
        } catch (IOException e) {
            throw new LeafIdClientException("获取id异常",e);
        } finally{
            try {
                httpclient.close();
            } catch (IOException e) {
                throw new LeafIdClientException("获取id异常",e);
            }
        }
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        String uri =  "segment".equals(leafConfigProperties.getGenerator())
                ? "/api/segment/get/" + leafConfigProperties.getKey()
                : "/api/snowflake/get/" + leafConfigProperties.getKey();
        leafServerRequestUrl = leafConfigProperties.getServer() + uri;
    }
}
