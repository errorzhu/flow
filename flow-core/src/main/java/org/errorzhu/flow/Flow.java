package org.errorzhu.flow;


import org.errorzhu.flow.base.plugin.PluginType;
import org.errorzhu.flow.base.sink.ISink;
import org.errorzhu.flow.base.source.ISource;
import org.errorzhu.flow.base.transform.ITransform;
import org.errorzhu.flow.cli.FlowCommandArgs;
import org.errorzhu.flow.cli.FlowCommandParser;
import org.errorzhu.flow.config.ConfigParser;
import org.errorzhu.flow.pipeline.Pipeline;
import org.errorzhu.flow.plugin.PluginLoader;
import org.errorzhu.flow.shade.com.typesafe.config.Config;
import org.errorzhu.flow.transform.Transformers;

import java.util.List;

public class Flow {


    public static void main(String[] args) throws Exception {

        FlowCommandArgs flowArgs = FlowCommandParser.parse(args);

        String configFile = flowArgs.getConfig();
        String pluginsDir = flowArgs.getPlugins();
        Config config = ConfigParser.getConfig(configFile);
        ConfigParser.check(config);

        PluginLoader pluginLoader = new PluginLoader(config, pluginsDir);
        List<ISource> sources = pluginLoader.load(PluginType.SOURCE);
        List<ISink> sinks = pluginLoader.load(PluginType.SINK);
        List<ITransform> transformsList = pluginLoader.load(PluginType.TRANSFORM);
        Transformers transformers = new Transformers(transformsList);
        Pipeline pipeline = new Pipeline(sources.get(0), transformers, sinks.get(0));
        pipeline.start();


    }

}
