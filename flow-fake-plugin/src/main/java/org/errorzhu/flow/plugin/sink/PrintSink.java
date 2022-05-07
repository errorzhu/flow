package org.errorzhu.flow.plugin.sink;

import org.errorzhu.flow.base.model.Row;
import org.errorzhu.flow.base.sink.ISink;
import org.errorzhu.flow.shade.com.typesafe.config.Config;

public class PrintSink implements ISink {
    private Config config;

    @Override
    public void write(Row data) {
        System.out.println(data.toString());
    }

    @Override
    public Boolean isRunning() {
        return Boolean.FALSE;
    }

    @Override
    public void open() throws Exception {

    }

    @Override
    public void close() throws Exception {

    }

    @Override
    public void init(Config config) {
        this.config = config;

    }
}
