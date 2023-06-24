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

/** Enumeration of all additional data context properties available. */
public enum DataContextProperties {
    COLUMNS("columns"),
    DELIMITER("delimiter"),
    TARGET_TABLE("target_table"),
    USE_BIND_VARIABLES("use_bind_variables");

    /** Name of the property. */
    private String property;

    /**
     * Constructor.
     *
     * @param property
     */
    private DataContextProperties(final String property) {
        this.property = property;
    }

    /**
     * The property associated with the data context enumeration.
     *
     * @return
     */
    public String property() {
        return property;
    }
}
