package org.errorzhu.flow.base.plugin;

public enum PluginType {
    SOURCE("source"), TRANSFORM("transform"), SINK("sink");

    private String type;

    PluginType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
