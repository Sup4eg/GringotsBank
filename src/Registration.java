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

    private Connection dbConnection = null;
    private Statement statement = null;

    {
        try {
            dbConnection = ConnectionDB.getDBConnection();
            statement = dbConnection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Registration reg = new Registration();
//        insert client test
        reg.insertClientSQL("Fleur", "Weasley", "Isabella", "Woman", "half-man", "Spellwoman", 4, "30.03.1977");

//        insert Address test
        reg.insertAddressSQL("Fleur", "Weasley", "Isabella", "Tinvort", null, "Kournuel",  null, null);

//        insert Wand parametrs test
        reg.insertWandParametersSQL("Fleur", "Weasley", "Isabella", 9, "Pink tree", "Villa hair", "Garrick Ollivander");
    }

//    Метод отправляет основную информацию о клиентах Gringots - строку в DBUSERS
    private Boolean insertClientSQL(String first_name, String second_name, String patronymic, String sex, String blood_status,
                                           String job, int storage_level, String date_of_birth) {

        String insertTableSQL = "INSERT INTO DBUSER"
                + "(FIRST_NAME, SECOND_NAME, PATRONYMIC, SEX, BLOOD_STATUS, JOB, STORAGE_LEVEL, DATE_OF_BIRTH) " + "VALUES"
                + String.format("('%s','%s','%s','%s','%s','%s','%s', to_date('%s', '%s'))", first_name, second_name, patronymic, sex, blood_status,
                job, storage_level, getCurrentTimeStamp(date_of_birth), "dd.mm.yyyy");

        try {
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

    private Boolean insertAddressSQL(String first_name, String second_name, String patronymic, String city, String street, String area, Integer home, Integer flat) {

        String insertAddressSQL_statement = "INSERT INTO DBADDRESS"
                + "(CLIENT_ID, CITY, STREET, AREA, HOME, FLAT) " + "VALUES";
        String insert_string;

        String insertAddressSQL = "INSERT INTO DBADDRESS"
                + "(CLIENT_ID, CITY, STREET, AREA, HOME, FLAT) " + "VALUES" +
                String.format("(%d,'%s','%s','%s',%d,%d)", getCurrentParametr(first_name, second_name, patronymic, "dbuser", "CLIENT_ID"),
                        city, street, area, home, flat).replace("'null'", "NULL");
        try {
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

    private Boolean insertWandParametersSQL(String first_name, String second_name, String patronymic, int length, String wood, String core, String made_by){
        String insertWandParametersSQL = "INSERT INTO DBWANDS"
                + "(CLIENT_ID, LENGTH, WOOD, CORE, MADE_BY) " + "VALUES" + String.format("(%d,%d,'%s','%s','%s')", getCurrentParametr(first_name, second_name, patronymic, "dbuser", "CLIENT_ID"), length, wood, core, made_by);
        try {
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

    private String getCurrentTimeStamp(String date_of_birth) {
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

    public Integer getCurrentParametr(String first_name, String second_name, String patronymic, String table, String request) {
        String selectClientIDSQL = String.format("SELECT %s from %s WHERE FIRST_NAME='%s' AND SECOND_NAME='%s' AND PATRONYMIC='%s'", request.toUpperCase(), table.toUpperCase(), first_name, second_name, patronymic);
        Integer client_id = null;
        try {
            ResultSet rs = statement.executeQuery(selectClientIDSQL);
            while (rs.next()) {
                client_id = Integer.parseInt(rs.getString(request));
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
