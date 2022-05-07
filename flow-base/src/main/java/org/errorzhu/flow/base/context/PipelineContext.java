package org.errorzhu.flow.base.context;

import com.google.common.collect.Queues;

import java.util.Queue;

public class PipelineContext<T> {

    private Queue<T> queue = Queues.newLinkedBlockingQueue();

    public void collect(T data)  {
        queue.add(data);
    }

    public T out() {
        return queue.poll();
    }


    public Boolean isRunning() {
        return !queue.isEmpty();
    }
}
