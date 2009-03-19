/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2008-2009, Red Hat Middleware LLC, and individual contributors
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

package org.jboss.jca.test.core.mdr.unit;

import org.jboss.jca.core.api.MetaDataRepository;

import java.net.URL;

import org.jboss.ejb3.test.mc.bootstrap.EmbeddedTestMcBootstrap;
import org.jboss.logging.Logger;
import org.jboss.metadata.rar.jboss.RARDeploymentMetaData;
import org.jboss.metadata.rar.jboss.mcf.ManagedConnectionFactoryDeploymentGroup;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * MetaDataRepositoryTestCase.
 * 
 * Tests for the JBoss specific metadata repository functionality.
 * 
 * @author <a href="mailto:jesper.pedersen@jboss.org">Jesper Pedersen</a>
 * @version $Revision: $
 */
public class MetaDataRepositoryTestCase
{

   // --------------------------------------------------------------------------------||
   // Class Members ------------------------------------------------------------------||
   // --------------------------------------------------------------------------------||

   private static Logger log = Logger.getLogger(MetaDataRepositoryTestCase.class);

   /*
    * Bootstrap (MC Facade)
    */
   private static EmbeddedTestMcBootstrap bootstrap;

   // --------------------------------------------------------------------------------||
   // Tests --------------------------------------------------------------------------||
   // --------------------------------------------------------------------------------||

   /**
    * Test that the MetaDataRepository is defined in the MC
    * @throws Throwable throwable exception 
    */
   @Test
   public void testDefined() throws Throwable
   {
      MetaDataRepository mdr =
         bootstrap.lookup("MetaDataRepository", MetaDataRepository.class);

      assertNotNull(mdr);
   }

   /**
    * Test null name for addRARDeploymentMetaData
    * @throws Throwable throwable exception 
    */
   @Test
   public void testAddRARDeploymentMetaDataNullName() throws Throwable
   {
      MetaDataRepository mdr =
         bootstrap.lookup("MetaDataRepository", MetaDataRepository.class);

      assertNotNull(mdr);

      try
      {
         RARDeploymentMetaData rdmd = mdr.addRARDeploymentMetaData(null, null);
         fail("Null name");
      }
      catch (Throwable t)
      {
         // Expected
      }
   }

   /**
    * Test null rdmd for addRARDeploymentMetaData
    * @throws Throwable throwable exception 
    */
   @Test
   public void testAddRARDeploymentMetaDataNullRDMD() throws Throwable
   {
      MetaDataRepository mdr =
         bootstrap.lookup("MetaDataRepository", MetaDataRepository.class);

      assertNotNull(mdr);

      try
      {
         RARDeploymentMetaData rdmd = mdr.addRARDeploymentMetaData("test", null);
         fail("Null rdmd");
      }
      catch (Throwable t)
      {
         // Expected
      }
   }

   /**
    * Test for addRARDeploymentMetaData
    * @throws Throwable throwable exception 
    */
   @Test
   public void testAddRARDeploymentMetaData() throws Throwable
   {
      MetaDataRepository mdr =
         bootstrap.lookup("MetaDataRepository", MetaDataRepository.class);

      assertNotNull(mdr);

      RARDeploymentMetaData rdmd = new RARDeploymentMetaData();
      rdmd = mdr.addRARDeploymentMetaData("test", rdmd);

      mdr.removeRARDeploymentMetaData("test");
   }

   /**
    * Test null name for getRARDeploymentMetaData
    * @throws Throwable throwable exception 
    */
   @Test
   public void testGetRARDeploymentMetaDataNullName() throws Throwable
   {
      MetaDataRepository mdr =
         bootstrap.lookup("MetaDataRepository", MetaDataRepository.class);

      assertNotNull(mdr);

      try
      {
         RARDeploymentMetaData rdmd = mdr.getRARDeploymentMetaData(null);
         fail("Null name");
      }
      catch (Throwable t)
      {
         // Expected
      }
   }

   /**
    * Test undefined name for getRARDeploymentMetaData
    * @throws Throwable throwable exception 
    */
   @Test
   public void testGetRARDeploymentMetaDataUndefinedName() throws Throwable
   {
      MetaDataRepository mdr =
         bootstrap.lookup("MetaDataRepository", MetaDataRepository.class);

      assertNotNull(mdr);

      RARDeploymentMetaData rdmd = mdr.getRARDeploymentMetaData("test");
      assertNull(rdmd);
   }

