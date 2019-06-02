import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectionDB {

    public static void main(String[] argv) {
        try {
            createDbClientTable();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    private static Connection getDBConnection() {

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
            dbConnection = DriverManager.getConnection(DB_CONNECTION, DB_USER,DB_PASSWORD);
            return dbConnection;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return dbConnection;
    }

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
                + "DATE_OF_BIRTH DATE NOT NULL, "
                + "PRIMARY KEY (CLIENT_ID) "
                + ")";

        try {
            dbConnection = getDBConnection();
            statement = dbConnection.createStatement();

            // выполнить SQL запрос
            statement.execute(createTableSQL);
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

}
