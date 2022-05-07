package org.errorzhu.flow.pipeline;


import org.errorzhu.flow.base.context.PipelineContext;
import org.errorzhu.flow.base.model.Row;
import org.errorzhu.flow.base.sink.ISink;
import org.errorzhu.flow.base.source.ISource;
import org.errorzhu.flow.statistics.Statistics;
import org.errorzhu.flow.transform.Transformers;

public class Pipeline {

    private final PipelineContext<Row> context;
    private ISource source;
    private Transformers transformers;
    private ISink sink;
    private Statistics statistics = new Statistics();


    public Pipeline(ISource source, Transformers transformers, ISink sink) {
        this.source = source;
        this.transformers = transformers;
        this.sink = sink;
        this.context = new PipelineContext<>();
    }

    public void start() throws Exception {
        statistics.start();
        Row data;
        source.open();
        sink.open();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    source.run(context);
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        });
        thread.start();

        while (isRunning()) {
            data = context.out();
            if (data == null) {
                continue;
            }
            data = transformers.tranform(data);
            sink.write(data);
            statistics.countPlus();
        }
        source.close();
        sink.close();


        statistics.end();

        System.out.println("time spent :  " + statistics.getCostTime());
        System.out.println("row count :  " + statistics.getCount());
        thread.join();


    }

    private Boolean isRunning() {

        return source.isRunning() || context.isRunning() || sink.isRunning();

    }


}
