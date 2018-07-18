package com.tj.common.db.log;

import com.alibaba.druid.filter.logging.Slf4jLogFilter;
import com.alibaba.druid.proxy.jdbc.CallableStatementProxy;
import com.alibaba.druid.proxy.jdbc.JdbcParameter;
import com.alibaba.druid.proxy.jdbc.PreparedStatementProxy;
import com.alibaba.druid.proxy.jdbc.StatementProxy;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlSchemaStatVisitor;
import com.alibaba.druid.util.JdbcConstants;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 记录业务sql日志类
 */
public class TjSlf4jLogFilter extends Slf4jLogFilter {

    private static final String INCLUDE_ALL = "*" ;

    private static final String PROP_TABLES = "sql_tabales" ;

    private static final String PROP_EXCLUDE_SELECT = "exclude_select" ;

    private static final String PROP_SPLIT = "," ;

    private Logger statementLogger  = LoggerFactory.getLogger(statementLoggerName);

    private static final List<String> includeTables = new ArrayList<>() ;

    private ThreadLocal<Boolean> needRecord = new ThreadLocal<>() ;

    private ThreadLocal<List<String>> sqlLog = new ThreadLocal<>() ;

    private ThreadLocal<Throwable> erroLog = new ThreadLocal<>() ;

    private static boolean excludeSelectFlag = true ;

    static {
        InputStream ins = Thread.currentThread().getContextClassLoader().getResourceAsStream("sqllog.properties");
        if(ins != null){
            Properties prop = new Properties();
            try {
                prop.load(ins);
                String sqlTabales = prop.getProperty(PROP_TABLES) ;
                String excludeSelect = prop.getProperty(PROP_EXCLUDE_SELECT) ;
                if(StringUtils.isNotBlank(excludeSelect) && "false".equals(excludeSelect)){
                    excludeSelectFlag = false ;
                }
                if(StringUtils.isNotBlank(sqlTabales)){
                    String [] tables = sqlTabales.split(",") ;
                    for (String table : tables) {
                        //当包含*号时，只保留*号
                        if(INCLUDE_ALL.equals(table)){
                            includeTables.clear();
                            includeTables.add(INCLUDE_ALL) ;
                            break ;
                        }
                        includeTables.add(table) ;
                    }
                }
            } catch (IOException e) {
                includeTables.add(INCLUDE_ALL) ;
            }
        }
    }

    @Override
    public String getStatementLoggerName() {
        return statementLoggerName;
    }

    @Override
    public void setStatementLoggerName(String statementLoggerName) {
        this.statementLoggerName = statementLoggerName;
        statementLogger = LoggerFactory.getLogger(statementLoggerName);
    }

    @Override
    public boolean isStatementLogEnabled() {
        return true;
    }

    @Override
    public boolean isStatementLogErrorEnabled() {
        return true;
    }

    @Override
    protected void statementExecuteBefore(StatementProxy statement, String sql) {
        statement.setLastExecuteStartNano();
        Boolean needRecordFlag = needRecord.get() ;
        if(needRecordFlag == null){
            needRecordFlag = isIncluleTable(sql) ;
            needRecord.set(needRecordFlag);
        }
        if(needRecordFlag){
            logExecutableSql(statement,sql);
        }
    }

    @Override
    protected void statementExecuteUpdateBefore(StatementProxy statement, String sql) {
        statement.setLastExecuteStartNano();
        Boolean needRecordFlag = needRecord.get() ;
        if(needRecordFlag == null){
            needRecordFlag = isIncluleTable(sql) ;
            needRecord.set(needRecordFlag);
        }
        if(needRecordFlag){
            logExecutableSql(statement,sql);
        }
    }

    @Override
    protected void statementLogError(String message, Throwable error) {
        if(StringUtils.isBlank(message)){
            return ;
        }
        List<String> outputLogs = sqlLog.get() ;
        if(outputLogs == null){
            outputLogs = new ArrayList<>() ;
            sqlLog.set(outputLogs);
        }
        Boolean needRecordFlag = needRecord.get() ;
        if(needRecordFlag){
            erroLog.set(error);
        }
    }

    @Override
    protected void statementLog(String message) {
        try {
            Boolean needRecordFlag = needRecord.get() ;
            if(needRecordFlag != null && needRecordFlag){
                List<String> outputLogs = sqlLog.get() ;
                Throwable error = erroLog.get() ;
                if(error != null){
                    statementLogger.error("sql执行异常",error);
                    for (String outputLog : outputLogs) {
                        statementLogger.error(outputLog);
                    }
                    statementLogger.error("============================分隔符============================");
                }else{
                    for (String outputLog : outputLogs) {
                        statementLogger.debug(outputLog);
                    }
                    statementLogger.debug("============================分隔符============================");
                }
            }
        }catch (Throwable e) {
            statementLogger.error("记录业务sql时报错",e);
        }finally {
            needRecord.remove();
            sqlLog.remove();
            erroLog.remove();
        }
    }

    /**
     * 判断sql是否需要记录
     * @param sql
     * @return
     */
    private boolean isIncluleTable(String sql) {
        if(StringUtils.isBlank(sql)){
            return false ;
        }
        //select不记录
        if(excludeSelectFlag && sql.trim().toLowerCase().startsWith("select")){
            return false ;
        }
        String dbType = JdbcConstants.MYSQL;
        List<SQLStatement> stmtList = SQLUtils.parseStatements(sql, dbType);
        //解析出的独立语句的个数
        for (int i = 0; i < stmtList.size(); i++) {
            SQLStatement stmt = stmtList.get(i);
            MySqlSchemaStatVisitor visitor = new MySqlSchemaStatVisitor();
            stmt.accept(visitor);
            String currentTabale = visitor.getCurrentTable() ;
            for (String incluedTable : includeTables) {
                if(INCLUDE_ALL.equals(incluedTable) || incluedTable.equalsIgnoreCase(currentTabale)){
                    return true ;
                }
            }

        }
        return false ;
    }

    /**
     * 记录完整sql语句
     * @param statement
     * @param sql
     */
    private void logExecutableSql(StatementProxy statement, String sql) {
        List<String> outputLogs = sqlLog.get() ;
        if(outputLogs == null){
            outputLogs = new ArrayList<>() ;
            sqlLog.set(outputLogs);
        }
        int parametersSize = statement.getParametersSize();
        if (parametersSize == 0) {
            String msg = "{conn-" + statement.getConnectionProxy().getId() + ", " + stmtId(statement) + "} " +
                    "logged" + ". \n" + sql ;
            outputLogs.add(msg) ;
            return;
        }

        List<Object> parameters = new ArrayList<Object>(parametersSize);
        for (int i = 0; i < parametersSize; ++i) {
            JdbcParameter jdbcParam = statement.getParameter(i);
            parameters.add(jdbcParam.getValue());
        }

        String dbType = statement.getConnectionProxy().getDirectDataSource().getDbType();
        String formattedSql = SQLUtils.format(sql, dbType, parameters);
        String msg = "{conn-" + statement.getConnectionProxy().getId() + ", " + stmtId(statement) + "} logged. \n"
                + formattedSql ;
        outputLogs.add(msg) ;
    }

    /**
     * 获取statment id
     * @param statement
     * @return
     */
    private String stmtId(StatementProxy statement) {
        StringBuffer buf = new StringBuffer();
        if (statement instanceof CallableStatementProxy) {
            buf.append("cstmt-");
        } else if (statement instanceof PreparedStatementProxy) {
            buf.append("pstmt-");
        } else {
            buf.append("stmt-");
        }
        buf.append(statement.getId());

        return buf.toString();
    }

}
