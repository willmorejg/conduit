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

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import net.ljcomputing.conduit.connector.Connector;
import net.ljcomputing.conduit.connector.impl.ConnectorFactory;
import net.ljcomputing.conduit.exception.ConduitException;
import net.ljcomputing.conduit.model.ConnectorContext;
import net.ljcomputing.conduit.model.ConnectorProtocol;
import net.ljcomputing.conduit.model.DataContext;
import net.ljcomputing.conduit.model.Dataset;
import net.ljcomputing.conduit.model.DatasetColumnDefinition;
import net.ljcomputing.conduit.model.DatasetRecord;
import net.ljcomputing.conduit.model.DatasetRecordColumn;
import net.ljcomputing.conduit.service.SourceService;
import org.springframework.beans.factory.annotation.Autowired;

/** Abstract dataset source service implementation. */
public abstract class AbstractSourceServiceImpl implements SourceService {
    /** The {@link net.ljcomputing.conduit.connector.impl.ConnectorFactory ConnectorFactory}. */
    @Autowired protected ConnectorFactory connectorFactory;

    /**
     * Connect to a datasource using the given {@link net.ljcomputing.conduit.model.DataContext
     * context}.
     *
     * @param context
     * @return
     */
    protected ConnectorContext connect(final DataContext context) {
        final String protocol = context.protocolString();
        final ConnectorProtocol connectorProtocol = ConnectorProtocol.findByProtocol(protocol);
        final Connector connector = connectorFactory.locate(connectorProtocol);
        return connector.connect(context);
    }

    /**
     * Convert the given {@link java.util.Map map} to a {@link
     * net.ljcomputing.conduit.model.DatasetRecord dataset record}.
     *
     * @param map
     * @return
     */
    protected DatasetRecord convertMapToRecord(final Map<String, Object> map) {
        final DatasetRecord record = new DatasetRecord();

        map.entrySet().stream()
                .forEach(
                        el -> {
                            record.addColumn(new DatasetRecordColumn(el.getKey(), el.getValue()));
                        });
        return record;
    }

    /**
     * Add {@link net.ljcomputing.conduit.model.DatasetColumnDefinition column definitions} to the
     * given {@link net.ljcomputing.conduit.model.Dataset dataset} using the given {@link
     * java.util.Map map}.
     *
     * @param map
     * @param dataset
     */
    protected void addColumnDefinitionsToDataset(
            final Map<String, Object> map, final Dataset dataset) {
        final AtomicInteger count = new AtomicInteger(0);

        map.entrySet().stream()
                .forEach(
                        el -> {
                            dataset.addColumnDefinition(
                                    new DatasetColumnDefinition(
                                            el.getKey(),
                                            el.getValue().getClass(),
                                            count.incrementAndGet()));
                        });
    }

    /**
     * Add the given {@link java.util.Map map} to the given {@link
     * net.ljcomputing.conduit.model.Dataset dataset}
     *
     * @param map
     * @param dataset
     */
    protected void addMapToDataset(final Map<String, Object> map, final Dataset dataset) {
        dataset.addRecord(convertMapToRecord(map));
        addColumnDefinitionsToDataset(map, dataset);
    }

    /** {@inheritDoc} */
    @Override
    public Dataset retrieveDataset(final DataContext context) throws ConduitException {
        try {
            final Dataset dataset = new Dataset();

            retrieve(context)
                    .forEach(
                            row -> {
                                addMapToDataset(row, dataset);
                            });

            return dataset;
        } catch (final Exception e) {
            throw new ConduitException(e);
        }
    }
}
