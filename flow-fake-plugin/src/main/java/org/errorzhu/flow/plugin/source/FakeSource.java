package org.errorzhu.flow.plugin.source;

import org.errorzhu.flow.base.context.PipelineContext;
import org.errorzhu.flow.base.model.Row;
import org.errorzhu.flow.base.source.ISource;
import org.errorzhu.flow.shade.com.typesafe.config.Config;

import java.util.Arrays;

public class FakeSource implements ISource {

    public static final String FIELD_NAME = "fieldName";
    private String[] fields = new String[]{};

    @Override
    public void run(PipelineContext context) throws InterruptedException {
        while (true) {
            Thread.sleep(1000);
            Row row = Row.withNames();
            Arrays.stream(fields).forEach(field -> row.setField(field, "test"));
            context.collect(row);
        }

    }

    @Override
    public void open() {

    }

    @Override
    public void close() throws Exception {

    }

    @Override
    public Boolean isRunning() {
        return Boolean.TRUE;
    }

    @Override
    public void init(Config config) {
        if (config.hasPath(FIELD_NAME)) {
            this.fields = config.getString(FIELD_NAME).split(",");
        }
    }
}
