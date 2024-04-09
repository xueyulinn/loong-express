package com.loong.aspect;

import java.time.LocalDateTime;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import com.loong.annotation.AutoFill;
import com.loong.context.BaseContext;
import com.loong.enumeration.OperationType;

@Aspect
@Component
public class AutoFillAspect {

    // pointcut helps specifies where to apply advice
    @Pointcut("execution(* com.loong.mapper.*.*(..)) && @annotation(com.loong.annotation.AutoFill)")
    public void autoFillPointCut() {
    }

    @Before("autoFillPointCut()")
    public void autoFill(JoinPoint joinpoint) {

        // get the method signature and then get the annotation
        MethodSignature signature = (MethodSignature) joinpoint.getSignature();
        AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class);

        Object[] args = joinpoint.getArgs();

        Object entity = args[0];

        LocalDateTime now = LocalDateTime.now();

        Long id = BaseContext.getCurrentId();

        // using reflection to set the values of the fields
        if (entity != null && entity != "") {

            if (autoFill.value().equals(OperationType.INSERT)) {

                try {
                    entity.getClass().getDeclaredMethod("setCreateTime", LocalDateTime.class).invoke(entity, now);
                    entity.getClass().getDeclaredMethod("setUpdateTime", LocalDateTime.class).invoke(entity, now);
                    entity.getClass().getDeclaredMethod("setCreateUser", Long.class).invoke(entity, id);
                    entity.getClass().getDeclaredMethod("setUpdateUser", Long.class).invoke(entity, id);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (autoFill.value().equals(OperationType.UPDATE)) {

                try {
                    entity.getClass().getDeclaredMethod("setUpdateTime", LocalDateTime.class).invoke(entity, now);
                    entity.getClass().getDeclaredMethod("setUpdateUser", Long.class).invoke(entity, id);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

}
