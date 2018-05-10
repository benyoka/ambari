package org.apache.ambari.server.topology.validators;

import static org.apache.ambari.server.topology.validators.HdfsValidator.HDFS_SITE;
import static org.apache.ambari.server.topology.validators.HdfsValidator.NAMESERVICES_PROPERTY_1ST;
import static org.apache.ambari.server.topology.validators.HdfsValidator.NAMESERVICES_PROPERTY_2ND;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.ambari.server.topology.ClusterTopology;
import org.apache.ambari.server.topology.Configuration;
import org.apache.ambari.server.topology.InvalidTopologyException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

public class HdfsValidatorTest {

  private HdfsValidator validator = new HdfsValidator();

  @Rule
  public ExpectedException expectedEx = ExpectedException.none();

  @Test
  public void testTooManyNameServices() throws Exception {
    String nameServices = "ns1, ns2, ns3, ns4, ns5";
    ClusterTopology topology = createTopology(NAMESERVICES_PROPERTY_1ST, nameServices);

    expectedEx.expect(InvalidTopologyException.class);
    expectedEx.expectMessage(expectedErrorMessage(NAMESERVICES_PROPERTY_1ST, nameServices));
    validator.validate(topology);
  }

  @Test
  public void testTooManyNameServices2() throws Exception {
    String nameServices = "ns1, ns2, ns3, ns4, ns5";
    ClusterTopology topology = createTopology(NAMESERVICES_PROPERTY_2ND, nameServices);

    expectedEx.expect(InvalidTopologyException.class);
    expectedEx.expectMessage(expectedErrorMessage(NAMESERVICES_PROPERTY_2ND, nameServices));
    validator.validate(topology);
  }

  @Test
  public void test_dfs_internal_nameservice_takesPrecedence() throws Exception {
    String nameServices1 = "ns1, ns2, ns3, ns4, ns5";
    String nameServices2 = "ns1, ns2, ns3, ns4";
    ClusterTopology topology = createTopology(
      NAMESERVICES_PROPERTY_1ST, nameServices1,
      NAMESERVICES_PROPERTY_2ND, nameServices2);

    expectedEx.expect(InvalidTopologyException.class);
    expectedEx.expectMessage(expectedErrorMessage(NAMESERVICES_PROPERTY_1ST, nameServices1));
    validator.validate(topology);
  }

  @Test
  public void test_dfs_internal_nameservice_takesPrecedence2() throws Exception {
    String nameServices1 = "ns1, ns2, ns3, ns4";
    String nameServices2 = "ns1, ns2, ns3, ns4, ns5";
    ClusterTopology topology = createTopology(
      NAMESERVICES_PROPERTY_1ST, nameServices1,
      NAMESERVICES_PROPERTY_2ND, nameServices2);
    validator.validate(topology);
  }

  @Test
  public void testAtMostFourNameServices() throws Exception {
    List<String> nameServicesList = ImmutableList.of("", "ns1", "ns1, ns2", "ns1, ns2, ns3", "ns1, ns2, ns3, ns4");
    for (String nameServices: nameServicesList) {
      validator.validate( createTopology(NAMESERVICES_PROPERTY_1ST, nameServices) );
      validator.validate( createTopology(NAMESERVICES_PROPERTY_2ND, nameServices) );
    }
  }

  @Test
  public void testNoNameServices() throws Exception {
    validator.validate( createTopology() );
  }


  private String expectedErrorMessage(String nameServicePropertyName, String nameServicePropertyValue) {
    return "Maximum 4 name services are allowed for HDFS. " + nameServicePropertyName + "=" + nameServicePropertyValue;
  }

  private ClusterTopology createTopology(String... keysAndValues) {
    Map<String, String> hdfsSiteProps = new HashMap<>();
    for (Iterator<String> it = Arrays.asList(keysAndValues).iterator(); it.hasNext(); ) {
      hdfsSiteProps.put(it.next(), it.next());
    }
    Map<String, Map<String, String>> clusterProps = ImmutableMap.of(HDFS_SITE, hdfsSiteProps);
    Configuration configuration = new Configuration(ImmutableMap.of(), ImmutableMap.of());
    Configuration parentConfiguration = new Configuration(clusterProps, ImmutableMap.of());
    configuration.setParentConfiguration(parentConfiguration);

    ClusterTopology topology = createMock(ClusterTopology.class);
    expect(topology.getConfiguration()).andReturn(parentConfiguration);
    replay(topology);
    return topology;
  }

}