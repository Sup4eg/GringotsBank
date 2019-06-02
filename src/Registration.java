import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Registration {
    public static void main(String[] args) {
//        insert client test
//        insertClientSQL("Harry", "Potter", "Man", "half-blood", "Student", 4, "23.08.1996");
//        insert Address test
//        insertAddressSQL("Harry", "Potter", "Little Winging", "Privet Drive", null, 15, null);
//        insert Wand parametrs test
    }

    private static Boolean insertClientSQL(String first_name, String second_name, String sex, String blood_status,
                                           String job, int storage_level, String date_of_birth) {

        String insertTableSQL = "INSERT INTO DBUSER"
                + "(FIRST_NAME, SECOND_NAME, SEX, BLOOD_STATUS, JOB, STORAGE_LEVEL, DATE_OF_BIRTH) " + "VALUES"
                + String.format("('%s','%s','%s','%s','%s','%s', to_date('%s', '%s'))", first_name, second_name, sex, blood_status,
                job, storage_level, getCurrentTimeStamp(date_of_birth), "dd.mm.yyyy");

        try {
            Connection dbConnection = ConnectionDB.getDBConnection();
            Statement statement = dbConnection.createStatement();
            statement.executeUpdate(insertTableSQL);
        } catch (SQLException e) {
            if (e.getSQLState().equals("23000")) {
                System.out.println("Client was registrated");
                return false;
            }
        }
        System.out.println("Client is registrated");
        return true;
    }

    private static Boolean insertAddressSQL(String first_name, String second_name, String city, String street, String area, Integer home, Integer flat) {

        String selectClientIDSQL = String.format("SELECT CLIENT_ID from DBUSER WHERE FIRST_NAME='%s' AND SECOND_NAME='%s'", first_name, second_name);

        try {
            Connection dbConnection = ConnectionDB.getDBConnection();
            Statement statement = dbConnection.createStatement();
            ResultSet rs = statement.executeQuery(selectClientIDSQL);
            Integer client_id = null;
            while (rs.next()) {
                client_id = Integer.parseInt(rs.getString("CLIENT_ID"));
            }
            String insertAddressSQL = "INSERT INTO DBADDRESS"
                    + "(CLIENT_ID, CITY, STREET, AREA, HOME, FLAT) " + "VALUES" + String.format("(%d,'%s','%s','%s',%d,%d)", client_id, city, street, area, home, flat);
            statement.executeQuery(insertAddressSQL);

        } catch (SQLException e) {
            System.out.println("Address wasn't insert");
            System.out.println(e.getMessage());
            return false;
        }
        System.out.println("Address is insert");
        return true;
    }

    private static String getCurrentTimeStamp(String date_of_birth) {
        DateFormat dateFormat = new SimpleDateFormat("dd.mm.yyyy");
        Date date = null;
        try {
            date = dateFormat.parse("29.08.1996");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateFormat.format(date);
    }
}
