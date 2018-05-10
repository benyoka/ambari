package org.apache.ambari.server.topology.validators;

import java.util.Map;

import org.apache.ambari.server.topology.ClusterTopology;
import org.apache.ambari.server.topology.InvalidTopologyException;
import org.apache.ambari.server.topology.TopologyValidator;

import com.google.common.base.Splitter;

public class HdfsValidator implements TopologyValidator {

  static final String NAMESERVICES_PROPERTY_1ST = "dfs.internal.nameservices";
  static final String NAMESERVICES_PROPERTY_2ND = "dfs.nameservices";
  static final String HDFS_SITE = "hdfs-site";

  @Override
  public void validate(ClusterTopology topology) throws InvalidTopologyException {
    validateNameServiceCount(topology);
  }

  public void validateNameServiceCount(ClusterTopology topology) throws InvalidTopologyException {
    Map<String, String> hdfsSiteProps = topology.getConfiguration().getFullProperties().get(HDFS_SITE);

    String nameServicesProp = hdfsSiteProps.containsKey(NAMESERVICES_PROPERTY_1ST) ?
      NAMESERVICES_PROPERTY_1ST : NAMESERVICES_PROPERTY_2ND;

    String nameServicesValue = hdfsSiteProps.get(nameServicesProp);

    if (null != nameServicesValue) {
      int nameServiceCount = Splitter.on(',').trimResults().omitEmptyStrings().splitToList(nameServicesValue).size();
      if (nameServiceCount > 4) {
        throw new InvalidTopologyException("Maximum 4 name services are allowed for HDFS. " +
          nameServicesProp + "=" + nameServicesValue);
      }
    }
  }

}
