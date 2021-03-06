/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.ambari.server.events.listeners.alerts;

import java.util.ArrayList;
import java.util.List;

import org.apache.ambari.server.AmbariException;
import org.apache.ambari.server.EagerSingleton;
import org.apache.ambari.server.agent.stomp.dto.AlertGroupUpdate;
import org.apache.ambari.server.events.AlertDefinitionDeleteEvent;
import org.apache.ambari.server.events.AlertGroupsUpdateEvent;
import org.apache.ambari.server.events.UpdateEventType;
import org.apache.ambari.server.events.publishers.AmbariEventPublisher;
import org.apache.ambari.server.events.publishers.StateUpdateEventPublisher;
import org.apache.ambari.server.orm.dao.AlertDispatchDAO;
import org.apache.ambari.server.orm.entities.AlertGroupEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
@EagerSingleton
public class AlertGroupsUpdateListener {

  private static final Logger LOG = LoggerFactory.getLogger(AlertGroupsUpdateListener.class);

  @Inject
  private StateUpdateEventPublisher stateUpdateEventPublisher;

  @Inject
  private AlertDispatchDAO alertDispatchDAO;

  @Inject
  public AlertGroupsUpdateListener(AmbariEventPublisher ambariEventPublisher) {
    ambariEventPublisher.register(this);
  }

  @Subscribe
  public void onAlertDefinitionDeleted(AlertDefinitionDeleteEvent event) throws AmbariException {
    List<AlertGroupUpdate> alertGroupUpdates = new ArrayList<>();
    for (AlertGroupEntity alertGroupEntity : alertDispatchDAO.findAllGroups(event.getClusterId())) {
      if (alertGroupEntity.getAlertDefinitions().contains(event.getDefinition().getDefinitionId())) {
        AlertGroupUpdate alertGroupUpdate = new AlertGroupUpdate(alertGroupEntity);
        alertGroupUpdate.getTargets().remove(event.getDefinition().getDefinitionId());
        alertGroupUpdates.add(alertGroupUpdate);
      }
    }
    stateUpdateEventPublisher.publish(new AlertGroupsUpdateEvent(alertGroupUpdates, UpdateEventType.UPDATE));
  }
}
