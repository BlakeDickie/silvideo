/**
 * Copyright (C) 2012-2014 Blake Dickie
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package net.landora.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 *
 * @author bdickie
 */
public class XMLUtilities {

    private static final Logger log = LoggerFactory.getLogger( XMLUtilities.class );

    public static final String xmlnsURI = "http://www.w3.org/2000/xmlns/";
    public static final String schemaInstanceNS = "http://www.w3.org/2001/XMLSchema-instance";
    public static final String xsdBase = "http://www.optimedsoftware.com/";

    /**
     * ********************* PUBLIC METHODS *******************
     */
    /**
     * Converts a DOM Tree to a String.
     *
     * @param xml The DOM document to convert.
     *
     * @return A string containing the XML if successful, null otherwise.
     */
    public static String xmlToString( Document xml ) {
        try {
            TransformerFactory f = TransformerFactory.newInstance();
            StringWriter writer = new StringWriter();
            Transformer t = f.newTransformer();
            DOMSource src = new DOMSource( xml );
            StreamResult result = new StreamResult( writer );
            t.transform( src, result );
            return writer.toString();
        } catch ( TransformerException e ) {
            return null;
        }
    }

    /**
     * Converts a DOM Tree to a String.
     *
     * @param xml  The DOM document to convert.
     * @param file The file to save to.
     *
     * @return true if there were no errors saving to disk.
     */
    public static boolean xmlToFile( Document xml, File file ) {
        try {
            TransformerFactory f = TransformerFactory.newInstance();
            OutputStream writer = new FileOutputStream( file );
            Transformer t = f.newTransformer();
            DOMSource src = new DOMSource( xml );
            StreamResult result = new StreamResult( writer );
            t.transform( src, result );
            writer.close();
            return true;
        } catch ( Exception e ) {
            return false;
        }
    }

    /**
     * Creates a new DOM Tree with a root element containing the schema
     * attributes.
     *
     * @param rootName        The name of the root element.
     * @param namespaceURI    The uri of the namespace of the document.
     * @param namespacePrefix The prefix to use for the namespace (ie. the part
     *                        before the ':' in the root element.
     * @param namespaceXSD    The name of the xsd file used to validate the file.
     *                        Should be given relative to xsdBase.
     */
    public static Document newXMLTree( String rootName, String namespaceURI, String namespacePrefix, String xsdBase, String namespaceXSD ) {
        try {
            DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();

            DocumentBuilder b = f.newDocumentBuilder();
            Document doc = b.newDocument();
            Element rootNode;
            if ( namespaceURI == null ) {
                rootNode = doc.createElement( rootName );
            } else {
                rootNode = doc.createElementNS( namespaceURI, rootName );

                if ( namespacePrefix != null ) {
                    rootNode.setPrefix( namespacePrefix );
                }

                rootNode.setAttributeNS( xmlnsURI, "xmlns:xsi", schemaInstanceNS );
                if ( namespacePrefix == null || namespacePrefix.isEmpty() ) {
                    rootNode.setAttributeNS( xmlnsURI, "xmlns", namespaceURI );
                } else {
                    rootNode.setAttributeNS( xmlnsURI, "xmlns:" + namespacePrefix, namespaceURI );
                }

                if ( xsdBase != null && namespaceXSD != null ) {
                    rootNode.setAttributeNS( schemaInstanceNS, "xsi:schemaLocation",
                                             namespaceURI + " " + xsdBase + namespaceXSD );
                }
            }
            doc.appendChild( rootNode );
            return doc;
        } catch ( Exception e ) {
            return null;
        }
    }

    /**
     * Creates a new DOM Tree with a root element containing the schema
     * attributes.
     *
     * @param rootName        The name of the root element.
     * @param namespaceURI    The uri of the namespace of the document.
     * @param namespacePrefix The prefix to use for the namespace (ie. the part
     *                        before the ':' in the root element.
     * @param namespaceXSD    The name of the xsd file used to validate the file.
     *                        Should be given relative to xsdBase.
     */
    public static Document newXMLTree( String rootName ) {
        return newXMLTree( rootName, null, null, null, null );
    }