   /**
    * Test for getRARDeploymentMetaData
    * @throws Throwable throwable exception 
    */
   @Test
   public void testGetRARDeploymentMetaData() throws Throwable
   {
      MetaDataRepository mdr =
         bootstrap.lookup("MetaDataRepository", MetaDataRepository.class);

      assertNotNull(mdr);

      RARDeploymentMetaData rdmd1 = new RARDeploymentMetaData();
      mdr.addRARDeploymentMetaData("test", rdmd1);

      RARDeploymentMetaData rdmd2 = mdr.getRARDeploymentMetaData("test");
      assertNotNull(rdmd2);

      assertEquals("Objects are not the same", rdmd1, rdmd2);

      mdr.removeRARDeploymentMetaData("test");
   }

   /**
    * Test null name for removeRARDeploymentMetaData
    * @throws Throwable throwable exception 
    */
   @Test
   public void testRemoveRARDeploymentMetaDataNullName() throws Throwable
   {
      MetaDataRepository mdr =
         bootstrap.lookup("MetaDataRepository", MetaDataRepository.class);

      assertNotNull(mdr);

      try
      {
         boolean result = mdr.removeRARDeploymentMetaData(null);
         fail("Null name");
      }
      catch (Throwable t)
      {
         // Expected
      }
   }

   /**
    * Test undefined name for removeRARDeploymentMetaData
    * @throws Throwable throwable exception 
    */
   @Test
   public void testRemoveRARDeploymentMetaDataUndefinedName() throws Throwable
   {
      MetaDataRepository mdr =
         bootstrap.lookup("MetaDataRepository", MetaDataRepository.class);

      assertNotNull(mdr);

      assertFalse(mdr.removeRARDeploymentMetaData("test"));
   }

   /**
    * Test for removeRARDeploymentMetaData
    * @throws Throwable throwable exception 
    */
   @Test
   public void testRemoveRARDeploymentMetaData() throws Throwable
   {
      MetaDataRepository mdr =
         bootstrap.lookup("MetaDataRepository", MetaDataRepository.class);

      assertNotNull(mdr);

      RARDeploymentMetaData rdmd = new RARDeploymentMetaData();
      mdr.addRARDeploymentMetaData("test", rdmd);

      assertTrue(mdr.removeRARDeploymentMetaData("test"));
   }

   /**
    * Test null name for addMCFDG
    * @throws Throwable throwable exception 
    */
   @Test
   public void testAddMCFDGNullName() throws Throwable
   {
      MetaDataRepository mdr =
         bootstrap.lookup("MetaDataRepository", MetaDataRepository.class);

      assertNotNull(mdr);

      try
      {
         ManagedConnectionFactoryDeploymentGroup group = mdr.addManagedConnectionFactoryDeploymentGroup(null, null);
         fail("Null name");
      }
      catch (Throwable t)
      {
         // Expected
      }
   }

   /**
    * Test null group for addMCFDG
    * @throws Throwable throwable exception 
    */
   @Test
   public void testAddMCFDGNullGroup() throws Throwable
   {
      MetaDataRepository mdr =
         bootstrap.lookup("MetaDataRepository", MetaDataRepository.class);

      assertNotNull(mdr);

      try
      {
         ManagedConnectionFactoryDeploymentGroup group = mdr.addManagedConnectionFactoryDeploymentGroup("test", null);
         fail("Null group");
      }
      catch (Throwable t)
      {
         // Expected
      }
   }

   /**
    * Test for addMCFDG
    * @throws Throwable throwable exception 
    */
   @Test
   public void testAddMCFDG() throws Throwable
   {
      MetaDataRepository mdr =
         bootstrap.lookup("MetaDataRepository", MetaDataRepository.class);

      assertNotNull(mdr);

      ManagedConnectionFactoryDeploymentGroup group = new ManagedConnectionFactoryDeploymentGroup();
      mdr.addManagedConnectionFactoryDeploymentGroup("test", group);

      mdr.removeManagedConnectionFactoryDeploymentGroup("test");
   }

