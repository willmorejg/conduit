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
package net.ljcomputing.conduit.utils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import net.ljcomputing.conduit.model.DatasetColumnDefinition;

/** SQL statement utilities. */
public enum SqlStatementUtils {
    INSTANCE;

    public static String buildInsertStatement(
            final String datasetName,
            final List<DatasetColumnDefinition> columnDefinitions,
            final String... excludedColumns) {
        return buildInsertStatement(datasetName, columnDefinitions, false, excludedColumns);
    }

    public static String buildInsertStatement(
            final String datasetName,
            final List<DatasetColumnDefinition> columnDefinitions,
            final boolean useBindVariables,
            final String... excludedColumns) {
        final List<String> exclude = Arrays.asList(excludedColumns);
        exclude.replaceAll(String::toUpperCase);

        final List<DatasetColumnDefinition> filteredColumnDefinitions =
                columnDefinitions.stream()
                        .filter(d -> !exclude.contains(d.getName().toUpperCase()))
                        .collect(Collectors.toList());

        final String columnNames =
                filteredColumnDefinitions.stream()
                        .map(el -> el.getName())
                        .collect(Collectors.joining(", "));

        String valueStatement =
                filteredColumnDefinitions.stream().map(el -> "?").collect(Collectors.joining(", "));

        if (useBindVariables) {
            valueStatement =
                    filteredColumnDefinitions.stream()
                            .map(el -> el.sqlBindVariable())
                            .collect(Collectors.joining(", "));
        }

        final StringBuilder builder = new StringBuilder("insert into ");

        builder.append(datasetName)
                .append(" (")
                .append(columnNames)
                .append(") values (")
                .append(valueStatement)
                .append(")");
        ;

        return builder.toString();
    }
}
