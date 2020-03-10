package com.example.aspect;

import com.example.annotation.MyTransactional;
import com.example.entity.TxManager;
import com.example.util.LockConditionUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.sql.Connection;

@Aspect
@Component
public class MyDataSourceAspect {
    @Around("execution(* javax.sql.DataSource.getConnection(..))")
    public Connection around(ProceedingJoinPoint joinPoint){
        Connection connection = null;
        try {
            connection = (Connection) joinPoint.proceed();
            connection.setAutoCommit(false);
            connection = new MyConnection(connection, TxManager.tm.get(TxManager.txGroup.get(Thread.currentThread())));
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return connection;
    }
}