   /**
    * Test null name for getMCFDG
    * @throws Throwable throwable exception 
    */
   @Test
   public void testGetMCFDGNullName() throws Throwable
   {
      MetaDataRepository mdr =
         bootstrap.lookup("MetaDataRepository", MetaDataRepository.class);

      assertNotNull(mdr);

      try
      {
         ManagedConnectionFactoryDeploymentGroup group = mdr.getManagedConnectionFactoryDeploymentGroup(null);
         fail("Null name");
      }
      catch (Throwable t)
      {
         // Expected
      }
   }

   /**
    * Test undefined name for getMCFDG
    * @throws Throwable throwable exception 
    */
   @Test
   public void testGetMCFDGUndefinedName() throws Throwable
   {
      MetaDataRepository mdr =
         bootstrap.lookup("MetaDataRepository", MetaDataRepository.class);

      assertNotNull(mdr);

      ManagedConnectionFactoryDeploymentGroup group = mdr.getManagedConnectionFactoryDeploymentGroup("test");
      assertNull(group);
   }

   /**
    * Test for getMCFDG
    * @throws Throwable throwable exception 
    */
   @Test
   public void testGetMCFDG() throws Throwable
   {
      MetaDataRepository mdr =
         bootstrap.lookup("MetaDataRepository", MetaDataRepository.class);

      assertNotNull(mdr);

      ManagedConnectionFactoryDeploymentGroup group1 = new ManagedConnectionFactoryDeploymentGroup();
      mdr.addManagedConnectionFactoryDeploymentGroup("test", group1);

      ManagedConnectionFactoryDeploymentGroup group2 = mdr.getManagedConnectionFactoryDeploymentGroup("test");
      assertNotNull(group2);

      assertEquals("Objects are not the same", group1, group2);

      mdr.removeManagedConnectionFactoryDeploymentGroup("test");
   }

   /**
    * Test null name for removeMCFDG
    * @throws Throwable throwable exception 
    */
   @Test
   public void testRemoveMCFDGNullName() throws Throwable
   {
      MetaDataRepository mdr =
         bootstrap.lookup("MetaDataRepository", MetaDataRepository.class);

      assertNotNull(mdr);

      try
      {
         boolean result = mdr.removeManagedConnectionFactoryDeploymentGroup(null);
         fail("Null name");
      }
      catch (Throwable t)
      {
         // Expected
      }
   }

   /**
    * Test undefined name for removeMCFDG
    * @throws Throwable throwable exception 
    */
   @Test
   public void testRemoveMCFDGUndefinedName() throws Throwable
   {
      MetaDataRepository mdr =
         bootstrap.lookup("MetaDataRepository", MetaDataRepository.class);

      assertNotNull(mdr);

      assertFalse(mdr.removeManagedConnectionFactoryDeploymentGroup("test"));
   }

   /**
    * Test for removeMCFDG
    * @throws Throwable throwable exception 
    */
   @Test
   public void testRemoveMCFDG() throws Throwable
   {
      MetaDataRepository mdr =
         bootstrap.lookup("MetaDataRepository", MetaDataRepository.class);

      assertNotNull(mdr);

      ManagedConnectionFactoryDeploymentGroup group = new ManagedConnectionFactoryDeploymentGroup();
      mdr.addManagedConnectionFactoryDeploymentGroup("test", group);

      assertTrue(mdr.removeManagedConnectionFactoryDeploymentGroup("test"));
   }

   // --------------------------------------------------------------------------------||
   // Lifecycle Methods --------------------------------------------------------------||
   // --------------------------------------------------------------------------------||

   /**
    * Lifecycle start, before the suite is executed
    * @throws Throwable throwable exception 
    */
   @BeforeClass
   public static void beforeClass() throws Throwable
   {
      // Create and set a new MC Bootstrap
      bootstrap = EmbeddedTestMcBootstrap.createEmbeddedMcBootstrap();

      // Deploy Beans
      bootstrap.deploy(MetaDataRepositoryTestCase.class);
   }

   /**
    * Lifecycle stop, after the suite is executed
    * @throws Throwable throwable exception 
    */
   @AfterClass
   public static void afterClass() throws Throwable
   {
      // Undeploy Beans
      bootstrap.undeploy(MetaDataRepositoryTestCase.class);

      // Shutdown MC
      bootstrap.shutdown();

      // Set Bootstrap to null
      bootstrap = null;
   }
}
