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
package org.openehealth.ipf.commons.ihe.xds.iti14;

import java.net.InetAddress;

import org.openehealth.ipf.commons.ihe.core.atna.AuditorManager;
import org.openehealth.ipf.commons.ihe.xds.core.audit.XdsSubmitAuditDataset;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes;

/**
 * Client audit strategy for ITI-14.
 * @author Dmytro Rud
 */
public class Iti14ClientAuditStrategy extends Iti14AuditStrategy {

    public Iti14ClientAuditStrategy() {
        super(false);
    }

    @Override
    public void doAudit(XdsSubmitAuditDataset auditDataset) {
        AuditorManager.getRepositoryAuditor().auditRegisterDocumentSetEvent(
                auditDataset.getEventOutcomeCode(),
                // InetAddress.getLocalHost().getHostAddress(),
                auditDataset.getClientIpAddress(),
                auditDataset.getUserName(),
                auditDataset.getServiceEndpointUrl(),
                auditDataset.getSubmissionSetUuid(),
                auditDataset.getPatientId());
    }

}
