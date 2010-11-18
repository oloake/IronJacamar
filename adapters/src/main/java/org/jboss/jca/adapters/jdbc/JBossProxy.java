/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2008, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.jca.adapters.jdbc;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.util.proxy.MethodFilter;
import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;
import javassist.util.proxy.ProxyObject;

import javax.resource.spi.IllegalStateException;

public class JBossProxy
{

   private final static boolean PROXY = true;

   public JBossProxy()
   {

   }


   @SuppressWarnings("unchecked")
   public static <T> T getProxyOrWrapper(final Object actualObject, final JBossWrapper wrapper, Class<T> iface)
      throws Exception
   {
      if (!wrapper.isWrapperFor(iface))
         throw new IllegalStateException("wrapper class is not wrapping this interface");

      if (PROXY)
      {
         return create(actualObject, wrapper, iface);
      }
      else
      {
         return (T) wrapper;
      }
   }

   @SuppressWarnings("unchecked")
   private static <T> T create(final Object actualObject, final JBossWrapper wrapper, Class<T>... ifaces)
      throws Exception
   {
      ProxyFactory f = new ProxyFactory();
      ClassPool cPoll = ClassPool.getDefault();

      CtClass ctclass = cPoll.get(actualObject.getClass().getName());

      CtConstructor constructor = new CtConstructor(new CtClass[]{}, ctclass);
      constructor
         .setBody("System.out.println(\"*********************************************************************************+\");");
      ctclass.addConstructor(constructor);
      Class newClass = ctclass.toClass();

      f.setSuperclass(actualObject.getClass());
      f.setInterfaces(ifaces);
      f.setFilter(new MethodFilter()
      {
         public boolean isHandled(Method m)
         {
            // ignore finalize()
            return !m.getName().equals("finalize");
         }
      });
      Class c = f.createClass();
            MethodHandler mi = new MethodHandler()
      {
         public Object invoke(Object self, Method m, Method proceed, Object[] args) throws Throwable
         {
            // execute the original method.
            return wrapper.getClass().getMethod(m.getName(), m.getParameterTypes()).invoke(wrapper, args);
         }
      };

      Constructor<T> co = c.getConstructors()[0];
      Object[] params = new Object[co.getParameterTypes().length];
      T proxy = co.newInstance(params);
      ((ProxyObject) proxy).setHandler(mi);
      return proxy;
   }

}
