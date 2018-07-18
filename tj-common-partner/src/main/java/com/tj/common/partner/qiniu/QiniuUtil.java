package com.tj.common.partner.qiniu;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.FetchRet;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.storage.model.FileListing;
import com.qiniu.util.Auth;
import com.tj.common.lang.DateUtils;
import com.tj.common.lang.UuidUtil;

/**
 * 七牛操作工具类
 * 
 * @author silongz
 *
 */
public class QiniuUtil {

	private static Logger logger = LoggerFactory.getLogger(QiniuUtil.class);

	private static UploadManager uploadManager = new UploadManager(new Configuration(Zone.zone0()));

	/**
	 * 上传文件，文件路径会强制加上当前日期
	 * 
	 * @param prefix
	 * @param fileName
	 * @param fileData
	 * @return 文件在七牛服务器的url路径
	 */
	public static String upload(String fileName, byte[] fileData) {
		if (fileData == null || fileData.length <= 0) {
			throw new IllegalArgumentException("文件内容不能为空");
		}
		if (StringUtils.isBlank(fileName)) {
			fileName = UUID.randomUUID().toString();
		}
		String date = DateUtils.getNowTime("yyyy-MM");
		fileName = date.concat("/").concat(fileName);
		String token = QiniuConfig.getAuth().uploadToken(QiniuConfig.getBucket());
		try {
			Response response = uploadManager.put(fileData, fileName, token);
			logger.debug("文件【{0}】上传七牛返回结果【{1}】", fileName, response.bodyString());
			if (!response.isOK()) {
				logger.error("上传文件到七牛出错:" + response.bodyString());
				throw new RuntimeException("上传文件到七牛异常：" + response.bodyString());
			} else {
				return QiniuConfig.getDomain().concat("/").concat(fileName);
			}
		} catch (QiniuException e) {
			throw new RuntimeException("上传文件到七牛异常", e);
		}
	}
	/**
	 * 根据指定参数上传文件
	 * @author luhl
	 * 2017年5月18日
	 * @param accessKey
	 * @param secretKey
	 * @param buket
	 * @param domain
	 * @param fileName
	 * @param fileData
	 * @return
	 */
	public static String upload(String accessKey,String secretKey,String bucket,String domain,String fileName, byte[] fileData) {
		if (fileData == null || fileData.length <= 0) {
			throw new IllegalArgumentException("文件内容不能为空");
		}
		if (StringUtils.isBlank(fileName)) {
			fileName = UUID.randomUUID().toString();
		}
		String date = DateUtils.getNowTime("yyyy-MM");
		fileName = date.concat("/").concat(fileName);

		Auth auth = Auth.create(accessKey, secretKey);
		String token = auth.uploadToken(bucket);
		try {
			Response response = uploadManager.put(fileData, fileName, token);
			logger.debug("文件【{0}】上传七牛返回结果【{1}】", fileName, response.bodyString());
			if (!response.isOK()) {
				logger.error("上传文件到七牛出错:" + response.bodyString());
				throw new RuntimeException("上传文件到七牛异常：" + response.bodyString());
			} else {
				return domain.concat("/").concat(fileName);
			}
		} catch (QiniuException e) {
			throw new RuntimeException("上传文件到七牛异常", e);
		}
	}
	/**
	 * 抓取网络资源
	 * @param fromUrl
	 * @param format
	 * @param key
	 * @return
	 */
	public static String fetchWebFile(String fromUrl,String format,String key){
		return  QiniuConfig.getDomain().concat("/")+fetchFile(fromUrl, format, key);
	}
	private static String fetchFile(String fromUrl,String format,String key){
		if (key == null) {
			key = UuidUtil.getUuid() + "." + format;
			String date = DateUtils.getNowTime("yyyy-MM");
			key = date.concat("/").concat(key);
		}
		Configuration config = new Configuration(Zone.zone0());
		Auth fetchAuth = Auth.create(QiniuConfig.getAccessKey(), QiniuConfig.getSecretKey());
		// 实例化一个BucketManager对象
		BucketManager bucketManager = new BucketManager(fetchAuth,config);
		try {
			// 调用fetch方法抓取文件
			FetchRet  ret = bucketManager.fetch(fromUrl, QiniuConfig.getBucket(), key);
		} catch (QiniuException e) {
			logger.error("fetch file error:",e);
			return null;
		}
		return key;
	}
	/**
	 * 删除文件
	 * 
	 * @param prefixe
	 * @param fileName
	 */
	public static void delete(String prefixe, String fileName) {

		if (StringUtils.isBlank(fileName)) {
			throw new IllegalArgumentException("删除文件时，文件名不能为空");
		}
		String fileKey = "";
		if (StringUtils.isNotBlank(prefixe)) {
			fileKey = prefixe.concat("/").concat(fileName);
		}
		try {
			Configuration config = new Configuration(Zone.zone0());
			Auth auth  = Auth.create(QiniuConfig.getAccessKey(), QiniuConfig.getSecretKey());
			// 实例化一个BucketManager对象
			BucketManager bucketManager = new BucketManager(auth ,config);
			bucketManager.delete(QiniuConfig.getBucket(), fileKey);
		} catch (QiniuException e) {
			throw new RuntimeException("从七牛删除文件异常", e);
		}
	}

