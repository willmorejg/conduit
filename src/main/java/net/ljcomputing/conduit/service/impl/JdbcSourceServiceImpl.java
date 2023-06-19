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
import net.ljcomputing.conduit.exception.ConduitException;
import net.ljcomputing.conduit.model.ConnectorContext;
import net.ljcomputing.conduit.model.DataContext;
import net.ljcomputing.conduit.service.SourceService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/** JDBC Source Service Implementation. */
@Service("jdbc")
public class JdbcSourceServiceImpl extends AbstractSourceServiceImpl implements SourceService {
    private DataSource dataSource;

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
}
