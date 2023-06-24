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
import net.ljcomputing.conduit.connector.Connector;
import net.ljcomputing.conduit.model.ConnectorProtocol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** Connector factory. */
@Component
@Slf4j
public class ConnectorFactory {
    /**
     * Map of available {@link net.ljcomputing.conduit.connector.Connector connectors} by {@link
     * net.ljcomputing.conduit.model.ConnectorProtocol connector protocol}.
     */
    private final Map<ConnectorProtocol, Connector> connectors = new HashMap<>();

    /**
     * Private constructor that populates the factory with all availabe connector implementations.
     *
     * @param implemntations
     */
    private ConnectorFactory(@Autowired final List<Connector> implemntations) {
        implemntations.forEach(
                c -> {
                    connectors.put(c.supportedProtocol(), c);
                    log.info("loaded connector for {} protocol", c.supportedProtocol());
                });
    }

    /**
     * Locate the {@link net.ljcomputing.conduit.connector.Connector connector} using the given
     * {@link net.ljcomputing.conduit.model.ConnectorProtocol connector protocol}.
     *
     * @param protocol
     * @return
     */
    public Connector locate(final ConnectorProtocol protocol) {
        return connectors.get(protocol);
    }
}
