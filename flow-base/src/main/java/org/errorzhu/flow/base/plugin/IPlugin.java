package org.errorzhu.flow.base.plugin;

import org.errorzhu.flow.shade.com.typesafe.config.Config;

public interface IPlugin {

    void init(Config config);
}
