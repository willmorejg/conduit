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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.Data;
import lombok.Getter;

/** Model representing a dataset. */
@Data
public class Dataset {
    /** Dataset column definitions. */
    @Getter private final List<DatasetColumnDefinition> columnDefinitions = new ArrayList<>();

    /** Dataset records. */
    @Getter private final List<DatasetRecord> records = new ArrayList<>();

    /** Add a record to the dataset. */
    public void addRecord(final DatasetRecord record) {
        getRecords().add(record);
    }

    /**
     * Return true if the dataset contains the column definition by name.
     *
     * @param datasetColumnDefinition
     * @return
     */
    public boolean hasColumnDefinition(final DatasetColumnDefinition datasetColumnDefinition) {
        final Optional<DatasetColumnDefinition> result =
                columnDefinitions.stream()
                        .filter(el -> datasetColumnDefinition.getName().equals(el.getName()))
                        .findFirst();
        return result.isPresent();
    }

    /**
     * Add the given column definition to the dataset only if the given column definition's name is
     * not already added to the dataset's column definitions.
     *
     * @param datasetColumnDefinition
     */
    public void addColumnDefinition(final DatasetColumnDefinition datasetColumnDefinition) {
        if (!hasColumnDefinition(datasetColumnDefinition)) {
            columnDefinitions.add(datasetColumnDefinition);
        }
    }
}
