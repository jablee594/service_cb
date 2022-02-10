package sql;

import java.sql.*;

public class RequestValuteCodeDB {

    public static final String DB_URL = "jdbc:h2:~/IdeaProjects/service_cb/db/ExchangeRateDB";
    public static final String DB_Driver = "org.h2.Driver";

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
            Class.forName(DB_Driver); //Проверяем наличие JDBC драйвера для работы с БД
            Connection connection = DriverManager.getConnection(DB_URL);//соединениесБД
            System.out.println("Соединение с СУБД выполнено.");

            Statement st = null;
            String str = "USD";
            st = connection.createStatement();
            ResultSet result;
            result = st.executeQuery("SELECT * FROM DAILY WHERE CODE = '"+str+"'");
            while (result.next()) {
                String vcode = result.getString("valute");
                System.out.println(vcode);
            }

            connection.close();       // отключение от БД
            System.out.println("Отключение от СУБД выполнено.");
    }

}
