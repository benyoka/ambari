#!/usr/bin/env python
"""
Licensed to the Apache Software Foundation (ASF) under one
or more contributor license agreements.  See the NOTICE file
distributed with this work for additional information
regarding copyright ownership.  The ASF licenses this file
to you under the Apache License, Version 2.0 (the
"License"); you may not use this file except in compliance
with the License.  You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

"""

__all__ = ["ModuleConfigs"]


class ModuleConfigs(object):
  """
  This class maps to "/configurations" block in command.json which includes configuration information of a service
  """

  def __init__(self, config):
    self.__module_configs = config

  def get_properties(self, module_name, config_type, property_names):
    return map(lambda property_name: self.get_property_value(module_name, config_type, property_name), property_names)

  def get_property_value(self, module_name, config_type, property_name, default=None):
    try:
      return self.__module_configs[config_type][property_name]
    except:
      return default


