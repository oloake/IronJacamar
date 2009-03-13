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
package org.jboss.jca.test.core.spec.chapter10.section3;

import javax.resource.spi.BootstrapContext;
import javax.resource.spi.work.Work;
import javax.resource.spi.work.WorkManager;

import org.jboss.ejb3.test.mc.bootstrap.EmbeddedTestMcBootstrap;
import org.jboss.jca.common.api.ThreadPool;
import org.jboss.jca.common.threadpool.ThreadPoolImpl;

import org.jboss.jca.test.core.spec.chapter10.SimpleBootstrapContext;
import org.jboss.jca.test.core.spec.chapter10.SimpleWork;

import org.junit.AfterClass;
import static org.junit.Assert.* ;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 * WorkManagementModelTestCase.
 * 
 * Tests for the JCA specific Chapter 10 Section 3
 * 
 * @author <a href="mailto:jeff.zhang@jboss.org">Jeff Zhang</a>
 * @version $Revision: $
 */
public class WorkManagementModelTestCase
{
   /*
    * Bootstrap (MC Facade)
    */
   private static EmbeddedTestMcBootstrap bootstrap;
      
   /**
    * Test for paragraph 1
    * A resource adapter obtains a WorkManager instance from the BootstrapContext
    *            instance provided by the application server during its deployment.
    */
   @Test
   public void testGetWorkManagerFromBootstrapConext() throws Throwable
   {
      BootstrapContext bootstrapContext = bootstrap.lookup("SimpleBootstrapContext", SimpleBootstrapContext.class);
      assertTrue(bootstrapContext instanceof SimpleBootstrapContext);

      assertNotNull(bootstrapContext.getWorkManager());
   }

   /**
    * Test for paragraph 2
    * When a Work instance is submitted, one of the free threads picks up the
    *            Work instance, sets up an appropriate execution context and 
    *            calls the run method on the Work instance. 
    */
   @Test
   public void testOneThreadPickWorkInstance() throws Throwable
   {
      WorkManager workManager = bootstrap.lookup("WorkManager", WorkManager.class);
      ThreadPoolImpl tpImpl = (ThreadPoolImpl)bootstrap.lookup("WorkManagerThreadPool", ThreadPool.class);
      int poolNum = tpImpl.getPoolNumber();
      int poolSize = tpImpl.getPoolSize();

      SimpleWork work = new SimpleWork();
      work.setBlockRun(true);
      
      assertFalse(work.isCallRun());
      workManager.scheduleWork(work);
      
      assertEquals(poolNum, tpImpl.getPoolNumber());
      assertEquals(poolSize + 1, tpImpl.getPoolSize());
   }
   
   /**
    * Test for paragraph2
    * There is no restriction on the NUMBER of Work instances submitted by a 
    *            resource adapter or when Work instances may be submitted.
    */
   @Test
   public void testManyWorkInstancesSubmitted() throws Throwable
   {
      WorkManager workManager = bootstrap.lookup("WorkManager", WorkManager.class);
      SimpleWork work1 = new SimpleWork();
      work1.setBlockRun(true);
      SimpleWork work2 = new SimpleWork();
      work2.setBlockRun(true);
      SimpleWork work3 = new SimpleWork();
      work3.setBlockRun(true);
      
      assertFalse(work1.isCallRun());
      assertFalse(work2.isCallRun());
      assertFalse(work3.isCallRun());
      workManager.startWork(work1);
      workManager.startWork(work2);
      workManager.startWork(work3);
      Thread.currentThread().sleep(SimpleWork.BLOCK_TIME + SimpleWork.FOLLOW_TIME);
      assertTrue(work1.isCallRun());
      assertTrue(work2.isCallRun());
      assertTrue(work3.isCallRun());
      work1 = null;
      work2 = null;
      work3 = null;
   }
   
