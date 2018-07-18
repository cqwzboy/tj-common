package com.tj.common.db.rwsplit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;

/**
 * 读写事务管理器
 * @author silongz
 *
 */
public class RWSplitTransactionManager extends DataSourceTransactionManager {

    private static final Logger logger = LoggerFactory.getLogger(RWSplitTransactionManager.class) ;
    /**
	 * 
	 */
	private static final long serialVersionUID = 4908280260438505514L;

	/**
     * 只读事务到读库，读写事务到写库
     * @param transaction
     * @param definition
     */
    @Override
    protected void doBegin(Object transaction, TransactionDefinition definition) {

        //设置数据源
        boolean readOnly = definition.isReadOnly();
        logger.info("事务readOnly=" + readOnly);
        if(readOnly) {
            RWSplitDataSourceHolder.setCurrentDataSource(RWSplitDataSourceType.READ);
        } else {
            RWSplitDataSourceHolder.setCurrentDataSource(RWSplitDataSourceType.WRITE);
        }
        super.doBegin(transaction, definition);
    }

    /**
     * 清理本地线程的数据源
     * @param transaction
     */
    @Override
    protected void doCleanupAfterCompletion(Object transaction) {
        super.doCleanupAfterCompletion(transaction);
        RWSplitDataSourceHolder.clearCurrentDataSource();
    }
}
