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
package org.openehealth.ipf.commons.ihe.xds.core.transform.requests.query;

import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLAdhocQueryRequest;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Hl7v2Based;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Identifiable;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.FindDocumentsQuery;

import static org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter.DOC_ENTRY_PATIENT_ID;
import static org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter.DOC_ENTRY_STATUS;
import static org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter.DOC_ENTRY_TYPE;

/**
 * Transforms between a {@link FindDocumentsQuery} and {@link EbXMLAdhocQueryRequest}.
 * @author Jens Riemschneider
 */
public class FindDocumentsQueryTransformer extends AbstractDocumentsQueryTransformer<FindDocumentsQuery> {

    @Override
    public void toEbXML(FindDocumentsQuery query, EbXMLAdhocQueryRequest ebXML) {
        if (query == null || ebXML == null) {
            return;
        }

        super.toEbXML(query, ebXML);

        QuerySlotHelper slots = new QuerySlotHelper(ebXML);
        slots.fromString(DOC_ENTRY_PATIENT_ID, Hl7v2Based.render(query.getPatientId()));
        slots.fromDocumentEntryType(DOC_ENTRY_TYPE, query.getDocumentEntryTypes());
        slots.fromStatus(DOC_ENTRY_STATUS, query.getStatus());
    }


    public void fromEbXML(FindDocumentsQuery query, EbXMLAdhocQueryRequest ebXML) {
        if (query == null || ebXML == null) {
            return;
        }

        super.fromEbXML(query, ebXML);
        QuerySlotHelper slots = new QuerySlotHelper(ebXML);
        String patientId = slots.toString(DOC_ENTRY_PATIENT_ID);
        query.setPatientId(Hl7v2Based.parse(patientId, Identifiable.class));
        query.setDocumentEntryTypes(slots.toDocumentEntryType(DOC_ENTRY_TYPE));
        query.setStatus(slots.toStatus(DOC_ENTRY_STATUS));
    }
}
