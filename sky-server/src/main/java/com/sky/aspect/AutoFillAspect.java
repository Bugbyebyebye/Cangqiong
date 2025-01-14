package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

/**
 * 自定义切面类，用于实现公共字段自动填充
 */
@Aspect
@Component
@Slf4j
public class AutoFillAspect {

    /**
     * 切入点
     */
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
    public void autoFillPointCut() {}


    /**
     * 前置通知，在通知中进行公共字段的赋值
     * @param joinPoint
     */
    @Before("autoFillPointCut()")
    public void autoFill(JoinPoint joinPoint) {
        log.info("开始公共字段自动填充");
        // 获取当前被拦截的方法上的数据库操作类型
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        AutoFill autoFill = methodSignature.getMethod().getAnnotation(AutoFill.class);
        OperationType operationType = autoFill.value();
        // 获取当前被拦截的方法上的参数、实体类型
        Object[] args = joinPoint.getArgs();
        if (args == null || args.length == 0) { // 如果没有参数，直接返回
            return;
        }
        Object arg = args[0];
        // 准备赋值的数据
        LocalDateTime now = LocalDateTime.now();
        Long currentId = BaseContext.getCurrentId();
        // 赋值
        if (operationType == OperationType.INSERT) {
            try {
                // 获取实体类中的各个字段
                Field createTime = arg.getClass().getDeclaredField(AutoFillConstant.CREATE_TIME);
                createTime.setAccessible(true);
                createTime.set(arg, now);
                Field createUser = arg.getClass().getDeclaredField(AutoFillConstant.CREATE_USER);
                createUser.setAccessible(true);
                createUser.set(arg, currentId);
                update(arg, now, currentId);
            } catch (NoSuchFieldException | IllegalAccessException e) {
               e.printStackTrace();
            }
        }else if (operationType == OperationType.UPDATE) {
            try {
                update(arg, now, currentId);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private void update(Object arg, LocalDateTime now, Long currentId) throws NoSuchFieldException, IllegalAccessException {
        Field updateTime = arg.getClass().getDeclaredField(AutoFillConstant.UPDATE_TIME);
        updateTime.setAccessible(true);
        updateTime.set(arg, now);
        Field updateUser = arg.getClass().getDeclaredField(AutoFillConstant.UPDATE_USER);
        updateUser.setAccessible(true);
        updateUser.set(arg, currentId);
    }
}
