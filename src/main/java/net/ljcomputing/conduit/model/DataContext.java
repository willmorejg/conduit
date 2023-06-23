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

import java.util.Properties;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NonNull;

/**
 * Data context model. Contains the definition of a given data context (ex. url, user, password,
 * etc.)
 */
@Data
@Builder(builderMethodName = "hiddenBuilder")
public class DataContext {
    @NonNull private final SourceType sourceType;
    @NonNull private final String url;
    private String user;
    private String password;
    private String driverClassName;
    private String query;
    @Getter private final Properties properties = new Properties();

    public static DataContextBuilder init(final SourceType sourceType, final String url) {
        return hiddenBuilder().sourceType(sourceType).url(url);
    }

    /**
     * Check for additional context properties.
     *
     * @return true if context has additional properties
     */
    public boolean hasAdditionalProperties() {
        return !properties.isEmpty();
    }

    public String protocolString() {
        return getUrl().substring(0, getUrl().indexOf(":"));
    }
}
