package com.qf.sc.cms.service.aop;


import com.qf.sc.common.AppException;
import com.qf.sc.result.AppResult;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.BeanUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(10)
public class ResultAop {
    @Around("execution(* com.qf.sc.cms.service.impl.*.*(..))")
    public AppResult aroundInvoke(ProceedingJoinPoint joinPoint){
        System.out.println("进入了切面。。。。");
        AppResult result = new AppResult();
        try{
            result = (AppResult) joinPoint.proceed();
        }catch(Throwable ex){
            if(ex instanceof AppException){
                BeanUtils.copyProperties(ex,result);
                result.setSuccess(false);
            }else{
                result = new AppResult(false,"系统异常",201,null);
            }
        }
        return result;
    }

}
