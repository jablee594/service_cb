package sql;

import java.sql.*;

public class RequestValuteCodeDB {

    public static final String DB_URL = "jdbc:h2:~/IdeaProjects/service_cb/db/ExchangeRateDB";
    public static final String DB_Driver = "org.h2.Driver";

    public static void main(String[] args) {
        try {
            Class.forName(DB_Driver); //Проверяем наличие JDBC драйвера для работы с БД
            Connection connection = DriverManager.getConnection(DB_URL);//соединениесБД
            System.out.println("Соединение с СУБД выполнено.");
            Statement st = null;
            st = connection.createStatement();
            ResultSet result;
            result = st.executeQuery("SELECT * FROM VALUTE WHERE CHARCODE = 'USD'");
            while (result.next()) {
                String vcode = result.getString("valutecode");
                System.out.println(vcode);
            }
            connection.close();       // отключение от БД
            System.out.println("Отключение от СУБД выполнено.");
        } catch (ClassNotFoundException e) {
            e.printStackTrace(); // обработка ошибки  Class.forName
            System.out.println("JDBC драйвер для СУБД не найден!");
        } catch (SQLException e) {
            e.printStackTrace(); // обработка ошибок  DriverManager.getConnection
            System.out.println("Ошибка SQL !");
        }
    }

}
