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

package org.openehealth.ipf.commons.ihe.fhir.iti78;

import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.rest.annotation.*;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.param.DateAndListParam;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.NumberParam;
import ca.uhn.fhir.rest.param.StringAndListParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenAndListParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.IBundleProvider;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.openehealth.ipf.commons.ihe.fhir.AbstractPlainProvider;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Set;

/**
 * Resource Provider for PDQm (ITI-78) for DSTU2
 *
 * @author Christian Ohr
 * @since 3.4
 */
public class Iti78ResourceProvider extends AbstractPlainProvider {

    /**
     * Handles the PDQm Query request
     *
     * @param identifiers         patient identifier search parameter
     * @param active              the active state indicates whether the patient record is active
     * @param family              family name search parameter(s)
     * @param given               family name search parameter(s)
     * @param birthDate           birth date search parameter
     * @param address             address search parameter
     * @param gender              gender search parameter
     * @param resourceId          _id search parameter
     * @param telecom             telecom search parameter
     * @param httpServletRequest  servlet request
     * @param httpServletResponse servlet response
     * @param sortSpec            sort specification
     * @param includeSpec         include specification
     * @return {@link IBundleProvider} instance that manages retrieving patients
     */
    @SuppressWarnings("unused")
    @Search(type = PdqPatient.class)
    public IBundleProvider pdqmSearch(
            @OptionalParam(name = Patient.SP_IDENTIFIER) TokenAndListParam identifiers,
            @OptionalParam(name = Patient.SP_ACTIVE) TokenParam active,
            @OptionalParam(name = Patient.SP_FAMILY) StringAndListParam family,
            @OptionalParam(name = Patient.SP_GIVEN) StringAndListParam given,
            @OptionalParam(name = Patient.SP_BIRTHDATE) DateAndListParam birthDate,
            @OptionalParam(name = Patient.SP_ADDRESS) StringParam address,
            @OptionalParam(name = Patient.SP_ADDRESS_CITY) StringParam city,
            @OptionalParam(name = Patient.SP_ADDRESS_COUNTRY) StringParam country,
            @OptionalParam(name = Patient.SP_ADDRESS_STATE) StringParam state,
            @OptionalParam(name = Patient.SP_ADDRESS_POSTALCODE) StringParam postalCode,
            @OptionalParam(name = Patient.SP_GENDER) TokenParam gender,
            @OptionalParam(name = IAnyResource.SP_RES_ID) TokenParam resourceId,
            @OptionalParam(name = Patient.SP_TELECOM) StringParam telecom,
            @Sort SortSpec sortSpec,
            @IncludeParam Set<Include> includeSpec,

            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse) {

        Iti78SearchParameters searchParameters = Iti78SearchParameters.builder()
                .identifiers(identifiers)
                .active(active)
                .family(family)
                .given(given)
                .birthDate(birthDate)
                .address(address)
                .city(city)
                .country(country)
                .state(state)
                .postalCode(postalCode)
                .gender(gender)
                ._id(resourceId)
                .telecom(telecom)
                .sortSpec(sortSpec)
                .includeSpec(includeSpec)
                .fhirContext(getFhirContext())
                .build();

        // Run down the route
        return requestBundleProvider(null, searchParameters, httpServletRequest, httpServletResponse);
    }


    /**
     * Handles the PDQm Retrieve
     *
     * @param id                  resource ID
     * @param httpServletRequest  servlet request
     * @param httpServletResponse servlet response
     * @return patient resource
     */
    @SuppressWarnings("unused")
    @Read(version = true, type = Patient.class)
    public Patient pdqmRetrieve(
            @IdParam IdType id,
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse) {

        // Run down the route
        return requestResource(id, Patient.class, httpServletRequest, httpServletResponse);
    }

}