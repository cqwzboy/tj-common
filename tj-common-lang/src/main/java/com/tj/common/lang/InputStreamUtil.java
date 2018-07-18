package com.tj.common.lang;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * inputstream 工具类 ：inputstream 与 byte[] 相互转换
 * @author luhl
 * 2016年9月9日
 */
public class InputStreamUtil {
	public static final byte[] input2byte(InputStream inStream) throws IOException {
		ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
		byte[] buff = new byte[100];
		int rc = 0;
		while ((rc = inStream.read(buff, 0, 100)) > 0) {
			swapStream.write(buff, 0, rc);
		}
		byte[] in2b = swapStream.toByteArray();
		return in2b;
	}

	public static final InputStream byte2input(byte[] bt) {
		return new ByteArrayInputStream(bt);
	}
}