	/**
	 * 批量删除文件 注意：该方法如果只指定bucket，会把bucket下的所有文件都删除 建议批量删除时必须指定prefix（相当于是文件夹）
	 * 
	 * @param config
	 * @return
	 */
	public static boolean batchdelete(String prefix) {
		if (StringUtils.isBlank(prefix)) {
			throw new IllegalArgumentException("从七牛批量删除文件必须指定文件前缀（文件夹）");
		}
		try {
			// 创建Batch类型的operations对象
			 BucketManager.BatchOperations operations = new BucketManager.BatchOperations();
			Configuration config = new Configuration(Zone.zone0());
			Auth auth  = Auth.create(QiniuConfig.getAccessKey(), QiniuConfig.getSecretKey());
			// 实例化一个BucketManager对象
			BucketManager bucketManager = new BucketManager(auth ,config);
			List<String> fileKeyList = listFile(prefix);
			if (fileKeyList != null && fileKeyList.size() > 0) {
				String[] fileKeys = new String[fileKeyList.size()];
				fileKeyList.toArray(fileKeys);
				Response response = bucketManager.batch(operations.addDeleteOp(QiniuConfig.getBucket(), fileKeys));
				if (response == null || !response.isOK()) {
					return false;
				} else {
					return true;
				}
			} else {
				logger.warn("本次批量删除，没有满足条件的文件");
				return true;
			}
		} catch (QiniuException e) {
			throw new RuntimeException("从七牛批量删除文件异常", e);
		}

	}

	/**
	 * 查询bucket（传入prefix参数时，会按bucket和prefix参数条件一起查询）下的文件key列表
	 * 
	 * @param config
	 * @return
	 */
	public static List<String> listFile(String prefix) {
		List<String> fileKeyList = new ArrayList<String>();
		try {
			Configuration config = new Configuration(Zone.zone0());
			Auth auth  = Auth.create(QiniuConfig.getAccessKey(), QiniuConfig.getSecretKey());
			// 实例化一个BucketManager对象
			BucketManager bucketManager = new BucketManager(auth ,config);
			FileListing fileList = bucketManager.listFiles(QiniuConfig.getBucket(), prefix, null, 100, null);
			FileInfo[] files = fileList.items;
			for (FileInfo fileInfo : files) {
				fileKeyList.add(fileInfo.key);
			}
		} catch (QiniuException e) {
			throw new RuntimeException("从七牛查询文件列表异常", e);
		}
		return fileKeyList;
	}

	/**
	 * 覆盖上传
	 * 
	 * @param config
	 * @return
	 */
	public static boolean coverUpload(String prefix, String fileName, byte[] fileData) {
		if (StringUtils.isBlank(prefix)) {
			throw new IllegalArgumentException("覆盖上传文件到七牛必须指定文件前缀（文件夹）");
		}
		if (StringUtils.isBlank(fileName)) {
			throw new IllegalArgumentException("覆盖上传方式，文件名不能为空");
		}
		if (fileData == null || fileData.length <= 0) {
			throw new IllegalArgumentException("文件内容不能为空");
		}
		fileName = prefix.concat("/").concat(fileName);
		String token = QiniuConfig.getAuth().uploadToken(QiniuConfig.getBucket(), fileName);
		try {
			Response response = uploadManager.put(fileData, fileName, token);
			logger.debug("文件【{0}】cover方式上传七牛返回结果【{1}】", fileName, response.bodyString());
			if (!response.isOK()) {
				logger.error("cover方式上传文件到七牛出错:" + response.bodyString());
				return false;
			} else {
				return true;
			}
		} catch (QiniuException e) {
			throw new RuntimeException("cover方式上传文件到七牛出现异常", e);
		}
	}

	/**
	 * 删除指定bucket下，上传时间早于当前时间monthAmount（几个月之前）的文件
	 * @param bucket
	 * @param monthAmount
	 */
	public static long deleteFile(String bucket, int monthAmount) {
		long fileCount = 0;
		try {
			while (true) {
				//每个批次取1000个文件
				Configuration config = new Configuration(Zone.zone0());
				Auth auth  = Auth.create(QiniuConfig.getAccessKey(), QiniuConfig.getSecretKey());
				// 实例化一个BucketManager对象
				BucketManager bucketManager = new BucketManager(auth ,config);
				FileListing fileList = bucketManager.listFiles(bucket, null, null, 1000, null);
				FileInfo[] files = fileList.items;
				if(files != null && files.length > 0){
					for (FileInfo fileInfo : files) {
						// 把七牛的上传时间转成毫秒单位
						long putTime = fileInfo.putTime;
						long putTimeUnix = putTime / 10000;
						String fileKey = fileInfo.key;
						Date putDate = new Date(putTimeUnix);
						Date monthAgo = DateUtils.beforeNowByMounth(monthAmount);
						if (putDate.before(monthAgo)) {
							bucketManager.delete(bucket, fileKey);
						}
						fileCount ++ ;
					}
				}
				if (fileList.isEOF()) {
					break;
				}
			}
			return fileCount ;
		} catch (QiniuException e) {
			throw new RuntimeException("从七牛查询文件列表异常", e);
		}
	}

}
