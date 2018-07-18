package com.tj.common.redis;

import redis.clients.jedis.Transaction;

public interface TransactionCallBack {
    void doTransaction(Transaction transaction);
}
