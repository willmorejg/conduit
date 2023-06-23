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

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import net.ljcomputing.conduit.exception.ConduitException;
import net.ljcomputing.conduit.model.ConnectorContext;
import net.ljcomputing.conduit.model.DataContext;
import net.ljcomputing.conduit.model.DataContextProperties;
import net.ljcomputing.conduit.model.SourceType;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

/** CSV Source Service Implementation. */
@Service("csv")
@Slf4j
public class CsvSourceServiceImpl extends AbstractSourceServiceImpl {
    private CsvSchema schema;
    private CsvMapper mapper;
    private Resource resource;

    /** {@inheritDoc} */
    @Override
    public SourceType sourceType() {
        return SourceType.CSV;
    }

    /** {@inheritDoc} */
    @Override
    public void init(final DataContext context) {
        if (columnsPropertyPresent(context)) {
            final String[] columns = getColumns(context);
            CsvSchema.Builder builder = CsvSchema.builder();

            for (final String el : columns) {
                builder.addColumn(el);
            }

            schema = builder.build();
        } else {
            schema = CsvSchema.emptySchema().withHeader();
        }

        final char columnSeparator =
                context.getProperties()
                        .getProperty(DataContextProperties.DELIMITER.property(), ",")
                        .toCharArray()[0];
        schema = schema.withColumnSeparator(columnSeparator);

        loadResource(context);
    }

    private boolean columnsPropertyPresent(final DataContext context) {
        return context.hasAdditionalProperties()
                && !context.getProperties()
                        .getProperty(DataContextProperties.COLUMNS.property(), "")
                        .isEmpty();
    }

    private String[] getColumns(final DataContext context) {
        final String columnsProperty =
                context.getProperties().getProperty(DataContextProperties.COLUMNS.property());
        final String[] columns = columnsProperty.split(",");
        Arrays.parallelSetAll(columns, (i) -> columns[i].trim());
        return columns;
    }

    private void loadResource(final DataContext context) {
        final ConnectorContext connectorContext = connect(context);
        this.resource = connectorContext.getResource();
    }

    /** {@inheritDoc} */
    @Override
    public List<Map<String, Object>> retrieve(final DataContext context) throws ConduitException {
        try {
            init(context);
            mapper = new CsvMapper();
            final List<Map<String, Object>> records = new ArrayList<>();
            MappingIterator<Map<String, Object>> it =
                    mapper.readerFor(Map.class).with(schema).readValues(resource.getInputStream());
            while (it.hasNext()) {
                final Map<String, Object> rowAsMap = it.next();
                log.debug("row: {}", rowAsMap);
                records.add(rowAsMap);
            }

            return records;
        } catch (final Exception e) {
            throw new ConduitException(e);
        }
    }
}
