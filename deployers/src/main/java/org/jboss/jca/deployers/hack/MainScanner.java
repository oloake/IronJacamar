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

package org.jboss.jca.deployers.hack;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import org.jboss.bootstrap.spi.Server;
import org.jboss.bootstrap.spi.microcontainer.MCServer;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.deployers.client.spi.main.MainDeployer;
import org.jboss.deployers.spi.DeploymentException;
import org.jboss.deployers.vfs.spi.client.VFSDeployment;
import org.jboss.deployers.vfs.spi.client.VFSDeploymentFactory;
import org.jboss.kernel.Kernel;
import org.jboss.logging.Logger;
import org.jboss.virtual.VFS;
import org.jboss.virtual.VirtualFile;

/**
 * Main scanner
 * @author Jesper Pedersen <jesper.pedersen@jboss.org>
 */
public class MainScanner
{
   /** The logger */
   private static Logger log = Logger.getLogger(MainScanner.class);

   /** Whether trace is enabled */
   private boolean trace = log.isTraceEnabled();

   /** The MC main deployer */
   private MainDeployer mainDeployer;

   /** The deploy directory */
   private URL deployDirectory;

   /**
    * Constructor
    * @param server The server
    * @param deployDirectory The deploy directory that needs to be scanned
    */
   public MainScanner(Object server, URL deployDirectory)
   {
      if (server == null)
         throw new IllegalArgumentException("Server is null");

      if (deployDirectory == null)
         throw new IllegalArgumentException("DeployDirectory is null");

      if (!(server instanceof MCServer))
         throw new IllegalArgumentException("Server is not a MCServer instance");

      MCServer mcServer = (MCServer)server;
      Kernel kernel = mcServer.getKernel();

      this.mainDeployer = 
         (MainDeployer)kernel.getController().getContext("MainDeployer", ControllerState.INSTALLED).getTarget();
      this.deployDirectory = deployDirectory;

      log.debug("MainScanner created.");
   }

   /**
    * Start
    * @exception Exception Thrown if an error occurs
    */
   public void start() throws Exception 
   {
      log.debug("MainScanner starting.");

      ClassLoader tccl = Thread.currentThread().getContextClassLoader();

      Enumeration<URL> urls = tccl.getResources("META-INF/jboss-beans.xml");
      while (urls.hasMoreElements())
      {
         URL u = urls.nextElement();
         URLConnection c = u.openConnection();

         if (!(c instanceof JarURLConnection))
            continue;

         JarURLConnection connection = (JarURLConnection)c;
         URL jarFileURL = connection.getJarFileURL();
         deploy(jarFileURL);
      }
      
      VirtualFile deployDir = VFS.getRoot(deployDirectory);
      List<VirtualFile> candidates = deployDir.getChildren();

      for (VirtualFile candidate : candidates)
      {
         deploy(candidate.toURL());
      }

      log.debug("MainScanner started.");
   }

   /**
    * Stop
    * @exception Exception Thrown if an error occurs
    */
   public void stop() throws Exception 
   {
      log.debug("MainScanner stopping.");

      log.debug("MainScanner stopped.");
   }

   /**
    * Deploy
    * @param url The URL
    * @exception DeploymentException Thrown if a deploy error occurs
    * @exception IOException Thrown if an I/O error occurs
    */
   protected void deploy(URL url) throws DeploymentException, IOException
   {
      log.info("Deploying: " + url);

      VirtualFile root = VFS.getRoot(url);
      VFSDeployment deployment = VFSDeploymentFactory.getInstance().createVFSDeployment(root);
      mainDeployer.deploy(deployment);
      mainDeployer.checkComplete(deployment);

      log.info("Deployed: " + url);
   }
}