    public static void appendCopy( Element dstParent, Element srcChild, Document dstDoc ) {
        convert( srcChild, dstParent, dstDoc );
    }

    /**
     * Applies an XSLT.
     *
     * @param xml          The document to translate.
     * @param xsltFilename The filename of the xsl file to use.
     * @param params       A map of parameters to pass to the transformation. if null
     *                     is passed none are used.
     *
     * @return The string representing of the result of the transformation.
     */
    public static String applyXSLT( Document xml, String xsltFilename, Map params ) {
        return applyXSLT( new DOMSource( xml ), xsltFilename, params );
    }

    /**
     * Applies an XSLT.
     *
     * @param xml          The inputstream to read the xml from.
     * @param xsltFilename The filename of the xsl file to use.
     * @param params       A map of parameters to pass to the transformation. if null
     *                     is passed none are used.
     *
     * @return The string representing of the result of the transformation.
     */
    public static String applyXSLT( InputStream in, String xsltFilename, Map params ) {
        StreamSource src = new StreamSource( in );
        return applyXSLT( src, xsltFilename, params );
    }

    /**
     * Applies an XSLT.
     *
     * @param xml          The soucrce to translate.
     * @param xsltFilename The filename of the xsl file to use.
     * @param params       A map of parameters to pass to the transformation. if null
     *                     is passed none are used.
     *
     * @return The string representing of the result of the transformation.
     */
    public static String applyXSLT( Source xml, String xsltFilename, Map params ) {
        try {
            String fullPath = xsltFilename;
            File xslt = new File( fullPath );
            TransformerFactory f = TransformerFactory.newInstance();
            StreamSource xsltSrc = new StreamSource( xslt );
            StringWriter out = new StringWriter();
            StreamResult res = new StreamResult( out );
            Transformer t = f.newTransformer( xsltSrc );

            if ( params != null ) {
                Iterator i = params.keySet().iterator();
                while ( i.hasNext() ) {
                    Object obj = i.next();
                    t.setParameter( obj.toString(), params.get( obj ) );
                }
            }
            t.transform( xml, res );

            return out.toString();
        } catch ( Exception e ) {
            return null;
        }
    }

    public static String applyXSLT( Source xml, URL xsltFilename, Map params ) {
        try {
            TransformerFactory f = TransformerFactory.newInstance();
            StreamSource xsltSrc = new StreamSource( xsltFilename.openConnection().getInputStream() );
            StringWriter out = new StringWriter();
            StreamResult res = new StreamResult( out );
            Transformer t = f.newTransformer( xsltSrc );

            if ( params != null ) {
                Iterator i = params.keySet().iterator();
                while ( i.hasNext() ) {
                    Object obj = i.next();
                    t.setParameter( obj.toString(), params.get( obj ) );
                }
            }
            t.transform( xml, res );

            return out.toString();
        } catch ( Exception e ) {
            return null;
        }
    }

    /**
     * Gets the text value contained in an element.
     *
     * @param node The Element to get the text value of.
     *
     * @return The text in the element.
     */
    public static String getTextValue( Element node ) {
        if ( node == null ) {
            return null;
        }
        NodeList nodes = node.getChildNodes();
        for ( int i = 0; i < nodes.getLength(); i++ ) {
            Node n = nodes.item( i );
            if ( n.getNodeType() == n.TEXT_NODE ) {
                return n.getNodeValue();
            }
        }
        return null;
    }

    /**
     * Trys to find a child element in the given parent element.
     *
     * @param parent The Element to search in.
     * @param name   The name of the element to search for.
     *
     * @return The Element if found, null otherwise.
     */
    public static Element findElement( Element parent, String name ) {
        NodeList l = parent.getChildNodes();
        for ( int i = 0; i < l.getLength(); i++ ) {
            Node n = l.item( i );
            if ( n.getNodeType() == n.ELEMENT_NODE ) {
                Element e = (Element) n;
                if ( e.getNodeName().equals( name ) ) {
                    return e;
                }
            }
        }
        return null;
    }

