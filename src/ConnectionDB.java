import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

//Класс для создания таблиц в базе данных и для того чтобы подсоединять к oracle через jdbc (java)

public class ConnectionDB {

    public static void main(String[] argv) {
        try {
//            createDbClientTable();
//            createDbAddressTable();
            createDbWandsTable();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

//    Метод для подсоединения к бд oracle через java

    public static Connection getDBConnection() {

        final String DB_DRIVER = "oracle.jdbc.driver.OracleDriver";
        final String DB_CONNECTION = "jdbc:oracle:thin:@KirillTrezubov:1521:GRINGOTS";
        final String DB_USER = "s266657";
        final String DB_PASSWORD = "epb591";

        Connection dbConnection = null;
        try {
            Class.forName(DB_DRIVER);
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        try {
            dbConnection = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
            return dbConnection;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return dbConnection;
    }

//    Создаем таблицу с клиентами

    private static void createDbClientTable() throws SQLException {
        Connection dbConnection = null;
        Statement statement = null;

        String createTableSQL = "CREATE TABLE DBUSER("
                + "CLIENT_ID NUMBER(5) NOT NULL, "
                + "FIRST_NAME VARCHAR(20) NOT NULL, "
                + "SECOND_NAME VARCHAR(20) NOT NULL, "
                + "SEX VARCHAR(5) NOT NULL, "
                + "BLOOD_STATUS VARCHAR(20) NOT NULL, "
                + "JOB VARCHAR(20) NOT NULL, "
                + "STORAGE_LEVEL NUMBER(1) NOT NULL, "
                + "DATE_OF_BIRTH DATE NOT NULL, " + "PRIMARY KEY (CLIENT_ID), "
                + "CONSTRAINT db_user_unique UNIQUE (FIRST_NAME, SECOND_NAME) "
                + ")";


        String createClientSequence = "CREATE SEQUENCE client_seq START WITH 1";
        String trigger_definition = "CREATE OR REPLACE TRIGGER client_bir \n" +
                "BEFORE INSERT ON DBUSER \n" +
                "FOR EACH ROW \n" +
                "BEGIN \n" +
                "SELECT client_seq.NEXTVAL \n" +
                "INTO:new.client_id \n" +
                "FROM dual; \n" +
                "END;";

        try {
            dbConnection = getDBConnection();
            statement = dbConnection.createStatement();

            // выполнить SQL запрос
            statement.execute(createTableSQL);
            statement.execute(createClientSequence);
            statement.execute(trigger_definition);
            System.out.println("Table \"dbuser\" is created!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (statement != null) {
                statement.close();
            }
            if (dbConnection != null) {
                dbConnection.close();
            }
        }
    }

//    Создаем таблицу с адресами
    private static void createDbAddressTable() throws SQLException {
        Connection dbConnection = null;
        Statement statement = null;

        String createTableAddressSQL = "CREATE TABLE DBAddress("
                + "CLIENT_ID NUMBER(5) NOT NULL, "
                + "CITY VARCHAR(20) NOT NULL, "
                + "STREET VARCHAR(20), "
                + "AREA VARCHAR(20), "
                + "HOME NUMBER(5), "
                + "FLAT NUMBER(5), "
                + "CONSTRAINT fk_dbuser \n"
                + "FOREIGN KEY (CLIENT_ID) \n"
                + "REFERENCES DBUSER(CLIENT_ID) \n"
                + ")";
        try {
            dbConnection = getDBConnection();
            statement = dbConnection.createStatement();

            statement.execute(createTableAddressSQL);
            System.out.println("Table \"dbaddress\" is created!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (statement != null) {
                statement.close();
            }
            if (dbConnection != null) {
                dbConnection.close();
            }
        }
    }

//Создаем таблицу с волшебными палочками
    private static void createDbWandsTable() throws SQLException {
        Connection dbConnection = null;
        Statement statement = null;

        String createTableWandsSQL = "CREATE TABLE DBWands("
                + "CLIENT_ID NUMBER(5) NOT NULL, "
                + "LENGTH NUMBER(2) NOT NULL, "
                + "WOOD VARCHAR(20) NOT NULL, "
                + "CORE VARCHAR(20) NOT NULL, "
                + "MADE_BY VARCHAR(20) NOT NULL, "
                +  "UNIQUE (LENGTH, WOOD, CORE), "
                + "CONSTRAINT fk_dbwands \n"
                + "FOREIGN KEY (CLIENT_ID) \n"
                + "REFERENCES DBUSER(CLIENT_ID) \n"
                + ")";
        try {
            dbConnection = getDBConnection();
            statement = dbConnection.createStatement();

            statement.execute(createTableWandsSQL);
            System.out.println("Table \"dbwands\" is created!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (statement != null) {
                statement.close();
            }
            if (dbConnection != null) {
                dbConnection.close();
            }
        }
    }

}
