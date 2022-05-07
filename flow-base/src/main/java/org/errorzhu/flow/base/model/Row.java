package org.errorzhu.flow.base.model;

import com.google.common.base.Preconditions;

import java.util.*;

public class Row {
    private StringJoiner joiner = new StringJoiner(",");
    private static final long serialVersionUID = 3L;
    private RowKind kind;
    private final Object[] fieldByPosition;
    private final Map<String, Object> fieldByName;
    private final LinkedHashMap<String, Integer> positionByName;

    Row(RowKind kind, Object[] fieldByPosition, Map<String, Object> fieldByName, LinkedHashMap<String, Integer> positionByName) {
        this.kind = kind;
        this.fieldByPosition = fieldByPosition;
        this.fieldByName = fieldByName;
        this.positionByName = positionByName;
    }

    public Row(RowKind kind, int arity) {
        this.kind = (RowKind) Preconditions.checkNotNull(kind, "Row kind must not be null.");
        this.fieldByPosition = new Object[arity];
        this.fieldByName = null;
        this.positionByName = null;
    }

    public Row(int arity) {
        this(RowKind.INSERT, arity);
    }

    public static Row withPositions(RowKind kind, int arity) {
        return new Row(kind, new Object[arity], (Map) null, (LinkedHashMap) null);
    }

    public static Row withPositions(int arity) {
        return withPositions(RowKind.INSERT, arity);
    }

    public static Row withNames(RowKind kind) {
        return new Row(kind, (Object[]) null, new HashMap(), (LinkedHashMap) null);
    }

    public static Row withNames() {
        return withNames(RowKind.INSERT);
    }

    public RowKind getKind() {
        return this.kind;
    }

    public void setKind(RowKind kind) {
        Preconditions.checkNotNull(kind, "Row kind must not be null.");
        this.kind = kind;
    }

    public int getArity() {
        if (this.fieldByPosition != null) {
            return this.fieldByPosition.length;
        } else {
            assert this.fieldByName != null;

            return this.fieldByName.size();
        }
    }

    public Object getField(int pos) {
        if (this.fieldByPosition != null) {
            return this.fieldByPosition[pos];
        } else {
            throw new IllegalArgumentException("Accessing a field by position is not supported in name-based field mode.");
        }
    }


    public Object getField(String name) {
        if (this.fieldByName != null) {
            return this.fieldByName.get(name);
        } else if (this.positionByName != null) {
            Integer pos = (Integer) this.positionByName.get(name);
            if (pos == null) {
                throw new IllegalArgumentException(String.format("Unknown field name '%s' for mapping to a position.", name));
            } else {
                assert this.fieldByPosition != null;

                return this.fieldByPosition[pos];
            }
        } else {
            throw new IllegalArgumentException("Accessing a field by name is not supported in position-based field mode.");
        }
    }


    public void setField(int pos, Object value) {
        if (this.fieldByPosition != null) {
            this.fieldByPosition[pos] = value;
        } else {
            throw new IllegalArgumentException("Accessing a field by position is not supported in name-based field mode.");
        }
    }

    public void setField(String name, Object value) {
        if (this.fieldByName != null) {
            this.fieldByName.put(name, value);
        } else {
            if (this.positionByName == null) {
                throw new IllegalArgumentException("Accessing a field by name is not supported in position-based field mode.");
            }

            Integer pos = (Integer) this.positionByName.get(name);
            if (pos == null) {
                throw new IllegalArgumentException(String.format("Unknown field name '%s' for mapping to a row position. Available names are: %s", name, this.positionByName.keySet()));
            }

            assert this.fieldByPosition != null;

            this.fieldByPosition[pos] = value;
        }

    }

    public Set<String> getFieldNames(boolean includeNamedPositions) {
        if (this.fieldByName != null) {
            return this.fieldByName.keySet();
        } else {
            return includeNamedPositions && this.positionByName != null ? this.positionByName.keySet() : null;
        }
    }

    public void clear() {
        if (this.fieldByPosition != null) {
            Arrays.fill(this.fieldByPosition, (Object) null);
        } else {
            assert this.fieldByName != null;

            this.fieldByName.clear();
        }

    }


    public static Row of(Object... values) {
        Row row = new Row(values.length);

        for (int i = 0; i < values.length; ++i) {
            row.setField(i, values[i]);
        }

        return row;
    }

    public static Row ofKind(RowKind kind, Object... values) {
        Row row = new Row(kind, values.length);

        for (int i = 0; i < values.length; ++i) {
            row.setField(i, values[i]);
        }

        return row;
    }

    public static Row copy(Row row) {
        Object[] newFieldByPosition;
        if (row.fieldByPosition != null) {
            newFieldByPosition = new Object[row.fieldByPosition.length];
            System.arraycopy(row.fieldByPosition, 0, newFieldByPosition, 0, newFieldByPosition.length);
        } else {
            newFieldByPosition = null;
        }

        HashMap newFieldByName;
        if (row.fieldByName != null) {
            newFieldByName = new HashMap(row.fieldByName);
        } else {
            newFieldByName = null;
        }

        return new Row(row.kind, newFieldByPosition, newFieldByName, row.positionByName);
    }

    public static Row project(Row row, int[] fieldPositions) {
        Row newRow = withPositions(row.kind, fieldPositions.length);

        for (int i = 0; i < fieldPositions.length; ++i) {
            newRow.setField(i, row.getField(fieldPositions[i]));
        }

        return newRow;
    }

    public static Row project(Row row, String[] fieldNames) {
        Row newRow = withNames(row.getKind());
        String[] var3 = fieldNames;
        int var4 = fieldNames.length;

        for (int var5 = 0; var5 < var4; ++var5) {
            String fieldName = var3[var5];
            newRow.setField(fieldName, row.getField(fieldName));
        }

        return newRow;
    }

    public static Row join(Row first, Row... remainings) {
        Preconditions.checkArgument(first.fieldByPosition != null, "All rows must operate in position-based field mode.");
        int newLength = first.fieldByPosition.length;
        Row[] var3 = remainings;
        int index = remainings.length;

        for (int var5 = 0; var5 < index; ++var5) {
            Row remaining = var3[var5];
            Preconditions.checkArgument(remaining.fieldByPosition != null, "All rows must operate in position-based field mode.");
            newLength += remaining.fieldByPosition.length;
        }

        Row joinedRow = new Row(first.kind, newLength);

        assert joinedRow.fieldByPosition != null;

        System.arraycopy(first.fieldByPosition, 0, joinedRow.fieldByPosition, index, first.fieldByPosition.length);
        index = index + first.fieldByPosition.length;
        Row[] var11 = remainings;
        int var12 = remainings.length;

        for (int var7 = 0; var7 < var12; ++var7) {
            Row remaining = var11[var7];

            assert remaining.fieldByPosition != null;

            System.arraycopy(remaining.fieldByPosition, 0, joinedRow.fieldByPosition, index, remaining.fieldByPosition.length);
            index += remaining.fieldByPosition.length;
        }

        return joinedRow;
    }

    @Override
    public String toString() {
        if (fieldByName != null) {
            return fieldByName.toString();
        }
        if (fieldByPosition != null) {

            Arrays.stream(fieldByPosition).forEach(f -> joiner.add(f.toString()));
            return joiner.toString();
        }
        return "{}";
    }
}

