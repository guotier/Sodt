package com.lizi.sodt.util;

import com.lizi.sodt.exception.BeanNameNotFoundException;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.StringUtils;

/**
 * Created by guotie on 18/2/3.
 */
public class SpringContextHolder implements ApplicationContextAware{
    private static ApplicationContext applicationContext;


    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public static Object getBean(String beanName){
        return applicationContext.getBean(beanName);
    }

    public static <T> String getBeanName(Class<T> clazz){
        String beanName = null;
        Object target = null;
        try {
            //先按bean的名称去bean
            String beanNameLikely = SodtStringUtil.toLowerCaseFirstLetter(clazz.getSimpleName());
            target = applicationContext.getBean(beanNameLikely);
            if(target != null){
                beanName = beanNameLikely;
            }
        } catch (BeansException e1){
            //按照类型取bean
            try {
                target = applicationContext.getBean(clazz);
                if(target != null){
                    beanName = applicationContext.getBeanNamesForType(clazz)[0];
                }
            } catch (BeansException e2){
                //按照父类找
                try {
                    Class<? super T> superClazz = clazz.getSuperclass();
                    target = applicationContext.getBean(superClazz);
                    if(target != null){
                        beanName = applicationContext.getBeanNamesForType(superClazz)[0];
                    }
                } catch (BeansException e3){
                    //按照接口找
                    try {
                        Class<?>[] interfaces = clazz.getInterfaces();
                        for(int i=0;i<interfaces.length;i++){
                            target = applicationContext.getBean(interfaces[i]);
                            if(target != null){
                                beanName = applicationContext.getBeanNamesForType(interfaces[i])[0];
                                break;
                            }
                        }
                    } catch (BeansException e4){
                        //执行到这里 bean的class的继承关系应该比较复杂
                        //TODO 在这一版本里暂时不实现
                        throw new BeanNameNotFoundException(clazz.getName() + " 找不到对应的bean的名称");
                    }
                }
            }
        }
        return beanName;
    }
}
