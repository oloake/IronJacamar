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
package org.jboss.jca.common.metadata.jbossra;

import org.jboss.jca.common.metadata.MetadataParser;
import org.jboss.jca.common.metadata.ParserException;
import org.jboss.jca.common.metadata.jbossra.jbossra10.JbossRa10;
import org.jboss.jca.common.metadata.jbossra.jbossra20.JbossRa20;
import org.jboss.jca.common.metadata.jbossra.jbossra20.RaConfigProperty;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import static javax.xml.stream.XMLStreamConstants.*;

/**
 * A JbossRaParser.
 *
 * @author <a href="stefano.maestri@jboss.com">Stefano Maestri</a>
 *
 */
public class JbossRaParser implements MetadataParser<JbossRa>
{

   /**
    * Parse the xml file and return the {@link JbossRa} metadata
    * @param xmlFile The xml file to parse
    * @return The {@link JbossRa} metadata
    * @exception Exception Thrown if an error occurs
    */
   @Override
   public JbossRa parse(File xmlFile) throws Exception
   {
      XMLInputFactory inputFactory = XMLInputFactory.newInstance();
      InputStream input = new FileInputStream(xmlFile);
      XMLStreamReader reader = inputFactory.createXMLStreamReader(input);
      JbossRa jbossRa = null;

      while (reader.hasNext())
      {
         switch (reader.nextTag())
         {
            case END_ELEMENT : {
               // should mean we're done, so ignore it.
               break;
            }
            case START_ELEMENT : {
               if (JbossRa10.NAMESPACE.equals(reader.getNamespaceURI()))
               {
                  switch (Tag.forName(reader.getLocalName()))
                  {
                     case JBOSSRA : {
                        jbossRa = parseJbossRa10(reader);
                        break;
                     }
                     default :
                        throw new ParserException("Unexpected element:" + reader.getLocalName());
                  }

               }
               else if (JbossRa20.NAMESPACE.equals(reader.getNamespaceURI()))
               {
                  switch (Tag.forName(reader.getLocalName()))
                  {
                     case JBOSSRA : {
                        jbossRa = parseJbossRa20(reader, jbossRa);
                        break;
                     }
                     default :
                        throw new ParserException("Unexpected element:" + reader.getLocalName());
                  }
               }
               else
               {
                  throw new ParserException(String.format("Namespace %s is not supported by %s parser",
                        reader.getNamespaceURI(), this.getClass().getName()));
               }

               break;
            }
            default :
               throw new IllegalStateException();
         }
      }
      return jbossRa;

   }

   private JbossRa20 parseJbossRa20(XMLStreamReader reader, JbossRa jbossRa) throws XMLStreamException
   {
      return null;
   }

   private JbossRa10 parseJbossRa10(XMLStreamReader reader) throws XMLStreamException, ParserException
   {
      List<RaConfigProperty<?>> raConfigProperties = new LinkedList<RaConfigProperty<?>>();
      while (reader.hasNext())
      {
         switch (reader.nextTag())
         {
            case END_ELEMENT : {
               if (Tag.forName(reader.getLocalName()) == Tag.JBOSSRA)
               {
                  return new JbossRa10(raConfigProperties);
               }
               else
               {
                  if (JbossRa10.Tag.forName(reader.getLocalName()) == JbossRa10.Tag.UNKNOWN)
                  {
                     throw new ParserException("unexpected end tag" + reader.getLocalName());
                  }
               }
               break;
            }
            case START_ELEMENT : {
               switch (JbossRa10.Tag.forName(reader.getLocalName()))
               {
                  case RA_CONFIG_PROPERTY : {
                     raConfigProperties.add(parseConfigProperty(reader));
                     break;
                  }
                  default :
                     throw new ParserException("Unexpected element:" + reader.getLocalName());
               }
               break;
            }
         }
      }
      throw new ParserException("Reached end of xml document unexpectedly");
   }

   private RaConfigProperty<?> parseConfigProperty(XMLStreamReader reader) throws XMLStreamException, ParserException
   {
      String value = null;
      String type = null;
      String name = null;
      while (reader.hasNext())
      {
         switch (reader.nextTag())
         {
            case END_ELEMENT : {
               //maeste: to be verified
               if (JbossRa10.Tag.forName(reader.getLocalName()) == JbossRa10.Tag.RA_CONFIG_PROPERTY
                     || JbossRa20.Tag.forName(reader.getLocalName()) == JbossRa20.Tag.RA_CONFIG_PROPERTY)
               {
                  return RaConfigProperty.buildRaConfigProperty(name, value, type);
               }
               else
               {
                  if (JbossRa10.Tag.forName(reader.getLocalName()) == JbossRa10.Tag.UNKNOWN
                        && JbossRa20.Tag.forName(reader.getLocalName()) == JbossRa20.Tag.UNKNOWN)
                  {
                     throw new ParserException("unexpected end tag" + reader.getLocalName());
                  }
               }
               break;
            }
            case START_ELEMENT : {
               switch (RaConfigProperty.Tag.forName(reader.getLocalName()))
               {
                  case RA_CONFIG_PROPERTY_NAME : {
                     name = reader.getElementText();
                     break;
                  }
                  case RA_CONFIG_PROPERTY_VALUE : {
                     value = reader.getElementText();
                     break;
                  }
                  case RA_CONFIG_PROPERTY_TYPE : {
                     type = reader.getElementText();
                     break;
                  }
                  default :
                     throw new ParserException("Unexpected element:" + reader.getLocalName());
               }
               break;
            }
         }
      }
      throw new ParserException("Reached end of xml document unexpectedly");
   }

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

      /** jboss-ra tag name
       *
       */
      JBOSSRA("jboss-ra");

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

}
