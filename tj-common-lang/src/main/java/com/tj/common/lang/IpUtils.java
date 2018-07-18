package com.tj.common.lang;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;

public class IpUtils {

	private static String os = System.getProperty("os.name").toLowerCase();
	
	private static String OS_WINDOWS = "windows" ;
	
	private static String OS_LINUX = "linux" ;
	
	private static String LOCAL = "localhost" ;
	
	private static String LOCAL_127 = "127.0.0.1" ;
	
	/**
	 * 获取本机ip
	 * 
	 * @return
	 */
	public static String getLocalIp() {
		if(os.startsWith(OS_WINDOWS)){
			return getDefualtIp() ;
		}else if(os.startsWith(OS_LINUX)){
			return getLinuxLocalIp() ;
		}else{
			throw new UnsupportedOperationException("不支持windows和linux以外的操作系统") ;
		}
	}

	/**
	 * 获取windows机器ip
	 * @return
	 */
	private static String getDefualtIp() {
		InetAddress address;
		try {
			address = InetAddress.getLocalHost();
			return address.getHostAddress();
		} catch (UnknownHostException e) {
			throw new RuntimeException("获取windows机器ip异常", e);
		}
	}

	/**
	 * 获取linux机器ip
	 * linux获取默认ip时，是读取hosts文件中配置的本机ip，默认是localhost和127.0.0.1
	 * @return
	 */
	private static String getLinuxLocalIp() {
		String defaultIp = getDefualtIp() ;
		if(!defaultIp.equalsIgnoreCase(LOCAL) && !defaultIp.equalsIgnoreCase(LOCAL_127)){
			return defaultIp ;
		}
		try {
			Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
			InetAddress ip = null;
			while (allNetInterfaces.hasMoreElements()) {
				NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
				Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
				while (addresses.hasMoreElements()) {
					ip = (InetAddress) addresses.nextElement();
					if (ip != null && ip instanceof Inet4Address) {
						String linuxIp = ip.getHostAddress();
						if(!linuxIp.equalsIgnoreCase(LOCAL) && !linuxIp.equalsIgnoreCase(LOCAL_127)){
							return linuxIp ;
						}
					}
				}
			}
			throw new RuntimeException("当前机器ipv4地址为空");
		} catch (Exception e) {
			throw new RuntimeException("获取linux机器ip异常", e);
		}
	}

	/**
     * 把IP按点号分4段，每段一整型就一个字节来表示，通过左移位来实现。
     * 第一段放到最高的8位，需要左移24位，依此类推即可
     *
     * @param ipStr ip地址
     * @return 整形
     */
    public static Integer ip2Num(String ipStr) {
        if (ipStr == null || "".equals(ipStr)) {
            return -1;
        }

        if (ipStr.contains(":")) {
            //ipv6的地址，不解析，返回127.0.0.1
            ipStr = LOCAL_127 ;
        }

        String[] ips = ipStr.split("\\.");

        return (Integer.parseInt(ips[0]) << 24) + (Integer.parseInt(ips[1]) << 16) + (Integer.parseInt(ips[2]) << 8) + Integer.parseInt(ips[3]);
    }

    /**
     * 把整数分为4个字节，通过右移位得到IP地址中4个点分段的值
     *
     * @param ipNum ip int value
     * @return ip str
     */
    public static String num2Ip(int ipNum) {
        return ((ipNum >> 24) & 0xFF) + "." + ((ipNum >> 16) & 0xFF) + "." + ((ipNum >> 8) & 0xFF) + "." + (ipNum & 0xFF);
    }
}
