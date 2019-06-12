import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Storage extends Registration {

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
        Storage storage = new Storage();
//        storage.insertClientToCashSQL("Fleur", "Weasley", "Isabella");
//        storage.addMoneyToCashSQL("Fleur", "Weasley", "Isabella", 30, 210, 110);
//        storage.getMoneyFromCashSQL("Fleur", "Weasley", "Isabella", 30, 10, 10);
//        System.out.println(storage.getBalance("Fleur", "Weasley", "Isabella").toString());
    }

    //    Метод показывает текущий баланс в ячейке

    private ArrayList<Integer> getBalance(String first_name, String second_name, String patronymic) {

        if (getCashPermission(first_name, second_name, patronymic)) {
            int current_galleons = getCurrentParametr(first_name, second_name, patronymic, "dbcash", "galleons");
            int current_secles = getCurrentParametr(first_name, second_name, patronymic, "dbcash", "secles");
            int current_knats = getCurrentParametr(first_name, second_name, patronymic, "dbcash", "knats");
            ArrayList<Integer> money = new ArrayList<Integer>();
            money.add(current_galleons);
            money.add(current_secles);
            money.add(current_knats);
            return money;
        } else {
            System.out.println("Access denied");
            return null;
        }
    }


//Метод для вставки параметров пользователя, другая часть полей заполняется вручную в таблец DBCASH

    private Boolean insertClientToCashSQL(String first_name, String second_name, String patronymic) {

        int storage = getCurrentParametr(first_name, second_name, patronymic, "dbuser", "STORAGE");
        int client_id = getCurrentParametr(first_name, second_name, patronymic, "dbuser", "CLIENT_ID");

        if (!getCashPermission(first_name, second_name, patronymic)) {
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

    //Метод проверяет доступ человека к ячейке

    private Boolean getCashPermission(String first_name, String second_name, String patronymic) {
        int storage_level = getCurrentParametr(first_name, second_name, patronymic, "dbuser", "STORAGE_LEVEL");
        return storage_level == 4;
    }

    //Метод добавляет деньги в ячейку

    private Boolean addMoneyToCashSQL(String first_name, String second_name, String patronymic, int galleons, int sicles, int knats) {
        if (getCashPermission(first_name, second_name, patronymic)) {
            int current_galleons = getCurrentParametr(first_name, second_name, patronymic, "dbcash", "galleons");
            int current_secles = getCurrentParametr(first_name, second_name, patronymic, "dbcash", "secles");
            int current_knats = getCurrentParametr(first_name, second_name, patronymic, "dbcash", "knats");
            updateTable("dbcash", "galleons", new MultiArgument<Integer>(current_galleons + galleons));
            updateTable("dbcash", "secles", new MultiArgument<Integer>(current_secles + sicles));
            updateTable("dbcash", "knats", new MultiArgument<Integer>(current_knats + knats));
        } else {
            System.out.println("Access denied");
            return false;
        }
        return true;
    }

    //Метод изымает деньги из ячейки

    private Boolean getMoneyFromCashSQL(String first_name, String second_name, String patronymic, int galleons, int sicles, int knats) {
        if (getCashPermission(first_name, second_name, patronymic)) {
            int current_galleons = getCurrentParametr(first_name, second_name, patronymic, "dbcash", "galleons");
            int current_secles = getCurrentParametr(first_name, second_name, patronymic, "dbcash", "secles");
            int current_knats = getCurrentParametr(first_name, second_name, patronymic, "dbcash", "knats");
            updateTable("dbcash", "galleons", new MultiArgument<Integer>(current_galleons - galleons));
            updateTable("dbcash", "secles", new MultiArgument<Integer>(current_secles - sicles));
            updateTable("dbcash", "knats", new MultiArgument<Integer>(current_knats - knats));
        } else {
            System.out.println("Access denied");
            return false;
        }
        return true;
    }

    public Boolean updateTable(String table, String column, MultiArgument<?> value) {
        String updateParametersInTable = String.format("UPDATE %s SET %s = '%s'", table.toUpperCase(), column, value.getArgument());
        try {
            statement.executeQuery(updateParametersInTable);
        } catch (SQLException e) {
            System.out.println("Parameters wasn't update");
            System.out.println(e.getMessage());
            return false;
        }
        System.out.println("Parametrs was update");
        return true;
    }

    //Метод обновляет параметры в таблице

// Класс generic для разных аргументов
    private class MultiArgument<T> {
        private T argument;

        MultiArgument(T argument) {
            this.argument = argument;
        }

        public T getArgument() {
            return argument;
        }
    }

}
