/*
Licensed to the Apache Software Foundation (ASF) under one
or more contributor license agreements.  See the NOTICE file
distributed with this work for additional information
regarding copyright ownership.  The ASF licenses this file
to you under the Apache License, Version 2.0 (the
"License"); you may not use this file except in compliance
with the License.  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing,
software distributed under the License is distributed on an
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
KIND, either express or implied.  See the License for the
specific language governing permissions and limitations
under the License.

James G Willmore - LJ Computing - (C) 2023
*/
package net.ljcomputing.conduit.connector.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import net.ljcomputing.conduit.model.SourceType;
import net.ljcomputing.conduit.service.SourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** Source service factory. */
@Component
@Slf4j
public class SourceServiceFactory {
    /**
     * Map of available {@link net.ljcomputing.conduit.service.SourceService source services} by
     * {@link net.ljcomputing.conduit.model.SourceType supported source type}.
     */
    private final Map<SourceType, SourceService> sourceServiceTypes = new HashMap<>();

    /**
     * Private constructor that populates the factory with all availabe source service
     * implementations.
     *
     * @param implemntations
     */
    private SourceServiceFactory(@Autowired final List<SourceService> implemntations) {
        implemntations.forEach(
                c -> {
                    sourceServiceTypes.put(c.sourceType(), c);
                    log.info("loaded source service for {} type", c.sourceType());
                });
    }

    /**
     * Locate the {@link net.ljcomputing.conduit.service.SourceService source service} using the
     * given {@link net.ljcomputing.conduit.model.SourceType supported source type}.
     *
     * @param type
     * @return
     */
    public SourceService locate(final SourceType type) {
        return sourceServiceTypes.get(type);
    }
}
