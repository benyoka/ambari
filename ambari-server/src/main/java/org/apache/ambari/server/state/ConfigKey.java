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

import javax.annotation.Nonnull;

import com.google.common.base.MoreObjects;

public final class ConfigKey {
  private final Optional<Long> serviceId;
  private final String configType;

  private ConfigKey(@Nonnull Optional<Long> serviceId, @Nonnull String configType) {
    this.serviceId = serviceId;
    this.configType = configType;
  }

  public static ConfigKey of(@Nonnull Optional<Long> serviceId, @Nonnull String configType) {
    return new ConfigKey(serviceId, configType);
  }

  public static ConfigKey of(@Nonnull String configType) {
    return new ConfigKey(Optional.empty(), configType);
  }

  public Optional<Long> getServiceId() {
    return serviceId;
  }

  public String getConfigType() {
    return configType;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ConfigKey configKey = (ConfigKey) o;
    return Objects.equals(serviceId, configKey.serviceId) &&
      Objects.equals(configType, configKey.configType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(serviceId, configType);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
      .add("serviceId", serviceId)
      .add("configType", configType)
      .toString();
  }
}
