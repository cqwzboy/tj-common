package com.tj.common.db.backup;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.util.IOUtils;
import com.tj.common.lang.CommandUtils;

/**
 * mysqldump工具类，该类的执行是通过java获取运行时环境，通过java调用当前机器的mysqldump命令来执行的。
 * 所以必须保证执行该类的机器上安装有mysqldump程序，并且目标库的mysql用户已经被授权给当前机器执行该命令
 * 
 * @author silongz
 *
 */
public class MysqlDumpUtils {

	private static Logger logger = LoggerFactory.getLogger(MysqlDumpUtils.class);

	// dump文件名后缀是系统当前时间，时分秒没有用冒号分隔，是因为文件名不能包含冒号
	private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd-HHmmss");
	
	private final static String INSERT_BEGIN = "INSERT" ;
	
	/**
	 * mysqldump整个mysql中的所有库
	 * 
	 * @param dbUrl
	 * @param userName
	 * @param password
	 * @param mysqlSockPath
	 * @param backUpDir
	 * @param backupSqlFileName
	 * @return
	 * @throws IOException
	 */
	public static boolean dumpAllDatabases(String dbUrl, int port, String userName, String password, String mysqlSockPath,
			String backUpDir, String backupSqlFileName) throws IOException {
		String [] dumpCmdAndFilePath = buildMysqlDumpCommand(dbUrl, port, userName, password, mysqlSockPath, backUpDir, backupSqlFileName, null, null,
				null);
		return CommandUtils.excute(dumpCmdAndFilePath[0]);
	}

	/**
	 * mysqldump指定数据库
	 * 
	 * @param dbUrl
	 * @param userName
	 * @param password
	 * @param mysqlSockPath
	 * @param backUpDir
	 * @param backupSqlFileName
	 * @param dbName
	 * @return
	 * @throws IOException
	 */
	public static boolean dumpDatabase(String dbUrl, int port, String userName, String password, String mysqlSockPath, String backUpDir,
			String backupSqlFileName, String dbName) throws IOException {
		String [] dumpCmdAndFilePath = buildMysqlDumpCommand(dbUrl, port, userName, password, mysqlSockPath, backUpDir, backupSqlFileName, dbName, null,
				null);
		return CommandUtils.excute(dumpCmdAndFilePath[0]);
	}

	/**
	 * mysqldump指定数据库的指定表，多个表名用英文空格隔开
	 * 
	 * @param dbUrl
	 * @param userName
	 * @param password
	 * @param mysqlSockPath
	 * @param backUpDir
	 * @param backupSqlFileName
	 * @param dbName
	 * @param tables
	 *            -- 多个表名用英文空格隔开
	 * @return
	 * @throws IOException
	 */
	public static boolean dumpTables(String dbUrl, int port, String userName, String password, String mysqlSockPath, String backUpDir,
			String backupSqlFileName, String dbName, String tables) throws IOException {
		String [] dumpCmdAndFilePath = buildMysqlDumpCommand(dbUrl, port, userName, password, mysqlSockPath, backUpDir, backupSqlFileName, dbName,
				tables, null);
		return CommandUtils.excute(dumpCmdAndFilePath[0]);
	}

	/**
	 * mysqldump指定数据库，指定数据表，指定条件（类似col1 > 1 and col2 <= 'abc' and date_col >
	 * '2016-11-20'） 注意：字符串条件必须用单引号包裹
	 * 
	 * @param host
	 * @param userName
	 * @param password
	 * @param mysqlSocketPath
	 * @param backUpDir
	 * @param backupSqlFileName
	 * @param dbName
	 * @param table
	 * @param conditon
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static boolean dumpTableWithConditon(String host, int port, String userName, String password, String mysqlSocketPath,
			String backUpDir, String backupSqlFileName, String dbName, String table, String conditon) throws IOException {
		String [] dumpCmdAndFilePath = buildMysqlDumpCommand(host, port, userName, password, mysqlSocketPath, backUpDir, backupSqlFileName, dbName,
				table, conditon);
		return CommandUtils.excute(dumpCmdAndFilePath[0]);
	}
	
	/**
	 * 按条件导出表数据，并返回对应的insert记录语句
	 * @param host
	 * @param port
	 * @param userName
	 * @param password
	 * @param mysqlSocketPath
	 * @param backUpDir
	 * @param backupSqlFileName
	 * @param dbName
	 * @param table
	 * @param conditon
	 * @return
	 * @throws IOException
	 */
	public static List<String> dumpTableAndReturnInsertSql(String host, int port, String userName, String password, String mysqlSocketPath,
			String backUpDir, String backupSqlFileName, String dbName, String table, String conditon) throws IOException {
		String [] dumpCmdAndFilePath = buildMysqlDumpCommand(host, port, userName, password, mysqlSocketPath, backUpDir, backupSqlFileName, dbName,
				table, conditon);
		boolean result = CommandUtils.excute(dumpCmdAndFilePath[0]);
		if(!result){
			throw new IOException("dump文件失败") ;
		}
		return getInsertSqlFromDumpFile(dumpCmdAndFilePath[1]) ;
	}
	
