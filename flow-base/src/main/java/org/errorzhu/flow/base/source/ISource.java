package org.errorzhu.flow.base.source;

import org.errorzhu.flow.base.context.PipelineContext;
import org.errorzhu.flow.base.plugin.IPlugin;

public interface ISource extends IPlugin {
    void run(PipelineContext context) throws Exception;
    void open() throws Exception;
    void close() throws Exception;
    Boolean isRunning();
}
