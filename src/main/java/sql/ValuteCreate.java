package sql;


import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class ValuteCreate {
    private static final String createValute = "create table valute (\r\n" + "  id_valute  int(10) primary key,\r\n" +
            "  ValuteCode varchar(20),\r\n" + "  CharCode varchar(20),\r\n" + "  );";

    public static void sql(String[] argv) throws SQLException {
        ValuteCreate createTablevalute = new ValuteCreate();
        createTablevalute.createTable();
    }

    private void createTable() throws SQLException {
        try (Connection connection = H2JDBCUtils.getConnection();
             Statement statement = connection.createStatement();){
            statement.execute(createTableSQL);
        } catch (SQLException e){
            H2JDBCUtils.printSQLException(e);
        }
    }
}


