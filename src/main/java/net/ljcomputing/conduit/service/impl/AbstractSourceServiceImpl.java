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
package net.ljcomputing.conduit.service.impl;

import net.ljcomputing.conduit.connector.Connector;
import net.ljcomputing.conduit.connector.impl.ConnectorFactory;
import net.ljcomputing.conduit.model.ConnectorContext;
import net.ljcomputing.conduit.model.ConnectorProtocol;
import net.ljcomputing.conduit.model.DataContext;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractSourceServiceImpl {
    @Autowired protected ConnectorFactory connectorFactory;

    protected ConnectorContext connect(final DataContext context) {
        final String protocol = context.protocolString();
        final ConnectorProtocol connectorProtocol = ConnectorProtocol.findByProtocol(protocol);
        final Connector connector = connectorFactory.locate(connectorProtocol);
        return connector.connect(context);
    }
}
