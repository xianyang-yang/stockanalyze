package stock.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import javax.net.ssl.SSLContext;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * create on 2018/7/29 下午8:05
 *
 * @author xianyang.yxy
 */

public class HttpUtils {
    private static final Logger logger = LoggerFactory.getLogger(HttpUtils.class);
    private static final String NL = System.getProperty("line.separator");

    public static String executeGet(String url, Charset charset) {
        StringBuilder sb = new StringBuilder(1024);
        executeGet(url, charset, (line, num) -> {
            sb.append(line + NL);
        });
        return sb.toString();
    }

    public static void executeGet(String url, Charset charset, BiConsumer<String, Integer> consumer) {
        logger.info("httpget:{}", url);
        BufferedReader in = null;
        try {
            CloseableHttpClient client = createSSLClientDefault();

            StringBuffer sb = new StringBuffer("");
            // 定义HttpClient
            // HttpClient client = new DefaultHttpClient();
            // 实例化HTTP方法
            HttpGet request = new HttpGet();
            request.setURI(new URI(url));
            HttpResponse response = client.execute(request);
            in = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), charset));

            String line;
            int lineNum = 0;
            while ((line = in.readLine()) != null) {
                consumer.accept(line, ++lineNum);
            }
        } catch (Exception e) {
            logger.error("error httpget", e);
        } finally {
            if (in != null) {
                try {
                    in.close();// 最后要关闭BufferedReader
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static JSONObject executePost(String url, JSONObject params) {
        HttpPost post = null;
        try {
            HttpClient httpClient = new DefaultHttpClient();

            post = new HttpPost(url);
            // 构造消息头
            post.setHeader("Content-type", "application/json");

            // 构建消息实体
            Charset charset = Charset.forName("UTF-8");
            StringEntity entity = new StringEntity(params.toString(), charset);
            // 发送Json格式的数据请求
            entity.setContentType("application/json");
            post.setEntity(entity);
            HttpResponse response = httpClient.execute(post);
            String result = EntityUtils.toString(response.getEntity(), charset);
            //logger.info("httpclient execute post:{},{}",url,result);
            return StringUtils.isEmpty(result) ? null : JSON.parseObject(result);
        } catch (Exception e) {
            logger.error("error executePost:{},{}", params, url, e);
            throw new RuntimeException(e);
        } finally {
            if (post != null) {
                try {
                    post.releaseConnection();
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String buildUrl(String tempUrl, String... params) {
        StringBuilder sb = new StringBuilder(tempUrl);
        for (String p : params) {
            sb.append('&').append(p);
        }
        return sb.toString();
    }

    public static CloseableHttpClient createSSLClientDefault() {
        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                // 信任所有
                @Override
                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    return true;
                }
            }).build();
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
            return HttpClients.custom().setSSLSocketFactory(sslsf).build();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        return HttpClients.createDefault();
    }
}
