package org.zanata.helper.component;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ContextBeanProvider implements ApplicationContextAware
{
   private static ApplicationContext applicationContext = null;

   public void setApplicationContext(ApplicationContext applicationContext) throws
       BeansException
   {
      ContextBeanProvider.applicationContext = applicationContext;
   }
   
   public static <T> T getBean(Class<T> clazz)
   {
      if(applicationContext != null)
      {
         return applicationContext.getBean(clazz);
      }
      return null;
   }

}
