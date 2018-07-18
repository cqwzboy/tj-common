package com.tj.common.db.rwsplit;

import org.springframework.core.NamedThreadLocal;

/**
 * 读写分离数据源ThreadLocal持有
 * @author silongz
 *
 */
public final class RWSplitDataSourceHolder {

    private static final ThreadLocal<RWSplitDataSourceType> holder = new NamedThreadLocal<RWSplitDataSourceType>("读写分离数据源ThreadLocal");

    private RWSplitDataSourceHolder() {
    }

    public static void setCurrentDataSource(RWSplitDataSourceType dataSource){
        holder.set(dataSource);
    }

    public static RWSplitDataSourceType getCurrentDataSource(){
        return holder.get();
    }

    public static void clearCurrentDataSource() {
        holder.remove();
    }

}