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
package net.ljcomputing.conduit.model;

/** Enum of acceptible / supported transfer protocols. */
public enum ConnectorProtocol {
    JDBC("jdbc"),
    FILE("file"),
    HTTP("http"),
    HTTPS("https");

    /** String representation of the protocol (ex. file:///tmp/file.txt) */
    private String protocol;

    /**
     * Private constructor.
     *
     * @param protocol
     */
    private ConnectorProtocol(final String protocol) {
        this.protocol = protocol;
    }

    /**
     * The String representation of the given protocol.
     *
     * @return
     */
    public String protocol() {
        return protocol;
    }

    /**
     * Find the ConectorProtocol by the given protocol.
     *
     * @param value
     * @return
     */
    public static ConnectorProtocol findByProtocol(final String value) {
        ConnectorProtocol result = null;

        for (final ConnectorProtocol current : values()) {
            if (value.equals(current.protocol())) {
                result = current;
            }
        }

        return result;
    }
}
