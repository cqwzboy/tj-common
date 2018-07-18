package com.tj.common.http;

import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

/**
 * httpclient builder
 * @author silongz
 *
 */
public class  TjHttpClientBuilder extends HttpClientBuilder{
	
	private boolean isSetPool=false;//记录是否设置了连接池
	private boolean isNewSSL=false;//记录是否设置了更新了ssl
	
	//用于配置ssl
	private SSLs ssls = SSLs.getInstance();
	
	private TjHttpClientBuilder(){}
	public static TjHttpClientBuilder custom(){
		return new TjHttpClientBuilder();
	}

	/**
	 * 设置超时时间
	 * 
	 * @param timeout		超市时间，单位-毫秒
	 * @return
	 */
	public TjHttpClientBuilder timeout(int timeout){
		 // 配置请求的超时设置
        RequestConfig config = RequestConfig.custom()
                .setConnectionRequestTimeout(timeout)
                .setConnectTimeout(timeout)
                .setSocketTimeout(timeout)
                .build();
		return (TjHttpClientBuilder) this.setDefaultRequestConfig(config);
	}
	
	/**
	 * 设置ssl安全链接
	 * 
	 * @return
	 * @throws HttpProcessException
	 */
	public TjHttpClientBuilder ssl() throws HttpProcessException {
		if(isSetPool){//如果已经设置过线程池，那肯定也就是https链接了
			if(isNewSSL){
				throw new HttpProcessException("请先设置ssl，后设置pool");
			}
			return this;
		}
		Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder
				.<ConnectionSocketFactory> create()
				.register("http", PlainConnectionSocketFactory.INSTANCE)
				.register("https", ssls.getSSLCONNSF()).build();
		//设置连接池大小
		PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
		return (TjHttpClientBuilder) this.setConnectionManager(connManager);
	}
	

	/**
	 * 设置自定义sslcontext
	 * 
	 * @param keyStorePath		密钥库路径
	 * @return
	 * @throws HttpProcessException
	 */
	public TjHttpClientBuilder ssl(String keyStorePath) throws HttpProcessException{
		return ssl(keyStorePath,"nopassword");
	}
	/**
	 * 设置自定义sslcontext
	 * 
	 * @param keyStorePath		密钥库路径
	 * @param keyStorepass		密钥库密码
	 * @return
	 * @throws HttpProcessException
	 */
	public TjHttpClientBuilder ssl(String keyStorePath, String keyStorepass) throws HttpProcessException{
		this.ssls = SSLs.custom().customSSL(keyStorePath, keyStorepass);
		this.isNewSSL=true;
		return ssl();
	}
	
	
	/**
	 * 设置连接池（默认开启https）
	 * 
	 * @param maxTotal					最大连接数
	 * @param defaultMaxPerRoute	每个路由默认连接数
	 * @return
	 * @throws HttpProcessException
	 */
	public TjHttpClientBuilder pool(int maxTotal, int defaultMaxPerRoute) throws HttpProcessException{
		Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder
				.<ConnectionSocketFactory> create()
				.register("http", PlainConnectionSocketFactory.INSTANCE)
				.register("https", ssls.getSSLCONNSF()).build();
		//设置连接池大小
		PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
		connManager.setMaxTotal(maxTotal);
		connManager.setDefaultMaxPerRoute(defaultMaxPerRoute);
		isSetPool=true;
		return (TjHttpClientBuilder) this.setConnectionManager(connManager);
	}
	
	/**
	 * 设置代理
	 * 
	 * @param hostOrIP		代理host或者ip
	 * @param port			代理端口
	 * @return
	 */
	public TjHttpClientBuilder proxy(String hostOrIP, int port){
		// 依次是代理地址，代理端口号，协议类型  
		HttpHost proxy = new HttpHost(hostOrIP, port, "http");  
		DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
		return (TjHttpClientBuilder) this.setRoutePlanner(routePlanner);
	}
}