package org.errorzhu.flow.cli;

import com.beust.jcommander.JCommander;

public class FlowCommandParser {

    public static FlowCommandArgs parse(String[] argv) {
        FlowCommandArgs parameters = new FlowCommandArgs();
        JCommander.newBuilder()
                .addObject(parameters)
                .build()
                .parse(argv);
        return parameters;
    }

}
