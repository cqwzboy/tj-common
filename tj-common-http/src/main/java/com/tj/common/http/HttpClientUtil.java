package com.tj.common.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpTrace;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tj.common.http.HttpHeader.Headers;
import com.tj.common.http.HttpHeader.HttpReqHead;
import com.tj.common.io.GzipUtils;

/**
 * 使用HttpClient模拟发送（http/https）请求
 * 
 * @author silongz
 *
 */
public class HttpClientUtil {
	private static final Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);

	// 默认采用的http协议的HttpClient对象
	private static HttpClient client4HTTP;

	// 默认采用的https协议的HttpClient对象
	private static HttpClient client4HTTPS;

	static {
		try {
			client4HTTP = TjHttpClientBuilder.custom().build();
			client4HTTPS = TjHttpClientBuilder.custom().ssl().build();
		} catch (HttpProcessException e) {
			logger.error("创建https协议的HttpClient对象出错：{}", e);
		}
	}

	/**
	 * 判断url是http还是https，直接返回相应的默认client对象
	 * 
	 * @return 返回对应默认的client对象
	 * @throws HttpProcessException
	 */
	private static HttpClient create(String url) throws HttpProcessException {
		if (url.toLowerCase().startsWith("https://")) {
			return client4HTTPS;
		} else {
			return client4HTTP;
		}
	}

	/**
	 * 以Get方式，请求资源或服务
	 * 
	 * @param client
	 *            client对象
	 * @param url
	 *            资源地址
	 * @param headers
	 *            请求头信息
	 * @param context
	 *            http上下文，用于cookie操作
	 * @param encoding
	 *            编码
	 * @return 返回处理结果
	 * @throws HttpProcessException
	 */
	public static String get(HttpClient client, String url, Header[] headers, HttpContext context, String encoding)
			throws HttpProcessException {
		return send(HttpConfig.custom().client(client).url(url).method(HttpMethods.GET).headers(headers).context(context)
				.encoding(encoding));
	}

	/**
	 * 以Get方式，请求资源或服务
	 * 
	 * @param config
	 *            请求参数配置
	 * @return
	 * @throws HttpProcessException
	 */
	public static String get(HttpConfig config) throws HttpProcessException {
		return send(config.method(HttpMethods.GET));
	}

	/**
	 * 以Post方式，请求资源或服务
	 * 
	 * @param client
	 *            client对象
	 * @param url
	 *            资源地址
	 * @param parasMap
	 *            请求参数
	 * @param headers
	 *            请求头信息
	 * @param context
	 *            http上下文，用于cookie操作
	 * @param encoding
	 *            编码
	 * @return 返回处理结果
	 * @throws HttpProcessException
	 */
	public static String post(HttpClient client, String url, Header[] headers, Map<String, Object> parasMap, HttpContext context,
			String encoding) throws HttpProcessException {
		return send(HttpConfig.custom().client(client).url(url).method(HttpMethods.POST).headers(headers).map(parasMap).context(context)
				.encoding(encoding));
	}

	/**
	 * 以Post方式，请求资源或服务
	 * 
	 * @param config
	 *            请求参数配置
	 * @return
	 * @throws HttpProcessException
	 */
	public static String post(HttpConfig config) throws HttpProcessException {
		return send(config.method(HttpMethods.POST));
	}

	public static String post(HttpConfig config, String encoding) throws HttpProcessException {
		return send(config.method(HttpMethods.POST), encoding);
	}

	/**
	 * 以Put方式，请求资源或服务
	 * 
	 * @param client
	 *            client对象
	 * @param url
	 *            资源地址
	 * @param parasMap
	 *            请求参数
	 * @param headers
	 *            请求头信息
	 * @param context
	 *            http上下文，用于cookie操作
	 * @param encoding
	 *            编码
	 * @return 返回处理结果
	 * @throws HttpProcessException
	 */
	public static String put(HttpClient client, String url, Map<String, Object> parasMap, Header[] headers, HttpContext context,
			String encoding) throws HttpProcessException {
		return send(HttpConfig.custom().client(client).url(url).method(HttpMethods.PUT).headers(headers).map(parasMap).context(context)
				.encoding(encoding));
	}

	/**
	 * 以Put方式，请求资源或服务
	 * 
	 * @param config
	 *            请求参数配置
	 * @return
	 * @throws HttpProcessException
	 */
	public static String put(HttpConfig config) throws HttpProcessException {
		return send(config.method(HttpMethods.PUT));
	}

	/**
	 * 以Delete方式，请求资源或服务
	 * 
	 * @param client
	 *            client对象
	 * @param url
	 *            资源地址
	 * @param headers
	 *            请求头信息
	 * @param context
	 *            http上下文，用于cookie操作
	 * @param encoding
	 *            编码
	 * @return 返回处理结果
	 * @throws HttpProcessException
	 */
	public static String delete(HttpClient client, String url, Header[] headers, HttpContext context, String encoding)
			throws HttpProcessException {
		return send(HttpConfig.custom().client(client).url(url).method(HttpMethods.DELETE).headers(headers).context(context)
				.encoding(encoding));
	}

	/**
	 * 以Delete方式，请求资源或服务
	 * 
	 * @param config
	 *            请求参数配置
	 * @return
	 * @throws HttpProcessException
	 */
	public static String delete(HttpConfig config) throws HttpProcessException {
		return send(config.method(HttpMethods.DELETE));
	}

	/**
	 * 以Patch方式，请求资源或服务
	 * 
	 * @param client
	 *            client对象
	 * @param url
	 *            资源地址
	 * @param parasMap
	 *            请求参数
	 * @param headers
	 *            请求头信息
	 * @param context
	 *            http上下文，用于cookie操作
	 * @param encoding
	 *            编码
	 * @return 返回处理结果
	 * @throws HttpProcessException
	 */
	public static String patch(HttpClient client, String url, Map<String, Object> parasMap, Header[] headers, HttpContext context,
			String encoding) throws HttpProcessException {
		return send(HttpConfig.custom().client(client).url(url).method(HttpMethods.PATCH).headers(headers).map(parasMap).context(context)
				.encoding(encoding));
	}

	/**
	 * 以Patch方式，请求资源或服务
	 * 
	 * @param config
	 *            请求参数配置
	 * @return
	 * @throws HttpProcessException
	 */
	public static String patch(HttpConfig config) throws HttpProcessException {
		return send(config.method(HttpMethods.PATCH));
	}

	/**
	 * 以Head方式，请求资源或服务
	 * 
	 * @param client
	 *            client对象
	 * @param url
	 *            资源地址
	 * @param headers
	 *            请求头信息
	 * @param context
	 *            http上下文，用于cookie操作
	 * @param encoding
	 *            编码
	 * @return 返回处理结果
	 * @throws HttpProcessException
	 */
	public static String head(HttpClient client, String url, Header[] headers, HttpContext context, String encoding)
			throws HttpProcessException {
		return send(HttpConfig.custom().client(client).url(url).method(HttpMethods.HEAD).headers(headers).context(context)
				.encoding(encoding));
	}

	/**
	 * 以Head方式，请求资源或服务
	 * 
	 * @param config
	 *            请求参数配置
	 * @return
	 * @throws HttpProcessException
	 */
	public static String head(HttpConfig config) throws HttpProcessException {
		return send(config.method(HttpMethods.HEAD));
	}

	/**
	 * 以Options方式，请求资源或服务
	 * 
	 * @param client
	 *            client对象
	 * @param url
	 *            资源地址
	 * @param headers
	 *            请求头信息
	 * @param context
	 *            http上下文，用于cookie操作
	 * @param encoding
	 *            编码
	 * @return 返回处理结果
	 * @throws HttpProcessException
	 */
	public static String options(HttpClient client, String url, Header[] headers, HttpContext context, String encoding)
			throws HttpProcessException {
		return send(HttpConfig.custom().client(client).url(url).method(HttpMethods.OPTIONS).headers(headers).context(context)
				.encoding(encoding));
	}

	/**
	 * 以Options方式，请求资源或服务
	 * 
	 * @param config
	 *            请求参数配置
	 * @return
	 * @throws HttpProcessException
	 */
	public static String options(HttpConfig config) throws HttpProcessException {
		return send(config.method(HttpMethods.OPTIONS));
	}

	/**
	 * 以Trace方式，请求资源或服务
	 * 
	 * @param client
	 *            client对象
	 * @param url
	 *            资源地址
	 * @param headers
	 *            请求头信息
	 * @param context
	 *            http上下文，用于cookie操作
	 * @param encoding
	 *            编码
	 * @return 返回处理结果
	 * @throws HttpProcessException
	 */
	public static String trace(HttpClient client, String url, Header[] headers, HttpContext context, String encoding)
			throws HttpProcessException {
		return send(HttpConfig.custom().client(client).url(url).method(HttpMethods.TRACE).headers(headers).context(context)
				.encoding(encoding));
	}

	/**
	 * 以Trace方式，请求资源或服务
	 * 
	 * @param config
	 *            请求参数配置
	 * @return
	 * @throws HttpProcessException
	 */
	public static String trace(HttpConfig config) throws HttpProcessException {
		return send(config.method(HttpMethods.TRACE));
	}

	/**
	 * 下载图片
	 * 
	 * @param client
	 *            client对象
	 * @param url
	 *            资源地址
	 * @param headers
	 *            请求头信息
	 * @param context
	 *            http上下文，用于cookie操作
	 * @param out
	 *            输出流
	 * @return 返回处理结果
	 * @throws HttpProcessException
	 */
	public static OutputStream down(HttpClient client, String url, Header[] headers, HttpContext context, OutputStream out)
			throws HttpProcessException {
		return fmt2Stream(execute(HttpConfig.custom().client(client).url(url).method(HttpMethods.GET).headers(headers).context(context)
				.out(out)), out);
	}

	/**
	 * 下载图片
	 * 
	 * @param config
	 *            请求参数配置
	 * @param out
	 *            输出流
	 * @return 返回处理结果
	 * @throws HttpProcessException
	 */
	public static OutputStream down(HttpConfig config) throws HttpProcessException {
		return fmt2Stream(execute(config.method(HttpMethods.GET)), config.out());
	}

	/**
	 * 请求资源或服务
	 * 
	 * @param config
	 * @return
	 * @throws HttpProcessException
	 */
	public static String send(HttpConfig config) throws HttpProcessException {
		config.inenc(Headers.CONTENT_CHARSET_UTF8) ;
		return fmt2String(execute(config), config.outenc());
	}

	/**
	 * xml方式请求
	 * 
	 * @param config
	 * @param xml
	 * @return
	 * @throws HttpProcessException
	 */
	public static String sendXml(HttpConfig config, String xml) throws HttpProcessException {
		Header[] headers = HttpHeader.custom().contentType(Headers.TEXT_XML).build();
		StringEntity xmlEntity = new StringEntity(xml, Headers.CONTENT_CHARSET_UTF8);
		config = config.headers(headers).stringEntity(xmlEntity);
		return HttpClientUtil.post(config, Headers.CONTENT_CHARSET_UTF8);
	}

	/**
	 * json方式请求
	 * 
	 * @param config
	 * @param json
	 * @return
	 * @throws HttpProcessException
	 * @throws UnsupportedEncodingException
	 */
	public static String sendJson(HttpConfig config, String json) throws HttpProcessException, UnsupportedEncodingException {
		Header[] headers = HttpHeader.custom().contentType(Headers.APPLICATION_JSON).build();
		StringEntity jsonEntity = new StringEntity(json,Headers.CONTENT_CHARSET_UTF8);
		config = config.headers(headers).stringEntity(jsonEntity);
		return HttpClientUtil.post(config, Headers.CONTENT_CHARSET_UTF8);
	}

	/**
	 * GZIP压缩请求
	 * @param config
	 * @param data
	 * @param timeoutMills 超时时间(毫秒)
	 * @return
	 * @throws HttpProcessException
	 * @throws IOException
	 */
	public static String sendWithGzip(String url, String data , int timeoutMills) throws Exception {
		Header[] headers = HttpHeader.custom().accept(Headers.TEXT_PLAIN).contentEncoding(Headers.ENCODING_BY_GZIP).build();
		byte[] compressData = GzipUtils.compress(data.getBytes(Consts.UTF_8));
		ByteArrayEntity byteArrayEntity = new ByteArrayEntity(compressData, ContentType.create(Headers.TEXT_PLAIN, Consts.UTF_8));
		HttpConfig config = HttpConfig.custom().url(url).headers(headers).byteArrayEntity(byteArrayEntity).timeoutMills(timeoutMills);
		return HttpClientUtil.post(config, Headers.CONTENT_CHARSET_UTF8);
	}

	/**
	 * 请求资源或服务
	 * 
	 * @param config
	 * @return
	 * @throws HttpProcessException
	 */
	public static String send(HttpConfig config, String encoding) throws HttpProcessException {
		config.inenc(encoding) ;
		return fmt2String(execute(config), encoding);
	}

	/**
	 * 请求资源或服务
	 * 
	 * @param client
	 *            client对象
	 * @param url
	 *            资源地址
	 * @param httpMethod
	 *            请求方法
	 * @param parasMap
	 *            请求参数
	 * @param headers
	 *            请求头信息
	 * @param encoding
	 *            编码
	 * @return 返回处理结果
	 * @throws HttpProcessException
	 */
	private static HttpResponse execute(HttpConfig config) throws HttpProcessException {
		if (config.client() == null) {// 检测是否设置了client
			config.client(create(config.url()));
		}
		HttpResponse resp = null;
		try {
			int timeoutMills = config.timeoutMills() ;
			if(timeoutMills <= 0){
				timeoutMills = 5000 ;
			}
			RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(3000).setConnectionRequestTimeout(3000).setSocketTimeout(timeoutMills)
						.build();
			// 创建请求对象
			HttpRequestBase request = getRequest(config.url(), config.method());
			// 设置header信息
			request.setHeaders(config.headers());
			request.setConfig(requestConfig);

			// 判断是否支持设置entity(仅HttpPost、HttpPut、HttpPatch支持)
			if (HttpEntityEnclosingRequestBase.class.isAssignableFrom(request.getClass())) {
				// gip传输
				if (config.byteArrayEntity() != null) {
					((HttpEntityEnclosingRequestBase) request).setEntity(config.byteArrayEntity());
				}
				// 如果是传递过来的xml报文，单独处理
				else if (isStringData(config) && config.stringEntity() != null && !"".equals(config.stringEntity())) {
					((HttpEntityEnclosingRequestBase) request).setEntity(config.stringEntity());
				} else {
					List<NameValuePair> nvps = new ArrayList<NameValuePair>();

					// 检测url中是否存在参数
					config.url(Utils.checkHasParas(config.url(), nvps, config.inenc()));
					// 装填参数
					HttpEntity entity = Utils.map2List(nvps, config.map(), config.inenc());
					
					// 设置参数到请求对象中
					((HttpEntityEnclosingRequestBase) request).setEntity(entity);

					logger.debug("请求地址：" + config.url());
					if (nvps.size() > 0) {
						logger.debug("请求参数：" + nvps.toString());
					}
				}
			} else {
				int idx = config.url().indexOf("?");
				logger.debug("请求地址：" + config.url().substring(0, (idx > 0 ? idx : config.url().length())));
				if (idx > 0) {
					logger.debug("请求参数：" + config.url().substring(idx + 1));
				}
			}
			// 执行请求操作，并拿到结果（同步阻塞）
			resp = (config.context() == null) ? config.client().execute(request) : config.client().execute(request, config.context());

			if (config.isReturnRespHeaders()) {
				// 获取所有response的header信息
				config.headers(resp.getAllHeaders());
			}

			// 获取结果实体
			return resp;

		} catch (IOException e) {
			throw new HttpProcessException(e);
		}
	}

	/**
	 * 转化为字符串
	 * 
	 * @param entity
	 *            实体
	 * @param encoding
	 *            编码
	 * @return
	 * @throws HttpProcessException
	 */
	public static String fmt2String(HttpResponse resp, String encoding) throws HttpProcessException {
		String body = "";
		try {
			if (resp.getEntity() != null) {
				// 按指定编码转换结果实体为String类型
				body = EntityUtils.toString(resp.getEntity(), encoding);
				logger.debug(body);
			}
			EntityUtils.consume(resp.getEntity());
		} catch (IOException e) {
			throw new HttpProcessException(e);
		} finally {
			close(resp);
		}
		return body;
	}

	/**
	 * 转化为流
	 * 
	 * @param entity
	 *            实体
	 * @param out
	 *            输出流
	 * @return
	 * @throws HttpProcessException
	 */
	public static OutputStream fmt2Stream(HttpResponse resp, OutputStream out) throws HttpProcessException {
		try {
			resp.getEntity().writeTo(out);
			EntityUtils.consume(resp.getEntity());
		} catch (IOException e) {
			throw new HttpProcessException(e);
		} finally {
			close(resp);
		}
		return out;
	}

	/**
	 * 根据请求方法名，获取request对象
	 * 
	 * @param url
	 *            资源地址
	 * @param method
	 *            请求方式
	 * @return
	 */
	private static HttpRequestBase getRequest(String url, HttpMethods method) {
		HttpRequestBase request = null;
		switch (method.getCode()) {
		case 0:// HttpGet
			request = new HttpGet(url);
			break;
		case 1:// HttpPost
			request = new HttpPost(url);
			break;
		case 2:// HttpHead
			request = new HttpHead(url);
			break;
		case 3:// HttpPut
			request = new HttpPut(url);
			break;
		case 4:// HttpDelete
			request = new HttpDelete(url);
			break;
		case 5:// HttpTrace
			request = new HttpTrace(url);
			break;
		case 6:// HttpPatch
			request = new HttpPatch(url);
			break;
		case 7:// HttpOptions
			request = new HttpOptions(url);
			break;
		default:
			request = new HttpPost(url);
			break;
		}
		return request;
	}

	/**
	 * 尝试关闭response
	 * 
	 * @param resp
	 *            HttpResponse对象
	 */
	private static void close(HttpResponse resp) {
		try {
			if (resp == null)
				return;
			// 如果CloseableHttpResponse 是resp的父类，则支持关闭
			if (CloseableHttpResponse.class.isAssignableFrom(resp.getClass())) {
				((CloseableHttpResponse) resp).close();
			}
		} catch (IOException e) {
			logger.error("关闭CloseableHttpResponse出错：", e);
		}
	}

	/**
	 * 判断是否是xml或者json报文
	 * 
	 * @param config
	 * @return
	 */
	private static boolean isStringData(HttpConfig config) {
		Header[] headers = config.headers();
		if (headers == null || headers.length == 0) {
			return false;
		}
		for (Header header : headers) {
			String headerName = header.getName();
			String headerValue = header.getValue();
			if (HttpReqHead.CONTENT_TYPE.equalsIgnoreCase(headerName)
					&& (Headers.TEXT_XML.equalsIgnoreCase(headerValue) || Headers.APPLICATION_JSON.equalsIgnoreCase(headerValue))) {
				return true;
			}
		}
		return false;
	}

	public static void main(String[] args) throws Exception {
		ByteArrayOutputStream bout=new ByteArrayOutputStream();
		Header[] headers = HttpHeader.custom().cacheControl(Headers.NO_CACHE).build() ;
		down(HttpConfig.custom().url("http://ohuz0eny8.bkt.clouddn.com/zsl001.txt?v=" + System.currentTimeMillis()).headers(headers).out(bout)) ;
		byte[] data = bout.toByteArray() ;
		System.out.println(new String(data));
	}
}