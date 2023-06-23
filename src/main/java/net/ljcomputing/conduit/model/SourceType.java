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
public enum SourceType {
    JDBC("jdbc"),
    CSV("csv"),
    JSON("json");

    /** Source type (ex. JDBC, CSV, JSON, etc.) */
    private String type;

    /**
     * Private constructor.
     *
     * @param type
     */
    private SourceType(final String type) {
        this.type = type;
    }

    /**
     * The String representation of the given type.
     *
     * @return
     */
    public String type() {
        return type;
    }

    /**
     * Find the Source Type by the given type.
     *
     * @param value
     * @return
     */
    public static SourceType findByProtocol(final String value) {
        SourceType result = null;

        for (final SourceType current : values()) {
            if (value.equals(current.type())) {
                result = current;
            }
        }

        return result;
    }
}
