package org.errorzhu.flow;

public class FlowTest {


    public static void main(String[] args) throws Exception {
        String[] myArgs = {"-config", "E:\\project\\java\\flow\\examples\\jdbc.conf", "-plugins", "E:\\project\\java\\flow\\plugins\\"};
        Flow flow = new Flow();
        flow.main(myArgs);
    }

}
