package org.errorzhu.flow.base.sink;

import org.errorzhu.flow.base.model.Row;
import org.errorzhu.flow.base.plugin.IPlugin;

public interface ISink extends IPlugin {
    void write(Row data) throws Exception;
    Boolean isRunning();
    void open() throws Exception;
    void close() throws Exception;
}
