/*
 * Copyright 2012 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.xds.rad75;

import org.apache.camel.Endpoint;
import org.openehealth.ipf.commons.ihe.core.atna.AuditStrategy;
import org.openehealth.ipf.commons.ihe.ws.JaxWsClientFactory;
import org.openehealth.ipf.commons.ihe.ws.WsTransactionConfiguration;
import org.openehealth.ipf.commons.ihe.xds.core.audit.XdsRetrieveAuditDataset;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.RetrieveDocumentSetResponseType;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.RetrieveImagingDocumentSetRequestType;
import org.openehealth.ipf.commons.ihe.xds.rad75.Rad75AuditStrategy;
import org.openehealth.ipf.commons.ihe.xds.rad75.Rad75PortType;
import org.openehealth.ipf.platform.camel.ihe.ws.*;
import org.openehealth.ipf.platform.camel.ihe.xds.XdsComponent;
import org.openehealth.ipf.platform.camel.ihe.xds.XdsEndpoint;

import javax.xml.namespace.QName;
import java.util.Map;

/**
 * The Camel component for the RAD-75 transaction.
 */
public class Rad75Component extends XdsComponent<XdsRetrieveAuditDataset> {
    private final static WsTransactionConfiguration WS_CONFIG = new WsTransactionConfiguration(
            new QName("urn:ihe:rad:xdsi-b:2009", "RespondingGateway_Service", "iherad"),
            Rad75PortType.class,
            new QName("urn:ihe:rad:xdsi-b:2009", "RespondingGateway_Binding_Soap12", "iherad"),
            true,
            "wsdl/rad75.wsdl",
            true,
            false,
            false,
            true);

    @Override
    @SuppressWarnings("unchecked") // Required because of base class
    protected Endpoint createEndpoint(String uri, String remaining, Map parameters) throws Exception {
        return new XdsEndpoint<XdsRetrieveAuditDataset>(uri, remaining, this,
                getCustomInterceptors(parameters),
                getFeatures(parameters),
                getSchemaLocations(parameters),
                getProperties(parameters),
                null) {
            @Override
            public AbstractWsProducer getProducer(AbstractWsEndpoint<XdsRetrieveAuditDataset, WsTransactionConfiguration> endpoint,
                                                  JaxWsClientFactory<XdsRetrieveAuditDataset> clientFactory) {
                return new SimpleWsProducer<>(
                        endpoint, clientFactory, RetrieveImagingDocumentSetRequestType.class, RetrieveDocumentSetResponseType.class);
            }

            @Override
            protected <T extends AbstractWebService> T getCustomServiceInstance(AbstractWsEndpoint<XdsRetrieveAuditDataset, WsTransactionConfiguration> endpoint) {
                return (T) new Rad75Service(endpoint);
            }
        };
    }

    @Override
    public WsTransactionConfiguration getWsTransactionConfiguration() {
        return WS_CONFIG;
    }

    @Override
    public AuditStrategy<XdsRetrieveAuditDataset> getClientAuditStrategy() {
        return new Rad75AuditStrategy(false);
    }

    @Override
    public AuditStrategy<XdsRetrieveAuditDataset> getServerAuditStrategy() {
        return new Rad75AuditStrategy(true);
    }

}
