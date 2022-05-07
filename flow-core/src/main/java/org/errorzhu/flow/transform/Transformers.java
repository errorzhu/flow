package org.errorzhu.flow.transform;

import org.errorzhu.flow.base.model.Row;
import org.errorzhu.flow.base.transform.ITransform;

import java.util.LinkedList;
import java.util.List;

public class Transformers {
    private List<ITransform> transforms = new LinkedList<>();

    public Transformers(List<ITransform> transformsList) {
        this.transforms = transformsList;
    }

    public Row tranform(Row data) {
        for (ITransform transform : transforms) {
            data = transform.transform(data);
        }
        return data;
    }
}
