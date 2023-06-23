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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import net.ljcomputing.conduit.exception.ConduitException;
import net.ljcomputing.conduit.model.ConnectorContext;
import net.ljcomputing.conduit.model.DataContext;
import net.ljcomputing.conduit.model.SourceType;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

/** CSV Source Service Implementation. */
@Service("json")
public class JsonSourceServiceImpl extends AbstractSourceServiceImpl {
    private ObjectMapper mapper;
    private Resource resource;

    /** {@inheritDoc} */
    @Override
    public SourceType sourceType() {
        return SourceType.JSON;
    }

    /** {@inheritDoc} */
    @Override
    public void init(final DataContext context) {
        loadResource(context);
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
            mapper = new ObjectMapper();
            List<Map<String, Object>> records =
                    mapper.readValue(
                            resource.getInputStream(),
                            new TypeReference<List<Map<String, Object>>>() {});
            return records;
        } catch (final Exception e) {
            throw new ConduitException(e);
        }
    }
}
