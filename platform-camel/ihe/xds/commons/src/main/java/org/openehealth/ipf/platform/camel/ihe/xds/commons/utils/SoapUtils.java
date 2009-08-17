/*
 * Copyright 2009 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *     
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.platform.camel.ihe.xds.commons.utils;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Generic constants and subroutines for SOAP/XML processing.
 * 
 * @author Dmytro Rud
 */
public abstract class SoapUtils {
    private static final transient Log LOG = LogFactory.getLog(SoapUtils.class);
    
    private SoapUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /* --------------------------------------- */
    /*      XML/SOAP processing constants      */
    /* --------------------------------------- */
    
    /**
     * Set of URIs corresponding to supported WS-Addressing specification versions. 
     */
    public static final Set<String> WS_ADDRESSING_NS_URIS;

    /**
     * Set of URIs corresponding to supported WS-Security specification versions. 
     */
    public static final Set<String> WS_SECURITY_NS_URIS;

    /**
     * Set of URIs corresponding to supported SOAP versions. 
     */
    public static final Set<String> SOAP_NS_URIS;
    
    static {
        WS_ADDRESSING_NS_URIS = new HashSet<String>();
        WS_ADDRESSING_NS_URIS.add("http://schemas.xmlsoap.org/ws/2004/08/addressing");
        WS_ADDRESSING_NS_URIS.add("http://www.w3.org/2005/08/addressing");                                 
        WS_ADDRESSING_NS_URIS.add("http://www.w3.org/2006/05/addressing");
        
        WS_SECURITY_NS_URIS = new HashSet<String>();
        WS_SECURITY_NS_URIS.add("http://schemas.xmlsoap.org/ws/2002/07/secext");
        WS_SECURITY_NS_URIS.add("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd");
        
        SOAP_NS_URIS = new HashSet<String>();
        SOAP_NS_URIS.add("http://schemas.xmlsoap.org/soap/envelope/");      // SOAP 1.1
        SOAP_NS_URIS.add("http://www.w3.org/2003/05/soap-envelope");        // SOAP 1.2
    }


    
    /* ----------------------- */
    /*      XML utilities      */ 
    /* ----------------------- */

    /**
     * Searches for the first sub-element of the given XML element, which has
     * the given local name and whose namespace belongs to the given set. 
     * 
     * @param root            
     *      an XML element whose children will be iterated, null values are allowed  
     * @param nsUris          
     *      a set of namespace URIs the wanted element can belong to
     * @param wantedLocalName 
     *      local name of the wanted element
     * @return
     *      corresponding child element or <code>null</code> when none found    
     */
    public static Element getElementNS(
            Element root, 
            Set<String> nsUris, 
            String wantedLocalName) 
    {
        if(root == null) {
            return null;
        }

        Node node = root.getFirstChild();
        while(node != null) {
            if((node instanceof Element) &&
               nsUris.contains(node.getNamespaceURI()) && 
               node.getLocalName().equals(wantedLocalName)) 
            {
                return (Element)node;
            }
            
            node = node.getNextSibling(); 
        }
            
        return null;
    }    
    
    
    /**
     * Recursively searches for an XML element.  
     * <p>
     * In the first step of the recursion, any namespace URI from the given 
     * set will be accepted.  After the first (the topmost) element has been 
     * found, its namespace URI is the only acceptable one. 
     *   
     * @param root            
     *      an XML element whose children will be iterated, null values are allowed  
     * @param nsUris          
     *      a set of namespace URIs the wanted elements can belong to
     * @param wantedLocalNamesChain 
     *      a chain of local element names, e.g. <code>{"outer", "middle", "inner"}</code>    
     */
    public static Element getDeepElementNS(
            Element root, 
            Set<String> nsUris, 
            String[] wantedLocalNamesChain) 
    {
        Element element = root;
        
        for(String name : wantedLocalNamesChain) {
            element = getElementNS(element, nsUris, name);
            if(element == null) {
                return null;
            } 
            if(nsUris.size() > 1) {
                nsUris = Collections.singleton(element.getNamespaceURI());
            }
        }
        
        return element;
    }
    
    
    /**
     * Extracts the proper body (for example, a Query) from the 
     * SOAP envelope, both represented as Strings.
     * <p>
     * Does really suppose that the given String contains  
     * a SOAP envelope and not check it thoroughly.
     * 
     * @param soapEnvelope
     *      The SOAP Envelope (XML document) as String.
     * @return
     *      Extracted SOAP Body contents as String, or the original 
     *      parameter when it does not seem to represent a valid 
     *      SOAP envelope.      
     */
    public static String extractSoapBody(String soapEnvelope) {
        try {
            /*   
             * We search for following positions (variables posXX):
             * 
             *    <S:Envelope><S:Body>the required information</S:Body><S:Envelope>
             *                3      4                        1  2    5
             *         
             *                                       
             *    <Envelope><Body>the required information</Body><Envelope>
             *   2                                        1     5 
             * 
             */ 
            int pos1, pos2, pos3, pos4, pos5;
            pos1 = soapEnvelope.lastIndexOf("<");
            pos1 = soapEnvelope.lastIndexOf("<", pos1 - 1);
            pos2 = soapEnvelope.indexOf(":", pos1);
            pos5 = soapEnvelope.indexOf(">", pos1);
            String soapPrefix = ((pos2 == -1) || (pos5 < pos2)) ? 
                    "" : soapEnvelope.substring(pos1 + 2, pos2) + ":";
            String bodyElementStart = new StringBuilder()
                .append('<')
                .append(soapPrefix)
                .append("Body")
                .toString(); 
            pos3 = soapEnvelope.indexOf(bodyElementStart);
            pos4 = pos3 + bodyElementStart.length();
            String body = soapEnvelope.substring(pos4 + 1, pos1);
            return body;
            
        } catch(Exception e) {
            LOG.error("Invalid contents, probably not a SOAP Envelope in the parameter", e);
            return soapEnvelope;
        }
    }
    
}
