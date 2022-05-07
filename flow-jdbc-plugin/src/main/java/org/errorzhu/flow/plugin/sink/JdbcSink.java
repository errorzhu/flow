package org.errorzhu.flow.plugin.sink;

import org.errorzhu.flow.base.model.Row;
import org.errorzhu.flow.base.sink.ISink;
import org.errorzhu.flow.shade.com.typesafe.config.Config;

import java.sql.*;

public class JdbcSink implements ISink {

    private String driverClass;
    private String url;
    private String username;
    private String password;
    private Connection connection;
    private PreparedStatement preparedStatement;
    private String sql;
    private Boolean isRunning = Boolean.FALSE;
    private Long batchSize = 5000L;
    private Long count = 0L;

    @Override
    public void init(Config config) {
        driverClass = config.getString("driver");
        url = config.getString("url");
        username = config.getString("username");
        password = config.getString("password");
        if (config.hasPath("batchSize")) {
            batchSize = config.getLong("batchSize");
        }
        sql = config.getString("sql");

    }

    @Override
    public void write(Row data) throws SQLException {
        for (int i = 1; i < data.getArity() + 1; i++) {
            preparedStatement.setObject(i, data.getField(i-1));
        }
        preparedStatement.addBatch();
        if ((count + 1) % batchSize == 0) {
            preparedStatement.executeBatch();
            preparedStatement.clearParameters();
        }
        count++;
    }

    @Override
    public Boolean isRunning() {
        return isRunning;
    }

    @Override
    public void open() throws Exception {
        Class.forName(driverClass);
        connection = DriverManager.getConnection(url, username, password);
        preparedStatement = connection.prepareStatement(sql);
    }

    @Override
    public void close() throws Exception {

        if (preparedStatement != null) {
            preparedStatement.executeBatch();
            preparedStatement.clearBatch();
            preparedStatement.close();
        }

        if (connection != null) {
            connection.close();
        }

    }
}
