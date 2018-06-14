/*
 * Copyright 2018 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.xacml20.stub;

import org.openehealth.ipf.commons.ihe.xacml20.stub.ehealthswiss.UnknownPolicySetId;

import javax.xml.ws.WebFault;


/**
 * This class was generated by Apache CXF 3.1.10
 * 2017-05-29T14:39:29.875+02:00
 * Generated source version: 3.1.10
 */

@WebFault(name = "UnknownPolicySetId", targetNamespace = "urn:e-health-suisse:2015:policy-administration")
public class UnknownPolicySetIdFaultMessage extends Exception {
    
    private UnknownPolicySetId unknownPolicySetId;

    public UnknownPolicySetIdFaultMessage() {
        super();
    }
    
    public UnknownPolicySetIdFaultMessage(String message) {
        super(message);
    }
    
    public UnknownPolicySetIdFaultMessage(String message, Throwable cause) {
        super(message, cause);
    }

    public UnknownPolicySetIdFaultMessage(String message, UnknownPolicySetId unknownPolicySetId) {
        super(message);
        this.unknownPolicySetId = unknownPolicySetId;
    }

    public UnknownPolicySetIdFaultMessage(String message, UnknownPolicySetId unknownPolicySetId, Throwable cause) {
        super(message, cause);
        this.unknownPolicySetId = unknownPolicySetId;
    }

    public UnknownPolicySetId getFaultInfo() {
        return this.unknownPolicySetId;
    }
}
