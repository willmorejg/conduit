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

import java.util.List;
import java.util.Map;
import net.ljcomputing.conduit.connector.impl.ConnectorFactory;
import net.ljcomputing.conduit.exception.ConduitException;
import net.ljcomputing.conduit.model.ConnectorProtocol;
import net.ljcomputing.conduit.model.DataContext;
import net.ljcomputing.conduit.model.DataContextProperties;
import net.ljcomputing.conduit.model.Dataset;
import net.ljcomputing.conduit.service.SourceService;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/** JUnit Tests. */
@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
@ActiveProfiles("test")
class ConduitApplicationTests {
    private static final Logger log = LoggerFactory.getLogger(ConduitApplicationTests.class);

    @Autowired private ConnectorFactory connectorFactory;

    @Autowired
    @Qualifier("jdbc")
    private SourceService jdbcSourceService;

    @Autowired
    @Qualifier("csv")
    private SourceService csvSourceService;

    /** Test to make sure application starts correctly. */
    @Test
    @Order(1)
    void contextLoads() {}

    /** Test connector factory. */
    @Test
    @Order(2)
    void connectorFactoryTest() {
        assertNotNull(connectorFactory.locate(ConnectorProtocol.JDBC));
    }

    /** Test JDBC data source retrieve. */
    @Test
    @Order(10)
    void retrieveJdbcSource() {
        final DataContext context =
                DataContext.builder()
                        .driverClassName("org.postgresql.Driver")
                        .url("jdbc:postgresql://localhost:5432/insurance")
                        .user("jim")
                        .query("select id, given_name || ' ' || surname as \"name\" from insured")
                        .build();

        context.getProperties().setProperty("foo", "bar");
        context.getProperties().setProperty(DataContextProperties.DELIMITER.property(), ",");

        try {
            log.debug("hasAdditionalProperties: {}", context.hasAdditionalProperties());
            log.debug("foo: {}", context.getProperties().getProperty("foo", ""));
            log.debug(
                    "{} : {}",
                    DataContextProperties.DELIMITER.property(),
                    context.getProperties()
                            .getProperty(DataContextProperties.DELIMITER.property(), ""));
            final List<Map<String, Object>> data = jdbcSourceService.retrieve(context);
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
                DataContext.builder()
                        .url("https://localhost.localdomain/~jim/data/insured.csv")
                        .build();

        try {
            log.debug("hasAdditionalProperties: {}", context.hasAdditionalProperties());
            log.debug("foo: {}", context.getProperties().getProperty("foo", ""));

            // final List<Map<String, Object>> data = csvSourceService.retrieve(context);
            final Dataset data = csvSourceService.retrieveDataset(context);
            log.debug("data: {}", data);
        } catch (ConduitException e) {
            log.error("Test failed: ", e);
        }
    }
}