	/**
	 * 构建mysqldump命令语句
	 * 
	 * @param host
	 * @param userName
	 * @param password
	 * @param mysqlSocketPath
	 * @param backUpDir
	 * @param backupSqlFileName
	 * @param dbName
	 * @param table
	 * @param conditon
	 * @return
	 */
	private static String[] buildMysqlDumpCommand(String host, int port, String userName, String password, String mysqlSocketPath,
			String backUpDir, String backupSqlFileName, String dbName, String table, String conditon) {
		String [] dumpCmdAndFilePath = new String[2] ;
		if (StringUtils.isBlank(backUpDir)) {
			throw new IllegalArgumentException("mysqldump备份文件路径为空");
		}
		if (StringUtils.isBlank(backupSqlFileName)) {
			throw new IllegalArgumentException("mysqldump备份文件名称为空");
		}
		StringBuilder sb = new StringBuilder();
		sb.append("mysqldump");
		// 不导出表结构
		sb.append(" -t ");
		sb.append(" -h ");
		sb.append(host);
		sb.append(" -P ");
		sb.append(port);
		sb.append(" -u");
		sb.append(userName);
		sb.append(" -p");
		sb.append(password);
		if (StringUtils.isNotBlank(mysqlSocketPath)) {
			sb.append(" -S ");
			sb.append(mysqlSocketPath);
		}
		// 该选项在导出大表时很有用，它强制 mysqldump 从服务器查询取得记录直接输出而不是取得所有记录后将它们缓存到内存中
		sb.append(" -q ");
		// 所有记录是否是一条insert语句
		sb.append(" --extended-insert=false ");
		if (StringUtils.isNotBlank(dbName)) {
			sb.append(" --databases ");
			sb.append(dbName);
		}
		sb.append(" ");
		if (StringUtils.isNotBlank(table)) {
			sb.append(" --tables ");
			sb.append(table);
		}
		if (StringUtils.isNotBlank(conditon)) {
			sb.append(" -w ");
			sb.append("\"");
			sb.append(conditon);
			sb.append("\"");
		}
		// by fuqq on 20181126 导出所有schema和table
		if(StringUtils.isAnyBlank(dbName) && StringUtils.isBlank(table) && StringUtils.isBlank(conditon)){
			sb.append(" --all-databases ");
		}
		sb.append(" --result-file=");
		String filePath = getBackupFilePath(backUpDir, backupSqlFileName);
		sb.append(filePath);
		sb.append(" --log-error=") ;
		sb.append(backUpDir) ;
		sb.append(File.separator);
		sb.append("dump_err.log");
		
		dumpCmdAndFilePath[0] = sb.toString() ;
		dumpCmdAndFilePath[1] = filePath ;
		return dumpCmdAndFilePath ;
	}

	/**
	 * 获取备份文件的全路径
	 * @param backUpDir
	 * @param backupSqlFileName
	 * @return
	 */
	private static String getBackupFilePath(String backUpDir, String backupSqlFileName) {
		StringBuilder sb = new StringBuilder();
		File file = new File(backUpDir);
		if (!file.exists()) {
			file.mkdirs();
		}
		sb.append(backUpDir);
		if (!backUpDir.endsWith(File.separator)) {
			sb.append(File.separator);
		}
		sb.append(backupSqlFileName);
		sb.append("-");
		sb.append(df.format(new Date()));
		sb.append(".dump");
		return sb.toString();
	}

