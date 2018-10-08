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

package org.apache.ambari.server.state;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public final class Configs {
  private final Optional<Long> serviceId;
  private final Set<Config> configs;

  private Configs(Optional<Long> serviceId, Set<Config> configs) {
    this.serviceId = serviceId;
    this.configs = configs.stream().filter(Objects::nonNull).collect(Collectors.toSet());
  }

  public static final Configs of(Optional<Long> serviceId, Set<Config> configs) {
    return new Configs(serviceId, configs);
  }

  public static final Configs of(Set<Config> configs) {
    return new Configs(Optional.empty(), configs);
  }

  public Optional<Long> getServiceId() {
    return serviceId;
  }

  public Set<Config> getConfigs() {
    return configs;
  }
}
