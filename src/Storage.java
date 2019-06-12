import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Storage extends Registration {

    public static void main(String[] args) {
        Storage storage = new Storage();
        storage.insertClientToCashSQL("Fleur", "Weasley", "Isabella");
    }

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


//Метод для вставки параметров пользователя, другая часть полей заполняется вручную в таблец DBCASH

    private Boolean insertClientToCashSQL(String first_name, String second_name, String patronymic) {

        int storage_level = getCurrentParametr(first_name, second_name, patronymic, "dbuser", "STORAGE_LEVEL");
        int storage = getCurrentParametr(first_name, second_name, patronymic, "dbuser", "STORAGE");
        int client_id = getCurrentParametr(first_name, second_name, patronymic, "dbuser", "CLIENT_ID");

        if (storage_level != 4) {
            System.out.println("Access denied");
            return false;
        } else {
            String insertClientParametersToCashSQL = "INSERT INTO DBCASH"
                    + "(CLIENT_ID, STORAGE, FIRST_NAME, SECOND_NAME, PATRONYMIC) " + "VALUES" +
                    String.format("(%d,%d,'%s','%s','%s')", client_id, storage, first_name, second_name, patronymic);
            try {
                statement.executeQuery(insertClientParametersToCashSQL);
            } catch (SQLException e) {
                System.out.println("Cash parameters wasn't inserted");
                System.out.println(e.getMessage());
                e.printStackTrace();
                return false;
            }
            System.out.println("Cash parameters is inserted");
            return true;
        }
    }
}
