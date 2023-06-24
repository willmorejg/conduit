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

import lombok.AllArgsConstructor;
import lombok.Data;

/** A {@link net.ljcomputing.conduit.model.Dataset Dataset's} column definition. */
@Data
@AllArgsConstructor
public class DatasetColumnDefinition {
    /** The name of the defined column. */
    private final String name;

    /** The datatype {@link java.lang.Class Class} of the defined column. */
    private final Class<?> datatypeClass;

    /** Order of the column in the dataset. */
    private int order;
}
