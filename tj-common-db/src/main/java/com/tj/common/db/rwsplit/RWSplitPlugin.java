package com.tj.common.db.rwsplit;

import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.keygen.SelectKeyGenerator;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * ibatis插件拦截器，select使用读库，其余使用写库
 * @author silongz
 *
 */
@Intercepts({
@Signature(type = Executor.class, method = "update", args = {
        MappedStatement.class, Object.class }),
@Signature(type = Executor.class, method = "query", args = {
        MappedStatement.class, Object.class, RowBounds.class,
        ResultHandler.class }) })
public class RWSplitPlugin implements Interceptor {

    protected static final Logger logger = LoggerFactory.getLogger(RWSplitPlugin.class);

    private static final String REGEX = ".*insert\\u0020.*|.*delete\\u0020.*|.*update\\u0020.*";

    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        boolean synchronizationActive = TransactionSynchronizationManager.isSynchronizationActive();
        if(!synchronizationActive) {
            Object[] objects = invocation.getArgs();
            MappedStatement ms = (MappedStatement) objects[0];
            RWSplitDataSourceType dynamicDataSourceGlobal = null;
            //读方法
            if(ms.getSqlCommandType().equals(SqlCommandType.SELECT)) {
                //!selectKey 为自增id查询主键(SELECT LAST_INSERT_ID() )方法，使用主库
                if(ms.getId().contains(SelectKeyGenerator.SELECT_KEY_SUFFIX)) {
                    dynamicDataSourceGlobal = RWSplitDataSourceType.WRITE;
                } else {
                    BoundSql boundSql = ms.getSqlSource().getBoundSql(objects[1]);
                    String sql = boundSql.getSql().toLowerCase(Locale.CHINA).replaceAll("[\\t\\n\\r]", " ");
                    if(sql.matches(REGEX)) {
                        dynamicDataSourceGlobal = RWSplitDataSourceType.WRITE;
                    } else {
                        dynamicDataSourceGlobal = RWSplitDataSourceType.READ;
                    }
                }
            }else{
                dynamicDataSourceGlobal = RWSplitDataSourceType.WRITE;
            }
            logger.info("方法[{}]使用[{}] 数据源, SqlCommandType [{}]..", ms.getId(), dynamicDataSourceGlobal.name(),
                    ms.getSqlCommandType().name());
            RWSplitDataSourceHolder.setCurrentDataSource(dynamicDataSourceGlobal);
        }

        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof Executor) {
            return Plugin.wrap(target, this);
        } else {
            return target;
        }
    }

    @Override
    public void setProperties(Properties properties) {
        //
    }
}