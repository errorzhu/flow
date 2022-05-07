package org.errorzhu.flow.base.transform;

import org.errorzhu.flow.base.model.Row;
import org.errorzhu.flow.base.plugin.IPlugin;

public interface ITransform extends IPlugin {
    Row transform(Row data);
}
