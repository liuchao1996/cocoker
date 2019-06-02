package com.cocoker.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * 发送http请求
 */
@Slf4j
public class HttpClientUtil {


	private static String user_name = "kettle";
	private static String user_pwd = "aimedical!";

	public static Object post(String url, JSONObject data) {

		CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
		HttpPost post = new HttpPost(url);
		CloseableHttpResponse httpResponse = null;
		String res = null;
		try {
			post.addHeader("Authorization", getHeader(user_name, user_pwd));
			// 设置请求超时时间
			RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(5000).setSocketTimeout(300000)
					.build();
			post.setConfig(requestConfig);
			// 设置参数
			HttpEntity httpEntity = new StringEntity(JSON.toJSONString(data), ContentType.APPLICATION_JSON);
			post.setEntity(httpEntity);
			// 执行请求
			httpResponse = closeableHttpClient.execute(post);
			// 获取响应状态
			StatusLine status = httpResponse.getStatusLine();
			// 获取响应结果
			HttpEntity result = httpResponse.getEntity();
			res = EntityUtils.toString(result, "utf-8");
			log.info("状态：" + status + "---结果：" + res);
			return res;
		} catch (IOException e) {
			// TODO: handle exception
			if (log.isInfoEnabled()) {
				log.error(e.getMessage());
			}
			e.printStackTrace();
		} finally {
			HttpClientUtils.closeQuietly(closeableHttpClient);
		}
		return null;
	}

	/**
	 * 封装HTTP GET方法 无参数的Get请求
	 *
	 * @param
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static Object get(String url) {
		CloseableHttpClient httpCilent = HttpClients.createDefault();
		// 发送get请求
		HttpGet get = new HttpGet(url);
		get.addHeader("Authorization", getHeader(user_name, user_pwd));
		String res = null;
		try {
			CloseableHttpResponse response = httpCilent.execute(get);
			int statuscode = response.getStatusLine().getStatusCode();
			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				res = EntityUtils.toString(entity, "utf-8");
				log.info("状态：" + statuscode + "---结果：" + res);
			} else if (response.getStatusLine().getStatusCode() == 400) {
				log.info("400");
			} else if (response.getStatusLine().getStatusCode() == 500) {
				log.info("500");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				httpCilent.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return res;
	}


	/**
	 * 构造Basic Auth认证头信息
	 *
	 * @return
	 */
	private static String getHeader(String APP_KEY, String SECRET_KEY) {
		String auth = APP_KEY + ":" + SECRET_KEY;
		byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")));
		String authHeader = "Basic " + new String(encodedAuth);
		return authHeader;
	}
}
