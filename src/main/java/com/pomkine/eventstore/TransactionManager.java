package com.pomkine.eventstore;

import com.google.common.collect.Maps;
import com.pomkine.domain.common.AggregateId;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javax.inject.Singleton;

@Singleton
public class TransactionManager {

    private ConcurrentMap<AggregateId, ReentrantLock> idToLock;

    public TransactionManager() {
        this.idToLock = Maps.newConcurrentMap();
    }

    public void doInTx(AggregateId aggregateId, Runnable writeAction) {
        Lock aggregateLock = getOrCreateLock(aggregateId);
        try {
            aggregateLock.lock();
            writeAction.run();
        } finally {
            aggregateLock.unlock();
        }
    }

    private Lock getOrCreateLock(AggregateId aggregateId) {
        Lock aggregateLock = idToLock.get(aggregateId);
        if (aggregateLock == null) {
            idToLock.putIfAbsent(aggregateId, new ReentrantLock());
            aggregateLock = idToLock.get(aggregateId);
        }
        return aggregateLock;
    }
}
