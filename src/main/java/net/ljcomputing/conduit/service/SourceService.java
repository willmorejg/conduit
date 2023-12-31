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
package net.ljcomputing.conduit.service;

import java.util.List;
import java.util.Map;
import net.ljcomputing.conduit.exception.ConduitException;
import net.ljcomputing.conduit.model.DataContext;
import net.ljcomputing.conduit.model.Dataset;
import net.ljcomputing.conduit.model.SourceType;

/** Interface defining a Source Service. */
public interface SourceService {
    /**
     * The Source supported by the service.
     *
     * @return
     */
    SourceType sourceType();

    /**
     * Initialize a data context based service.
     *
     * @param context
     * @throws ConduitException
     */
    void init(DataContext context) throws ConduitException;

    /**
     * Retrieve the source data.
     *
     * @param context data context
     * @return records retrieved from the source of data
     * @throws ConduitException
     */
    List<Map<String, Object>> retrieve(DataContext context) throws ConduitException;

    /**
     * Retrieve the source data.
     *
     * @param context data context
     * @return records retrieved from the source of data
     * @throws ConduitException
     */
    Dataset retrieveDataset(DataContext context) throws ConduitException;

    /**
     * Insert the given {@link net.ljcomputing.conduit.model.Dataset Dataset} into the given data
     * source.
     *
     * @param context
     * @param dataset
     * @throws ConduitException
     */
    void insertDataset(DataContext context, Dataset dataset) throws ConduitException;
}
