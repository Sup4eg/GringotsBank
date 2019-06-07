import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

//Класс регистрациии пользователей в банке Гринготтс
public class Registration {
    public static void main(String[] args) {
        Registration reg = new Registration();
//        insert client test
        reg.insertClientSQL("Harry", "Potter", "James", "Man", "half-blood", "Student", 3, "23.08.1996");

//        insert Address test
//        reg.insertAddressSQL("Harry", "Potter", "Little Winging", "Privet Drive", null, 15, null);

//        insert Wand parametrs test
//        reg.insertWandParametersSQL("Harry", "Potter", 13, "Ostrolist", "Phoenix Feather", "Garrick Ollivander");
    }

//    Метод отправляет основную информацию о клиентах Gringots - строку в DBUSERS
    private Boolean insertClientSQL(String first_name, String second_name, String patronymic, String sex, String blood_status,
                                           String job, int storage_level, String date_of_birth) {

        String insertTableSQL = "INSERT INTO DBUSER"
                + "(FIRST_NAME, SECOND_NAME, PATRONYMIC, SEX, BLOOD_STATUS, JOB, STORAGE_LEVEL, DATE_OF_BIRTH) " + "VALUES"
                + String.format("('%s','%s','%s','%s','%s','%s','%s', to_date('%s', '%s'))", first_name, second_name, patronymic, sex, blood_status,
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

    //    Метод отправляет информацию об адресе клиентов - строку в DBADDRESS

    private Boolean insertAddressSQL(String first_name, String second_name, String city, String street, String area, Integer home, Integer flat) {

        String insertAddressSQL = "INSERT INTO DBADDRESS"
                + "(CLIENT_ID, CITY, STREET, AREA, HOME, FLAT) " + "VALUES" + String.format("(%d,'%s','%s','%s',%d,%d)", getCurrentID(first_name, second_name), city, street, area, home, flat);
        try {
            Connection dbConnection = ConnectionDB.getDBConnection();
            Statement statement = dbConnection.createStatement();
            statement.executeQuery(insertAddressSQL);
        } catch (SQLException e) {
            System.out.println("Address wasn't inserted");
            System.out.println(e.getMessage());
            return false;
        }
        System.out.println("Address is inserted");
        return true;
    }

    //    Метод отправляет информацию о палочке клиентов - строку в DBWANDS

    private Boolean insertWandParametersSQL(String first_name, String second_name, int length, String wood, String core, String made_by){
        String insertWandParametersSQL = "INSERT INTO DBWANDS"
                + "(CLIENT_ID, LENGTH, WOOD, CORE, MADE_BY) " + "VALUES" + String.format("(%d,%d,'%s','%s','%s')", getCurrentID(first_name, second_name), length, wood, core, made_by);
        try {
            Connection dbConnection = ConnectionDB.getDBConnection();
            Statement statement = dbConnection.createStatement();
            statement.executeQuery(insertWandParametersSQL);
        } catch (SQLException e) {
            System.out.println("Wand's parameters wasn't inserted");
            System.out.println(e.getMessage());
            return false;
        }
        System.out.println("Wand's parameters is inserted");
        return true;
    }

//    Метод возвращает дату рождения в нужно формате

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

//    Метод возвращает идентификатор выбранного пользователя

    private static Integer getCurrentID(String first_name, String second_name) {
        String selectClientIDSQL = String.format("SELECT CLIENT_ID from DBUSER WHERE FIRST_NAME='%s' AND SECOND_NAME='%s'", first_name, second_name);
        Integer client_id = null;
        try {
            Connection dbConnection = ConnectionDB.getDBConnection();
            Statement statement = dbConnection.createStatement();
            ResultSet rs = statement.executeQuery(selectClientIDSQL);
            while (rs.next()) {
                client_id = Integer.parseInt(rs.getString("CLIENT_ID"));
            }
            if (client_id == null) {
                throw new SQLException();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return client_id;
    }
}
