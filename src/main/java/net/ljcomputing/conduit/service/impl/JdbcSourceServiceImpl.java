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

import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import net.ljcomputing.conduit.exception.ConduitException;
import net.ljcomputing.conduit.model.ConnectorContext;
import net.ljcomputing.conduit.model.DataContext;
import net.ljcomputing.conduit.model.DataContextProperties;
import net.ljcomputing.conduit.model.Dataset;
import net.ljcomputing.conduit.model.SourceType;
import net.ljcomputing.conduit.utils.SqlStatementUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/** JDBC Source Service Implementation. */
@Service("jdbc")
@Slf4j
public class JdbcSourceServiceImpl extends AbstractSourceServiceImpl {
    private DataSource dataSource;

    /** {@inheritDoc} */
    @Override
    public SourceType sourceType() {
        return SourceType.JDBC;
    }

    /** {@inheritDoc} */
    @Override
    public void init(final DataContext context) {
        final ConnectorContext connectorContext = connect(context);
        this.dataSource = connectorContext.getDataSource();
    }

    /** {@inheritDoc} */
    @Override
    public List<Map<String, Object>> retrieve(final DataContext context) throws ConduitException {
        try {
            init(context);
            final JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
            return jdbcTemplate.queryForList(context.getQuery());
        } catch (final Exception e) {
            throw new ConduitException(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void insertDataset(final DataContext context, final Dataset dataset)
            throws ConduitException {
        try {
            init(context);

            final String table = context.getProperty(DataContextProperties.TARGET_TABLE);

            final boolean useBindVariables =
                    Boolean.getBoolean(
                            context.getProperty(DataContextProperties.USE_BIND_VARIABLES, "true"));

            final String sql =
                    SqlStatementUtils.buildInsertStatement(
                            table, dataset.getColumnDefinitions(), useBindVariables, "id");
            final JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

            dataset.getRecords().stream()
                    .forEach(
                            r -> {
                                log.debug("record: {}", r.getColumns());
                                log.debug(
                                        "{} {}",
                                        r.getColumns().get(1).getValue(),
                                        r.getColumns().get(2).getValue());
                                jdbcTemplate.update(
                                        sql,
                                        r.getColumns().get(1).getValue(),
                                        r.getColumns().get(2).getValue());
                            });
        } catch (final Exception e) {
            throw new ConduitException(e);
        }
    }
}