   /**
    * Test for paragraph 2
    * There is no restriction on the number of Work instances submitted by a 
    *            resource adapter or WHEN Work instances may be submitted.
    */
   @Test
   public void testAnytimeWorkInstanceSubmitted() throws Throwable
   {
      WorkManager workManager = bootstrap.lookup("WorkManager", WorkManager.class);
      SimpleWork work1 = new SimpleWork();
      work1.setBlockRun(true);
      SimpleWork work2 = new SimpleWork();
      work2.setBlockRun(true);
      
      assertFalse(work1.isCallRun());
      assertFalse(work2.isCallRun());

      workManager.startWork(work1);
      Thread.currentThread().sleep(SimpleWork.FOLLOW_TIME);
      workManager.startWork(work2);

      Thread.currentThread().sleep(SimpleWork.BLOCK_TIME + SimpleWork.FOLLOW_TIME);
      assertTrue(work1.isCallRun());
      assertTrue(work2.isCallRun());

      work1 = null;
      work2 = null;
   }
   
   /**
    * Test for paragraph 2
    * When the run method on the Work instance completes, the application 
    *            server reuses the thread.
    */
   @Test
   public void testThreadBackPoolWhenWorkDone() throws Throwable
   {
      WorkManager workManager = bootstrap.lookup("WorkManager", WorkManager.class);
      ThreadPoolImpl tpImpl = (ThreadPoolImpl)bootstrap.lookup("WorkManagerThreadPool", ThreadPool.class);
      int poolNum = tpImpl.getPoolNumber();
      int poolSize = tpImpl.getPoolSize();


      SimpleWork work = new SimpleWork();
      
      assertFalse(work.isCallRun());
      workManager.doWork(work);
      assertTrue(work.isCallRun());
      
      assertEquals(poolNum, tpImpl.getPoolNumber());
      assertEquals(poolSize, tpImpl.getPoolSize());
   }
   
   /**
    * Test for paragraph 3
    * The application server may decide to reclaim active threads based on load conditions. 
    * @see https://jira.jboss.org/jira/browse/JBJCA-42
    */
   @Ignore
   public void testAsActiveThreadOnLoadCondition() throws Throwable
   {
   }   
   
   /**
    * Test for paragraph 3
    * The resource adapter should periodically monitor such hints and do the 
    *            necessary internal cleanup to avoid any inconsistencies. 
    * @see https://jira.jboss.org/jira/browse/JBJCA-43
    */
   @Ignore
   public void testRaPeriodicalReleaseWorkResource() throws Throwable
   {
   }   
   
   /**
    * Test for paragraph 4
    * the application server must use threads of the same thread priority level to
    *            process Work instances submitted by a specific resource adapter. 
    */
   @Ignore
   public void testAsUseThreadSamePriorityLevel() throws Throwable
   {
   }   
   
   // --------------------------------------------------------------------------------||
   // Lifecycle Methods --------------------------------------------------------------||
   // --------------------------------------------------------------------------------||
   /**
    * Lifecycle start, before the suite is executed
    */
   @BeforeClass
   public static void beforeClass() throws Throwable
   {
      // Create and set a new MC Bootstrap
      bootstrap = EmbeddedTestMcBootstrap.createEmbeddedMcBootstrap();

      // Deploy Naming and Transaction
      bootstrap.deploy(WorkManagerInterfaceTestCase.class.getClassLoader(), "naming-jboss-beans.xml");
      bootstrap.deploy(WorkManagerInterfaceTestCase.class.getClassLoader(), "transaction-jboss-beans.xml");
      
      // Deploy Beans
      bootstrap.deploy(WorkManagerInterfaceTestCase.class);
   }

   /**
    * Lifecycle stop, after the suite is executed
    */
   @AfterClass
   public static void afterClass() throws Throwable
   {
      // Undeploy Transaction and Naming
      bootstrap.undeploy(WorkManagerInterfaceTestCase.class.getClassLoader(), "transaction-jboss-beans.xml");
      bootstrap.undeploy(WorkManagerInterfaceTestCase.class.getClassLoader(), "naming-jboss-beans.xml");

      // Undeploy Beans
      bootstrap.undeploy(WorkManagerInterfaceTestCase.class);

      // Shutdown MC
      bootstrap.shutdown();

      // Set Bootstrap to null
      bootstrap = null;
   }
}