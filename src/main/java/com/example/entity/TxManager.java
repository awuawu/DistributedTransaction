package com.example.entity;

import com.example.util.LockConditionUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TxManager {
    public static Map<String, LockConditionUtil> tm = new ConcurrentHashMap<>(1000);
    public static Map<Thread, String> txGroup = new ConcurrentHashMap<>(1000);
}
