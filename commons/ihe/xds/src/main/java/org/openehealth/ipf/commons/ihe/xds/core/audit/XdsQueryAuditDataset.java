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
package org.openehealth.ipf.commons.ihe.xds.core.audit;

import lombok.Getter;
import lombok.Setter;
import org.openehealth.ipf.commons.audit.AuditContext;

/**
 * XDS audit dataset specific for query-related transactions.
 * @author Dmytro Rud
 */
public class XdsQueryAuditDataset extends XdsAuditDataset {
    private static final long serialVersionUID = -5590580053651954919L;

    @Getter @Setter private String queryUuid;
    @Getter @Setter private String homeCommunityId;


    public XdsQueryAuditDataset(AuditContext auditContext, boolean serverSide) {
        super(auditContext, serverSide);
    }
}