	/**
	 * 按行读取dump文件，并截取dump文件中的insert语句行
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	private static List<String> getInsertSqlFromDumpFile(String filePath) throws IOException {
		logger.info("本次dump文件全路径="+filePath);
		int bufferSize = 1024 * 1000 ;// 一次读取的字节长度
		FileChannel fChannel = null;
		RandomAccessFile file = null;
		List<String> lineList = new ArrayList<String>();// 存储读取的每行数据
		try {
			file = new RandomAccessFile(filePath, "r");
			fChannel = file.getChannel();
			ByteBuffer readBuffer = ByteBuffer.allocate(bufferSize);
			byte[] lineByte = new byte[0];
			String encode = "UTF-8";
			// temp：由于是按固定字节读取，在一次读取中，第一行和最后一行经常是不完整的行，因此定义此变量来存储上次的最后一行和这次的第一行的内容，
			// 并将之连接成完成的一行，否则会出现汉字被拆分成2个字节，并被提前转换成字符串而乱码的问题
			byte[] temp = new byte[0];
			while (fChannel.read(readBuffer) != -1) {// fcin.read(rBuffer)：从文件管道读取内容到缓冲区(rBuffer)
				int readSize = readBuffer.position();// 读取结束后的位置，相当于读取的长度
				byte[] bs = new byte[readSize];// 用来存放读取的内容的数组
				readBuffer.rewind();// 将position设回0,所以你可以重读Buffer中的所有数据,此处如果不设置,无法使用下面的get方法
				readBuffer.get(bs);// 相当于rBuffer.get(bs,0,bs.length())：从position初始位置开始相对读,读bs.length个byte,并写入bs[0]到bs[bs.length-1]的区域
				readBuffer.clear();

				int startNum = 0;
				int lineChar = 10;// 换行符
				int enterChar = 13;// 回车符
				boolean hasLF = false;// 是否有换行符
				for (int i = 0; i < readSize; i++) {
					if (bs[i] == lineChar) {
						hasLF = true;
						int tempNum = temp.length;
						int lineNum = i - startNum;
						lineByte = new byte[tempNum + lineNum];// 数组大小已经去掉换行符

						System.arraycopy(temp, 0, lineByte, 0, tempNum);// 填充了lineByte[0]~lineByte[tempNum-1]
						temp = new byte[0];
						System.arraycopy(bs, startNum, lineByte, tempNum, lineNum);// 填充lineByte[tempNum]~lineByte[tempNum+lineNum-1]

						String line = new String(lineByte, 0, lineByte.length, encode);// 一行完整的字符串(过滤了换行和回车)
						
						if(line.startsWith(INSERT_BEGIN)){
							lineList.add(line);
						}
						
						// 过滤回车符和换行符
						if (i + 1 < readSize && bs[i + 1] == enterChar) {
							startNum = i + 2;
						} else {
							startNum = i + 1;
						}

					}
				}
				if (hasLF) {
					temp = new byte[bs.length - startNum];
					System.arraycopy(bs, startNum, temp, 0, temp.length);
				} else {// 兼容单次读取的内容不足一行的情况
					byte[] toTemp = new byte[temp.length + bs.length];
					System.arraycopy(temp, 0, toTemp, 0, temp.length);
					System.arraycopy(bs, 0, toTemp, temp.length, bs.length);
					temp = toTemp;
				}
			}
			if (temp != null && temp.length > 0) {// 兼容文件最后一行没有换行的情况
				String line = new String(temp, 0, temp.length, encode);
				if(line.startsWith(INSERT_BEGIN)){
					lineList.add(line);
				}
			}
		} catch (Exception e) {
			throw new IOException("读取dump文件内容异常", e);
		} finally {
			IOUtils.close(file);
			IOUtils.close(fChannel);
		}
		return lineList;
	}

	public static void main(String[] args) throws IOException {
		System.out.println(dumpAllDatabases("localhost", 3306, "root", "root", null, "D://wahaha", "backUpFile.dump"));
	}

}
