package com.tj.common.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Gzip压缩解压工具类
 * @author silongz
 *
 */
public class GzipUtils {

	private static final int BUFFER_SIZE = 8 * 1024; 
	
	/**
	 * 压缩字节内容
	 * @param originData
	 * @return
	 * @throws IOException
	 */
	public static byte[] compress(byte[] originData) throws IOException{
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			GZIPOutputStream gzipOut = new GZIPOutputStream(baos);  
			gzipOut.write(originData);
			baos.close();
			gzipOut.finish();
			gzipOut.close();
			return baos.toByteArray() ;
		} catch (Exception e) {
			throw new IOException("调用GzipUtils工具类压缩方法异常", e) ;
		}
	}
	
	/**
	 * 从InputStream读取压缩内容并解压
	 * @param ins
	 * @return
	 * @throws IOException
	 */
	public static byte[] uncompress(InputStream ins) throws IOException{
		byte[] buffer = new byte[BUFFER_SIZE];
		try {
			GZIPInputStream gunzip = new GZIPInputStream(ins);
			int length = 0;
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			while ((length = gunzip.read(buffer)) >= 0) {
				baos.write(buffer, 0, length);
			}
			gunzip.close();
			ins.close();
			baos.close();
			return baos.toByteArray() ;
		} catch (Exception e) {
			throw new IOException("调用GzipUtils工具类解压缩方法异常", e) ;
		}
	}
}
