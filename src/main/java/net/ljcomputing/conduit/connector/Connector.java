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
package net.ljcomputing.conduit.connector;

import net.ljcomputing.conduit.exception.ConduitException;
import net.ljcomputing.conduit.model.ConnectorContext;
import net.ljcomputing.conduit.model.ConnectorProtocol;
import net.ljcomputing.conduit.model.DataContext;

/** Interface shared by all data source connectors. */
public interface Connector {
    /**
     * Protocols supported by the Connector implemntation.
     *
     * @return
     */
    ConnectorProtocol supportedProtocol();

    /**
     * Connect to the data source using the given DataContext.
     *
     * @param dataContext
     * @return
     * @throws ConduitException
     */
    ConnectorContext connect(final DataContext dataContext);
}
