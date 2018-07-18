package com.tj.common.db.rwsplit;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 读写分离动态数据源，扩展AbstractRoutingDataSource，并实现determineCurrentLookupKey方法来实现数据源动态切换
 * @author silongz
 *
 */
public class RWSplitDataSource extends AbstractRoutingDataSource {

    private static final Logger logger = LoggerFactory.getLogger(RWSplitDataSource.class) ;

	/**
	 * 读，写数据源
	 */
    private Object readAndWriteDataSource; 

	/**
     * 读数据源
     */
    private Object readDataSource;

    @Override
    public void afterPropertiesSet() {
        if (this.readAndWriteDataSource == null) {
            throw new IllegalArgumentException("Property 'readAndWriteDataSource' is required");
        }
        setDefaultTargetDataSource(readAndWriteDataSource);
        Map<Object, Object> targetDataSources = new HashMap<Object, Object>();
        targetDataSources.put(RWSplitDataSourceType.WRITE.name(), readAndWriteDataSource);
        if(readDataSource != null) {
            targetDataSources.put(RWSplitDataSourceType.READ.name(), readDataSource);
        }
        setTargetDataSources(targetDataSources);
        super.afterPropertiesSet();
        logger.info("动态数据源初始化完成");
    }

    @Override
    protected Object determineCurrentLookupKey() {

        RWSplitDataSourceType dynamicDataSourceGlobal = RWSplitDataSourceHolder
                .getCurrentDataSource();
        logger.info("获取动态数据源 dynamicDataSourceGlobal=" + dynamicDataSourceGlobal);
        if(dynamicDataSourceGlobal == null
                || dynamicDataSourceGlobal == RWSplitDataSourceType.WRITE) {
            return RWSplitDataSourceType.WRITE.name();
        }
        return RWSplitDataSourceType.READ.name();
    }
    
    public Object getReadAndWriteDataSource() {
		return readAndWriteDataSource;
	}

	public void setReadAndWriteDataSource(Object readAndWriteDataSource) {
		this.readAndWriteDataSource = readAndWriteDataSource;
	}

	public Object getReadDataSource() {
		return readDataSource;
	}

	public void setReadDataSource(Object readDataSource) {
		this.readDataSource = readDataSource;
	}

}