/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.commons.ihe.core.atna;

import org.openehealth.ipf.commons.audit.codes.EventOutcomeIndicator;
import org.openehealth.ipf.commons.audit.AuditContext;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes.RFC3881EventOutcomeCodes;

import java.util.Map;
import java.util.function.Consumer;

/**
 * ATNA audit strategy base for transactions. This strategy is accompanied with a
 * dedicated subclass of {@link AuditDataset} containing the data for the audit record.
 *
 * @author Christian Ohr
 * @since 3.1
 */
public interface AuditStrategy<T extends AuditDataset> extends Consumer<T> {

    /**
     * Creates a new audit dataset instance.
     */
    T createAuditDataset(AuditContext auditContext);

    /**
     * Enriches the given audit dataset with transaction-specific
     * contents of the request message and Camel exchange.
     *
     * @param auditDataset audit dataset to be enriched.
     * @param request      {@link Object} representing the request.
     * @param parameters   additional parameters
     */
    T enrichAuditDatasetFromRequest(T auditDataset, Object request, Map<String, Object> parameters);


    /**
     * Enriches the given audit dataset with transaction-specific
     * contents of the response message.
     *
     * @param auditDataset audit dataset to be enriched.
     * @param response     {@link Object} representing the responded resource.
     * @return true if response indicates success, false otherwise
     */
    boolean enrichAuditDatasetFromResponse(T auditDataset, Object response);


    /**
     * Performs the actual ATNA audit.
     *
     * @param auditDataset Collected audit dataset.
     */
    void doAudit(T auditDataset);

    @Override
    default void accept(T t) {
        doAudit(t);
    }

    /**
     * Determines whether the given response finalizes the interaction
     * and thus the ATNA auditing should be finalized as well.
     * <p>
     * Per default always returns <code>true</code>.
     *
     * @param response response in transaction-specific format (POJO, XML string, etc.).
     * @return <code>true</code> when this response finalizes the interaction.
     */
    boolean isAuditableResponse(Object response);

    /**
     * Determines which event outcome corresponds with the provided response POJO
     *
     * @param response POJO
     * @return event outcome code
     */
    EventOutcomeIndicator getEventOutcomeIndicator(Object response);
}
