package org.errorzhu.flow.statistics;

public class Statistics {
    private Long count = 0L;
    private Long start;
    private Long end;


    public void countPlus() {
        count++;
    }

    public Long getCount() {
        return count;
    }

    public void start() {
        this.start = System.currentTimeMillis();
    }

    public void end() {
        this.end = System.currentTimeMillis();
    }

    public Long getCostTime() {
        return end - start;
    }
}
