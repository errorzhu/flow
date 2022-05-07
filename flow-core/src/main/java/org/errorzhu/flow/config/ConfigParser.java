package org.errorzhu.flow.config;


import org.errorzhu.flow.base.exception.FlowConfigException;
import org.errorzhu.flow.base.plugin.PluginType;
import org.errorzhu.flow.shade.com.typesafe.config.Config;
import org.errorzhu.flow.shade.com.typesafe.config.ConfigFactory;
import org.errorzhu.flow.shade.com.typesafe.config.ConfigResolveOptions;

import java.io.File;

public class ConfigParser {


    public static Config getConfig(String configFile) {
        Config config = ConfigFactory
                .parseFile(new File(configFile))
                .resolve(ConfigResolveOptions.defaults().setAllowUnresolved(true))
                .resolveWith(ConfigFactory.systemProperties(),
                        ConfigResolveOptions.defaults().setAllowUnresolved(true));

//        ConfigRenderOptions options = ConfigRenderOptions.concise().setFormatted(true);
        return config;
    }

    public static void check(Config config) {

        if (!config.hasPath(PluginType.SOURCE.getType())) {
            throw new FlowConfigException("config must has a source!");
        }
        if (!config.hasPath(PluginType.SINK.getType())) {
            throw new FlowConfigException("config must has a sink!");
        }
    }


}
