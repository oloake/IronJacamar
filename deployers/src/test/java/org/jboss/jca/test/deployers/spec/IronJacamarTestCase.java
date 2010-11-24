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

package org.jboss.jca.test.deployers.spec;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.jboss.shrinkwrap.api.spec.ResourceAdapterArchive;

import org.junit.Test;

import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * Test cases for deploying resource adapter archives (.RAR) using ironjacamar.xml files
 * for activation
 *
 * @author <a href="mailto:jesper.pedersen@jboss.org">Jesper Pedersen</a>
 * @version $Revision: $
 */
public class IronJacamarTestCase extends AbstractDeployerTestCase
{

   // --------------------------------------------------------------------------------||
   // Class Members ------------------------------------------------------------------||
   // --------------------------------------------------------------------------------||

   private static final String JNDI_PREFIX = "java:/eis/";

   /**
    * ra15outironjacamar.rar
    * @throws Throwable throwable exception
    */
   @Test
   public void testRa15out() throws Throwable
   {
      //given
      String archiveName = "ra15outironjacamar.rar";
      String packageName = "org.jboss.jca.test.deployers.spec.rars.ra10dtdout";
      ResourceAdapterArchive raa = buidShrinkwrapRa(archiveName, packageName);
      raa.addManifestResource(archiveName + "/META-INF/ra.xml", "ra.xml");
      raa.addManifestResource(archiveName + "/META-INF/ironjacamar.xml", "ironjacamar.xml");

      Context context = new InitialContext();;

      try
      {
         //when
         embedded.deploy(raa);

         //then
         assertThat(context.lookup(JNDI_PREFIX + "ra15outironjacamar-explicit"), notNullValue());

      }
      finally
      {

         context.close();

         embedded.undeploy(raa);
      }

   }
}
