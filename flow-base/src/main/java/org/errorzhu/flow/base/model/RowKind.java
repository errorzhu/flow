package org.errorzhu.flow.base.model;

public enum RowKind {
    INSERT("+I", (byte)0),
    UPDATE_BEFORE("-U", (byte)1),
    UPDATE_AFTER("+U", (byte)2),
    DELETE("-D", (byte)3);

    private final String shortString;
    private final byte value;

    private RowKind(String shortString, byte value) {
        this.shortString = shortString;
        this.value = value;
    }

    public String shortString() {
        return this.shortString;
    }

    public byte toByteValue() {
        return this.value;
    }

    public static RowKind fromByteValue(byte value) {
        switch(value) {
            case 0:
                return INSERT;
            case 1:
                return UPDATE_BEFORE;
            case 2:
                return UPDATE_AFTER;
            case 3:
                return DELETE;
            default:
                throw new UnsupportedOperationException("Unsupported byte value '" + value + "' for row kind.");
        }
    }
}
