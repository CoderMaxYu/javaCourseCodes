package com.yuwq.spring.common;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.stereotype.Component;


/**
 * 动态注册Bean
 */
@Component
public class StudentBeanDefinitionRegistry implements BeanDefinitionRegistryPostProcessor {
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
        System.out.println(" ==> postProcessBeanDefinitionRegistry: "+beanDefinitionRegistry.getBeanDefinitionCount());
        System.out.println(" ==> postProcessBeanDefinitionRegistry: "+String.join(",",beanDefinitionRegistry.getBeanDefinitionNames()));
        RootBeanDefinition rootBeanDefinition = new RootBeanDefinition(Student.class);
        rootBeanDefinition.getPropertyValues().add("name","abc");
        rootBeanDefinition.getPropertyValues().add("age",22);
        beanDefinitionRegistry.registerBeanDefinition("stuDefi",rootBeanDefinition);



    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        System.out.println(" ==> postProcessBeanFactory: "+configurableListableBeanFactory.getBeanDefinitionCount());
        System.out.println(" ==> postProcessBeanFactory: "+String.join(",",configurableListableBeanFactory.getBeanDefinitionNames()));
    }
}
