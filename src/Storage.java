import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    //Test

    public static void main(String[] args) {
        Storage storage = new Storage();
//        storage.insertClientToCashSQL("Tom", "Riddle", "Marvollo");
//        storage.addMoneyToCashSQL("Tom", "Riddle", "Marvollo", 30, 210, 110);
//        storage.getMoneyFromCashSQL("Tom", "Riddle", "Marvollo", 30, 100, 100);
//        System.out.println(storage.getBalance("Tom", "Riddle", "Marvollo").toString());
//        System.out.println(storage.checkCashNumber("1111111111110000020").toString());
    }

    //    Метод показывает текущий баланс в ячейке

    public ArrayList<Integer> getBalance(String first_name, String second_name, String patronymic) {

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

    public Boolean insertClientToCashSQL(String first_name, String second_name, String patronymic) {

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

    public Boolean getCashPermission(String first_name, String second_name, String patronymic) {
        int storage_level = getCurrentParametr(first_name, second_name, patronymic, "dbuser", "STORAGE_LEVEL");
        return storage_level == 4;
    }

    //Метод добавляет деньги в ячейку

    public Boolean addMoneyToCashSQL(String first_name, String second_name, String patronymic, int galleons, int sicles, int knats) {
        if (getCashPermission(first_name, second_name, patronymic)) {
            int current_galleons = getCurrentParametr(first_name, second_name, patronymic, "dbcash", "galleons");
            int current_secles = getCurrentParametr(first_name, second_name, patronymic, "dbcash", "secles");
            int current_knats = getCurrentParametr(first_name, second_name, patronymic, "dbcash", "knats");
            int storage = getCurrentParametr(first_name, second_name, patronymic, "dbcash", "storage");
            updateTable("dbcash", "galleons", new MultiArgument<Integer>(current_galleons + galleons), "storage", new MultiArgument<Integer>(storage));
            updateTable("dbcash", "secles", new MultiArgument<Integer>(current_secles + sicles), "storage", new MultiArgument<Integer>(storage));
            updateTable("dbcash", "knats", new MultiArgument<Integer>(current_knats + knats), "storage", new MultiArgument<Integer>(storage));
        } else {
            System.out.println("Access denied");
            return false;
        }
        return true;
    }

    //Метод изымает деньги из ячейки

    public Boolean getMoneyFromCashSQL(String first_name, String second_name, String patronymic, int galleons, int sicles, int knats) {
        if (getCashPermission(first_name, second_name, patronymic)) {
            int current_galleons = getCurrentParametr(first_name, second_name, patronymic, "dbcash", "galleons");
            int current_secles = getCurrentParametr(first_name, second_name, patronymic, "dbcash", "secles");
            int current_knats = getCurrentParametr(first_name, second_name, patronymic, "dbcash", "knats");
            int storage = getCurrentParametr(first_name, second_name, patronymic, "dbcash", "storage");
            updateTable("dbcash", "galleons", new MultiArgument<Integer>(current_galleons - galleons), "storage", new MultiArgument<Integer>(storage));
            updateTable("dbcash", "secles", new MultiArgument<Integer>(current_secles - sicles), "storage", new MultiArgument<Integer>(storage));
            updateTable("dbcash", "knats", new MultiArgument<Integer>(current_knats - knats), "storage", new MultiArgument<Integer>(storage));
        } else {
            System.out.println("Access denied");
            return false;
        }
        return true;
    }

    //Метод обновляет параметры в таблице

    public Boolean updateTable(String table, String column, MultiArgument<?> value, String columt_id, MultiArgument<?> column_id_value) {

        String parse_value = value.getArgument().toString();
        Pattern pattern = Pattern.compile("^[-+]?\\d+(\\.{0,1}(\\d+?))?$");
        Matcher matcher = pattern.matcher(parse_value);

        String check_parse_value = matcher.matches() ? parse_value.replace(".", ",") : parse_value;
        String updateParametersInTable = String.format("UPDATE %s SET %s = '%s' WHERE %s = '%s'", table.toUpperCase(), column.toUpperCase(), check_parse_value, columt_id.toUpperCase(), column_id_value.getArgument());
        try {
            statement.executeQuery(updateParametersInTable);
            System.out.println("Parametrs was update");
        } catch (SQLException e) {
            System.out.println("Parameters wasn't update");
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }


    // Метод проверяет наличие определенного чека в dbcash. Если чек есть, то возвращается номер хранилища, иначе null

    public ArrayList<String> checkCashNumber(String cash_number) {
        String selectCashNumberSQL = "SELECT CASH_NUMBER from DBCASH";
        String selectStorageSQL = String.format("SELECT FIRST_NAME, SECOND_NAME, PATRONYMIC from DBCASH WHERE CASH_NUMBER = '%s'", cash_number);
        boolean isCashNumber = false;
        ArrayList<String> name_arr = null;
        try {
            ResultSet rs_check = statement.executeQuery(selectCashNumberSQL);
            while (rs_check.next()) {
                if (rs_check.getString(1).equals(cash_number)) {
                    isCashNumber = true;
                }
            }
            if (isCashNumber) {
                ResultSet rs_storage = statement.executeQuery(selectStorageSQL);
                while (rs_storage.next()) {
                    name_arr = new ArrayList<String>();
                    name_arr.add(rs_storage.getString("first_name"));
                    name_arr.add(rs_storage.getString("second_name"));
                    name_arr.add(rs_storage.getString("patronymic"));
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return name_arr;
    }


    // Класс generic для разных аргументов
    public class MultiArgument<T> {
        private T argument;

        MultiArgument(T argument) {
            this.argument = argument;
        }

        public T getArgument() {
            return argument;
        }
    }

}
