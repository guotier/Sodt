package com.lizi.sodt.aspect;

import com.alibaba.dubbo.remoting.TimeoutException;
import com.alibaba.dubbo.rpc.RpcException;
import com.lizi.sodt.annotation.Sodt;
import com.lizi.sodt.core.domain.Transaction;
import com.lizi.sodt.core.support.TransactionTemplate;
import com.lizi.sodt.util.SodtMethodUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by guotie on 18/1/24.
 */
@Aspect
public class SodtAspect {

    @Pointcut("@annotation(com.lizi.sodt.annotation.Sodt)")
    public void sodtService(){}


    @Around("sodtService()")
    public void intercept(ProceedingJoinPoint joinPoint) throws Throwable{
        //开始全局事务， 状态为TRYING
        Transaction transaction = new Transaction(joinPoint);
        transactionTemplate.beginTranscation();

        Method method = SodtMethodUtil.getMethod(joinPoint);
        Sodt sodt = method.getAnnotation(Sodt.class);
        String confirmMethodName = sodt.confirmMethod();
        String cancelMethodName = sodt.cancelMethod();

        Object target = joinPoint.getTarget();
        Object[] args = joinPoint.getArgs();
        try {
            joinPoint.proceed();
        } catch (Exception e){
            //其他原因导致的异常
            //全局事务状态改为CANCELING
            transactionTemplate.rollbackTransaction();
            invokeMethod(cancelMethodName, target, method.getParameterTypes(), args);
            transactionTemplate.rollbackedTransaction();
            //全局事务状态改为CANCELED
            return;
        }

        //全局事务状态改为CONFIRMING
        transactionTemplate.commitTransaction();
        invokeMethod(confirmMethodName, target, method.getParameterTypes(), args);
        transactionTemplate.commitedTransaction();
        //全局事务状态改为CONFIRMED

        //全局事务完成之后，需要清除
        //transactionTemplate.cleanAfterCompletion();
        return;
    }

    private void invokeMethod(String methodName, Object target, Class<?>[] parameterTypes, Object[] parameters){
        try {
            Method method = target.getClass().getMethod(methodName, parameterTypes);
            method.setAccessible(true);
            method.invoke(target, parameters);
        } catch (NoSuchMethodException e) {
            //全局事务方法没有配置对应的method的方法，需要进行记录
            /*
             *  后续的改进方向，能否在启动时就检测出这些没有配置method的方法
             */
            System.out.println(target.getClass().toString() + methodName + parameterTypes + "方法没有被配置");
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            //method.setAccessible(true)之后，应该就不会跑出这个异常了
            e.printStackTrace();
        } catch (IllegalArgumentException e){
            //全局事务方法配置的对应的method方法参数跟原生方法参数不一致
            /*
             *  后续的改进方向，能否在启动时就检测出这些配置method参数，但是参数不一致的方法
             */
            System.out.println(target.getClass().toString() + methodName + parameterTypes + parameters + "方法配置的参数与原生方法不一致");
            e.printStackTrace();
        }catch (InvocationTargetException e) {
            if(e.getTargetException() != null && e.getTargetException() instanceof RpcException){
                //dubbo调用异常
                if(e.getTargetException().getCause() instanceof TimeoutException){
                    System.out.println("服务调用超时");
                    System.out.println("需要在数据库中进行记录，之后要进行重试");
                    e.printStackTrace();
                }else{
                    e.printStackTrace();
                }

            }else{
                System.out.println("需要在数据库中进行记录，之后要进行重试");
                e.printStackTrace();
            }
        } catch (Exception e){
            if(e instanceof TimeoutException){
                //普遍因为网络抖动原因引起的dubbo调用超时异常
                System.out.println("服务调用超时");
                System.out.println("需要在数据库中进行记录，之后要进行重试");
                e.printStackTrace();
            }else{
                System.out.println("其他原因导致的异常");
                System.out.println("需要在数据库中进行记录，之后要进行重试");
                e.printStackTrace();
            }
        }
    }

    private TransactionTemplate transactionTemplate;

    public TransactionTemplate getTransactionTemplate() {
        return transactionTemplate;
    }

    public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
        this.transactionTemplate = transactionTemplate;
    }
}
