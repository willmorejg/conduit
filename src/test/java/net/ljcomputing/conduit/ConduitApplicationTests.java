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
package net.ljcomputing.conduit;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import net.ljcomputing.conduit.connector.impl.ConnectorFactory;
import net.ljcomputing.conduit.connector.impl.SourceServiceFactory;
import net.ljcomputing.conduit.exception.ConduitException;
import net.ljcomputing.conduit.model.ConnectorProtocol;
import net.ljcomputing.conduit.model.DataContext;
import net.ljcomputing.conduit.model.DataContextProperties;
import net.ljcomputing.conduit.model.Dataset;
import net.ljcomputing.conduit.model.DatasetColumnDefinition;
import net.ljcomputing.conduit.model.SourceType;
import net.ljcomputing.conduit.service.SourceService;
import net.ljcomputing.conduit.utils.SqlStatementUtils;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/** JUnit Tests. */
@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
@ActiveProfiles("test")
class ConduitApplicationTests {
    private static final Logger log = LoggerFactory.getLogger(ConduitApplicationTests.class);

    @Autowired private ConnectorFactory connectorFactory;

    @Autowired private SourceServiceFactory sourceServiceFactory;

    /** Test to make sure application starts correctly. */
    @Test
    @Order(1)
    void contextLoads() {}

    /** Test connector factory. */
    @Test
    @Order(2)
    void connectorFactoryTest() {
        assertNotNull(connectorFactory.locate(ConnectorProtocol.JDBC));
        assertNotNull(sourceServiceFactory.locate(SourceType.JDBC));
    }

    /** Test connector factory. */
    @Test
    @Order(3)
    void sqlUtilitiesTest() {
        final List<DatasetColumnDefinition> columnDefinitions = new ArrayList<>();
        int order = 0;

        columnDefinitions.add(new DatasetColumnDefinition("id", String.class, ++order));
        columnDefinitions.add(new DatasetColumnDefinition("full_name", String.class, ++order));
        columnDefinitions.add(new DatasetColumnDefinition("birth_date", Date.class, ++order));

        String sql = SqlStatementUtils.buildInsertStatement("foo", columnDefinitions, true, "id");
        log.debug("sql: {}", sql);

        sql = SqlStatementUtils.buildInsertStatement("foo", columnDefinitions, false, "id");
        log.debug("sql: {}", sql);
    }

    /** Test JDBC data source retrieve. */
    @Test
    @Order(10)
    void retrieveJdbcSource() {
        final DataContext context =
                DataContext.init(SourceType.JDBC, "jdbc:postgresql://localhost:5432/insurance")
                        .driverClassName("org.postgresql.Driver")
                        .user("jim")
                        .query("select id, given_name || ' ' || surname as \"name\" from insured")
                        .build();

        context.getProperties().setProperty("foo", "bar");

        try {
            log.debug("hasAdditionalProperties: {}", context.hasAdditionalProperties());
            log.debug("foo: {}", context.getProperties().getProperty("foo", ""));

            final SourceService sourceService =
                    sourceServiceFactory.locate(context.getSourceType());
            final List<Map<String, Object>> data = sourceService.retrieve(context);
            log.debug("data: {}", data);
        } catch (ConduitException e) {
            log.error("Test failed: ", e);
        }
    }

    /** Test CSV data source retrieve. */
    @Test
    @Order(11)
    void retrieveCsvSource() {
        final DataContext context =
                DataContext.init(
                                SourceType.CSV,
                                "http://localhost.localdomain/~jim/data/insured.csv")
                        .build();

        try {
            context.getProperties().setProperty(DataContextProperties.DELIMITER.property(), "|");
            log.debug("hasAdditionalProperties: {}", context.hasAdditionalProperties());

            final SourceService sourceService =
                    sourceServiceFactory.locate(context.getSourceType());
            final Dataset data = sourceService.retrieveDataset(context);
            log.debug("data: {}", data);
        } catch (ConduitException e) {
            log.error("Test failed: ", e);
        }
    }

    /** Test JSON data source retrieve. */
    @Test
    @Order(12)
    void retrieveJsonSource() {
        final DataContext context =
                DataContext.init(
                                SourceType.JSON,
                                "https://localhost.localdomain/~jim/data/insured.json")
                        .build();

        try {
            final SourceService sourceService =
                    sourceServiceFactory.locate(context.getSourceType());
            final Dataset data = sourceService.retrieveDataset(context);
            log.debug("data: {}", data);
        } catch (ConduitException e) {
            log.error("Test failed: ", e);
        }
    }

    /** Test JSON data source insert into JDBC data source. */
    @Test
    @Order(20)
    void jsonSourceJdbcTarget() {
        final DataContext sourceContext =
                DataContext.init(SourceType.JDBC, "jdbc:postgresql://localhost:5432/insurance")
                        .driverClassName("org.postgresql.Driver")
                        .user("jim")
                        .query(
                                "select id, given_name || '-x' as given_name, surname || '-x'"
                                        + " as surname from insured")
                        .build();

        final DataContext targetContext =
                DataContext.init(SourceType.JDBC, "jdbc:postgresql://localhost:5432/insurance")
                        .driverClassName("org.postgresql.Driver")
                        .user("jim")
                        .build();

        targetContext
                .getProperties()
                .setProperty(DataContextProperties.TARGET_TABLE.property(), "spinsured");

        targetContext
                .getProperties()
                .setProperty(DataContextProperties.USE_BIND_VARIABLES.property(), "true");

        try {
            final SourceService sourceService =
                    sourceServiceFactory.locate(sourceContext.getSourceType());
            final SourceService targetService =
                    sourceServiceFactory.locate(targetContext.getSourceType());
            final Dataset data = sourceService.retrieveDataset(sourceContext);

            log.debug("data: {}", data);

            targetService.insertDataset(targetContext, data);
        } catch (ConduitException e) {
            log.error("Test failed: ", e);
        }
    }
}
