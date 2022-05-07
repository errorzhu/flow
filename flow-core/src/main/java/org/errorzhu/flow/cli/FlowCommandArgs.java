package org.errorzhu.flow.cli;

import com.beust.jcommander.Parameter;

public class FlowCommandArgs {

    @Parameter(names = "-config", description = "the config file of flow task")
    private String config;

    @Parameter(names = "-plugins", description = "the plugin dir")
    private String plugins;

    public String getConfig() {
        return config;
    }

    public String getPlugins() {
        return plugins;
    }
}
