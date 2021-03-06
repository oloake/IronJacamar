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
package org.jboss.jca.common.api.metadata.ra;

import org.jboss.jca.common.api.validator.ValidateException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * A ResourceAdapter1516.
 *
 * @author <a href="stefano.maestri@jboss.com">Stefano Maestri</a>
 *
 */
public interface ResourceAdapter1516 extends ResourceAdapter, MergeableMetadata<ResourceAdapter1516>
{

   /**
    * @return resourceadapterClass
    */
   public abstract String getResourceadapterClass();

   /**
    * @return configProperty
    */
   @Override
   public abstract List<? extends ConfigProperty> getConfigProperties();

   /**
    * @return outboundResourceadapter
    */
   public abstract OutboundResourceAdapter getOutboundResourceadapter();

   /**
    * @return inboundResourceadapter
    */
   public abstract InboundResourceAdapter getInboundResourceadapter();

   /**
    * @return adminobject
    */
   public abstract List<AdminObject> getAdminObjects();

   /**
    * @return securityPermission
    */
   public abstract List<SecurityPermission> getSecurityPermissions();

   /**
    * {@inheritDoc}
    *
    * @see IdDecoratedMetadata#getId()
    */
   @Override
   public abstract String getId();

   /**
    * {@inheritDoc}
    *
    * @see java.lang.Object#hashCode()
    */
   @Override
   public abstract int hashCode();

   /**
    * {@inheritDoc}
    *
    * @see java.lang.Object#equals(java.lang.Object)
    */
   @Override
   public abstract boolean equals(Object obj);

   /**
    * {@inheritDoc}
    *
    * @see java.lang.Object#toString()
    */
   @Override
   public abstract String toString();

   @Override
   public abstract void validate() throws ValidateException;

   /**
   *
   * A Tag.
   *
   * @author <a href="stefano.maestri@jboss.com">Stefano Maestri</a>
   *
   */
   public enum Tag
   {
      /** always first
       *
       */
      UNKNOWN(null),

      /**
       * resourceadapter-class tag
       */
      RESOURCEADAPTER_CLASS("resourceadapter-class"),

      /**
       * config-property tag
       */
      CONFIG_PROPERTY("config-property"),

      /**
       * outbound-resourceadapte TAG
       */
      OUTBOUND_RESOURCEADAPTER("outbound-resourceadapter"),

      /**
       * inbound-resourceadapter TAG
       */
      INBOUND_RESOURCEADAPTER("inbound-resourceadapter"),

      /**
       * adminobject TAG
       */
      ADMINOBJECT("adminobject"),

      /**
       * security-permission TAG
       */
      SECURITY_PERMISSION("security-permission");

      private final String name;

      /**
       *
       * Create a new Tag.
       *
       * @param name a name
       */
      Tag(final String name)
      {
         this.name = name;
      }

      /**
       * Get the local name of this element.
       *
       * @return the local name
       */
      public String getLocalName()
      {
         return name;
      }

      /**
       * {@inheritDoc}
       */
      public String toString()
      {
         return name;
      }

      private static final Map<String, Tag> MAP;

      static
      {
         final Map<String, Tag> map = new HashMap<String, Tag>();
         for (Tag element : values())
         {
            final String name = element.getLocalName();
            if (name != null)
               map.put(name, element);
         }
         MAP = map;
      }

      /**
      *
      * Static method to get enum instance given localName string
      *
      * @param localName a string used as localname (typically tag name as defined in xsd)
      * @return the enum instance
      */
      public static Tag forName(String localName)
      {
         final Tag element = MAP.get(localName);
         return element == null ? UNKNOWN : element;
      }

   }

   /**
    *
    * A Attribute.
    *
    * @author <a href="stefano.maestri@jboss.com">Stefano Maestri</a>
    *
    */
   public enum Attribute
   {

      /** id attribute
       *
       */
      ID("id");

      private final String name;

      /**
       *
       * Create a new Tag.
       *
       * @param name a name
       */
      Attribute(final String name)
      {
         this.name = name;
      }

      /**
       * Get the local name of this element.
       *
       * @return the local name
       */
      public String getLocalName()
      {
         return name;
      }

      /**
       * {@inheritDoc}
       */
      public String toString()
      {
         return name;
      }
   }
}
