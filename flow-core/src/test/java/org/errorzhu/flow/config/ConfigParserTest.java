package org.errorzhu.flow.config;

import com.google.common.base.Preconditions;
import org.errorzhu.flow.shade.com.typesafe.config.Config;
import org.errorzhu.flow.shade.com.typesafe.config.ConfigException;
import org.errorzhu.flow.shade.com.typesafe.config.ConfigFactory;
import org.errorzhu.flow.shade.com.typesafe.config.ConfigResolveOptions;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class ConfigParserTest {

    @Test
    public void test_parse_file() {
        Config config = ConfigFactory.parseResources("test_config");
        List<? extends Config> source = config.getConfigList("source");
        Config fakeSource = source.get(0);
        String field_name = fakeSource.getString("fieldName");
        String plugin_name = fakeSource.getString("pluginName");
        Assert.assertEquals("name,age", field_name);
        Assert.assertEquals("FakeSource", plugin_name);
    }

    @Test(expected = ConfigException.class)
    public void test_get_config_list() {
        Config config = ConfigFactory.parseResources("test_config");
//        List<? extends Config> source = config.getConfigList("a");
        List<? extends Config> a = Preconditions.checkNotNull(config.getConfigList("a"));
        Assert.assertEquals(0, a.size());
    }

    @Test
    public void test_parse_resolve() {
        Config config = ConfigFactory.parseResources("test_config");
        Assert.assertEquals(config.hasPath("a"), false);
    }


    
    @Test
    public void test_get_multi_source(){
        Config config = ConfigFactory.parseResources("fake_to_console.conf")
                .resolve(ConfigResolveOptions.defaults().setAllowUnresolved(true))
                .resolveWith(ConfigFactory.systemProperties(),
                        ConfigResolveOptions.defaults().setAllowUnresolved(true));

//        config.entrySet();


    }


}