    /**
     * Trys to find a child element in the given parent element where the
     * child's name attribute is the given value.
     *
     * @param parent The Element to search in.
     * @param name   The name attribute of the element to search for.
     *
     * @return The Element if found, null otherwise.
     */
    public static Element findElementWithNameAttribute( Element parent, String name ) {
        NodeList l = parent.getChildNodes();
        for ( int i = 0; i < l.getLength(); i++ ) {
            Node n = l.item( i );
            if ( n.getNodeType() == n.ELEMENT_NODE ) {
                Element e = (Element) n;
                if ( e.getAttribute( "name" ).equals( name ) ) {
                    return e;
                }
            }
        }
        return null;
    }

    /**
     * Gets the text in a child tag.
     *
     * @param The parent of the child to look at.
     * @param The name attribute of the child element to get the text of.
     */
    public static String getTextValueOfChildWithNameAttribute( Element parent, String childName ) {
        String result = getTextValue( findElementWithNameAttribute( parent, childName ) );
        if ( result == null ) {
            return "";
        }
        return result;
    }

    /**
     * Gets the text in a child tag.
     *
     * @param The parent of the child to look at.
     * @param The tag name of the child to get the text of.
     */
    public static String getTextValueOfChild( Element parent, String childName ) {
        String result = getTextValue( findElement( parent, childName ) );
        if ( result == null ) {
            return "";
        }
        return result;
    }

    public static Document extractDocument( Element baseNode ) {
        try {
            DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();

            DocumentBuilder b = f.newDocumentBuilder();
            Document doc = b.newDocument();
            convert( baseNode, doc, doc );
            return doc;
        } catch ( Exception e ) {
            log.error( "Unable to extract document from element.", e );
            return null;
        }
    }

    private static void convert( Node toCopy, Node saveTo, Document doc ) {
        Node newNode;
        switch ( toCopy.getNodeType() ) {
            case Node.ELEMENT_NODE:
                Element newElement = doc.createElementNS( toCopy.getNamespaceURI(), toCopy.getNodeName() );
                newNode = newElement;
                Element baseElement = (Element) toCopy;
                NamedNodeMap children = baseElement.getAttributes();
                for ( int i = 0; i < children.getLength(); i++ ) {
                    convertAttribute( (Attr) children.item( i ), newElement, doc );
                }
                break;
            case Node.TEXT_NODE:
                newNode = doc.createTextNode( toCopy.getTextContent() );
                break;
            default:
                newNode = null;
        }
        if ( newNode != null ) {
            NodeList children = toCopy.getChildNodes();
            for ( int i = 0; i < children.getLength(); i++ ) {
                convert( children.item( i ), newNode, doc );
            }

            saveTo.appendChild( newNode );
        }
    }

    private static void convertAttribute( Attr toCopy, Element saveTo, Document doc ) {
        if ( toCopy.getNamespaceURI() == null || !toCopy.getNamespaceURI().equals( xmlnsURI ) ) {
            saveTo.setAttributeNS( toCopy.getNamespaceURI(), toCopy.getNodeName(), toCopy.getValue() );
        }
    }

    public static String nextString( XMLStreamReader reader ) throws XMLStreamException {
        int type;
        StringBuilder result = new StringBuilder();
        while ( ( type = reader.next() ) != XMLStreamReader.END_ELEMENT ) {
            switch ( type ) {
                case XMLStreamReader.START_ELEMENT:
                    throw new IllegalStateException( "Found start element parsing text." );

                case XMLStreamReader.CDATA:
                case XMLStreamReader.SPACE:
                case XMLStreamReader.CHARACTERS:
                    result.append( reader.getText() );
            }
        }
        return result.toString();
    }

    public static void ignoreTag( XMLStreamReader reader ) throws XMLStreamException {
        int type;
        while ( ( type = reader.next() ) != XMLStreamReader.END_ELEMENT ) {
            switch ( type ) {
                case XMLStreamReader.START_ELEMENT:
                    ignoreTag( reader );

                case XMLStreamReader.CDATA:
                case XMLStreamReader.SPACE:
                case XMLStreamReader.CHARACTERS:
                default:
            }
        }
    }
}
