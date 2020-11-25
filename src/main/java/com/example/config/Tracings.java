package com.example.config;

import com.example.entity.TxManager;

//feign调用放置groupId
public class Tracings {
    /**
     * 传输Tracing信息
     *
     * @param tracingSetter Tracing信息设置器
     */
    public static void transmit(TracingSetter tracingSetter) {
//        String groupId = (String) TxManager.txGroup.get(Thread.currentThread());
        String groupId = TxManager.deliverGroup.get();
        if (groupId != null) {
            tracingSetter.set("groupId", groupId);
        }
    }

    /**
     * Tracing信息设置器
     */
    public interface TracingSetter {
        /**
         * 设置tracing属性
         *
         * @param key   key
         * @param value value
         */
        void set(String key, String value);
    }
}
