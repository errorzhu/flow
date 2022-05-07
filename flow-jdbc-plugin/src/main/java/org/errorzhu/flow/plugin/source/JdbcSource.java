package org.errorzhu.flow.plugin.source;

import org.errorzhu.flow.base.context.PipelineContext;
import org.errorzhu.flow.base.model.Row;
import org.errorzhu.flow.base.source.ISource;
import org.errorzhu.flow.shade.com.typesafe.config.Config;

import java.sql.*;

public class JdbcSource implements ISource {
    private String driverClass;
    private String url;
    private String username;
    private String password;
    private Connection connection;
    private PreparedStatement preparedStatement;
    private String sql;
    private ResultSet resultSet;
    private Boolean isRunning = Boolean.TRUE;

    @Override
    public void init(Config config) {
        driverClass = config.getString("driver");
        url = config.getString("url");
        username = config.getString("username");
        password = config.getString("password");
        sql = config.getString("sql");

    }

    @Override
    public void run(PipelineContext context) throws Exception {

        resultSet = preparedStatement.executeQuery();
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();
        System.out.println(columnCount);

        while (resultSet.next()) {
            Object[] rowData = new Object[columnCount];
            for (int i = 0; i < columnCount; i++) {
                rowData[i] = resultSet.getObject(i + 1);
            }
            context.collect(Row.of(rowData));
        }
        isRunning = Boolean.FALSE;

    }

    @Override
    public void open() throws SQLException, ClassNotFoundException {
        Class.forName(driverClass);
        connection = DriverManager.getConnection(url, username, password);
        preparedStatement = connection.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        preparedStatement.setFetchSize(Integer.MIN_VALUE);
        preparedStatement.setFetchDirection(ResultSet.FETCH_REVERSE);

    }

    @Override
    public void close() throws Exception {

        if (resultSet != null) {
            resultSet.close();
        }

        if (preparedStatement != null) {
            preparedStatement.close();
        }

        if (connection != null) {
            connection.close();
        }
    }

    @Override
    public Boolean isRunning() {
        return isRunning;
    }
}
