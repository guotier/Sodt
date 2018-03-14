package com.lizi.sodt.revovery;

import com.alibaba.dubbo.remoting.TimeoutException;
import com.alibaba.dubbo.rpc.RpcException;
import com.lizi.sodt.core.domain.Transaction;
import com.lizi.sodt.dao.TransactionDao;
import com.lizi.sodt.exception.CancelMethodNotDefineException;
import com.lizi.sodt.util.SpringContextHolder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.SerializationUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;

/**
 * Created by guotie on 18/2/6.
 */
public class SodtRecoveryJob {

    private TransactionDao transactionDao;


    @Scheduled(cron = "0 0/1 * * * ?")
    public void recoverCancelingTransaction(){
        List<Transaction> cancelingTransactions = transactionDao.findAllCancelingTransactions();
        for(Transaction cancelingTransaction : cancelingTransactions){
            //从容器中获取对应的bean
            //TODO 如果一个类型在容器中定义了两个bean 此版本的框架解决不了这种情况
            //TODO 也就是说此版本框架一个类型类只能在容器中有一个bean
            Object target = SpringContextHolder.getBean(cancelingTransaction.getBeanName());
            //方法名称
            String cancelMethodName = cancelingTransaction.getMethodName();
            //方法入参类型
            Class<?>[] parameterTypes = (Class<?>[]) SerializationUtils.deserialize(cancelingTransaction.getParameterTypes());
            //方法实际参数
            Object[] parameters = (Object[]) SerializationUtils.deserialize(cancelingTransaction.getParameters());

            try {
                Method cancelMethod = target.getClass().getMethod(cancelMethodName, parameterTypes);
                cancelMethod.setAccessible(true);
                cancelMethod.invoke(target, parameters);
            } catch (NoSuchMethodException e) {
                //cancelMethod方法名称或者参数没有完全匹配，导致反射失败
                throw new CancelMethodNotDefineException(target.getClass().getName() + ":" + cancelMethodName + "定义的不正确");
            } catch (InvocationTargetException e){
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
                e.printStackTrace();
            }
            System.out.println("cancelMethod方法完成");
        }
    }

    @Scheduled(cron = "0 0/1 * * * ?")
    public void recoverConfirmingTransaction(){
        List<Transaction> confirmingTransactions = transactionDao.findAllConfirmingTransactions();
        for(Transaction confirmingTransaction : confirmingTransactions){
            //从容器中获取对应的bean
            //TODO 如果一个类型在容器中定义了两个bean 此版本的框架解决不了这种情况
            //TODO 也就是说此版本框架一个类型类只能在容器中有一个bean
            Object target = SpringContextHolder.getBean(confirmingTransaction.getBeanName());
            //方法名称
            String cancelMethodName = confirmingTransaction.getMethodName();
            //方法入参类型
            Class<?>[] parameterTypes = (Class<?>[]) SerializationUtils.deserialize(confirmingTransaction.getParameterTypes());
            //方法实际参数
            Object[] parameters = (Object[]) SerializationUtils.deserialize(confirmingTransaction.getParameters());

            try {
                Method confirmMethod = target.getClass().getMethod(cancelMethodName, parameterTypes);
                confirmMethod.setAccessible(true);
                confirmMethod.invoke(target, parameters);
            } catch (NoSuchMethodException e) {
                //cancelMethod方法名称或者参数没有完全匹配，导致反射失败
                throw new CancelMethodNotDefineException(target.getClass().getName() + ":" + cancelMethodName + "定义的不正确");
            } catch (InvocationTargetException e){
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
                e.printStackTrace();
            }
            System.out.println("cancelMethod方法完成");
        }
    }

    public TransactionDao getTransactionDao() {
        return transactionDao;
    }

    public void setTransactionDao(TransactionDao transactionDao) {
        this.transactionDao = transactionDao;
    }
}
