import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Exchange extends Storage {

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
        Exchange exchange = new Exchange();
//        exchange.insertDataToExchangeCurrencySQL("rub", 1300280, 65.358, 66.004, 1120000, 0, 0);
//        exchange.insertDataToExchangeCurrencySQL("usd", 763800, 0.015, 0.017, 965350, 0, 0);
//        exchange.insertDataToExchangeCurrencySQL("gbp", 940000, 0.012, 0.027, 1400000, 0, 0);
//        exchange.insertDataToExchangeCurrencySQL("mag", 3750000, 0.034, 0.039, 2840000, 0, 0);
    }

    //Метод для вставки данных в таблицу EXCHANGE_CURRENCY

    private Boolean insertDataToExchangeCurrencySQL(String currency, int balance_beginning, double purchase_rate, double selling_rate, int remainder, int bought, int sold) {
        String insertDataToExchangeCurrencySQL = "INSERT INTO EXCHANGE_CURRENCY"
                + "(CURRENCY, BALANCE_BEGINNING, PURCHASE_RATE, SELLING_RATE, REMAINDER, BOUGHT, SOLD) " + "VALUES" +
                String.format("('%s',%d,%s,%s,%d,%d,%d)", currency.toUpperCase(), balance_beginning, String.valueOf(purchase_rate), String.valueOf(selling_rate), remainder, bought, sold);

        try {
            statement.executeQuery(insertDataToExchangeCurrencySQL);
        } catch (SQLException e) {
            System.out.println("Currency data wasn't inserted");
            System.out.println(e.getMessage());
            return false;
        }
        System.out.println("Currency data was inserted");
        return true;
    }


    //Метод делает обмен валюты

    private Boolean changeMoney(String currency, int bought, int sold) {
        int current_bought = getMoneyParameter(currency, "bought");
        int current_sold = getMoneyParameter(currency, "sold");
//        updateTable("exchange_currency", "bought", new MultiArgument<Integer>(bought + current_bought));
//        updateTable("exchange_currency", "sold", new MultiArgument<Integer>(bought + current_sold));
        return true;
    }

//    Метод получает числовой параметр из таблцы EXCHANGE_TABLE

    private Integer getMoneyParameter(String currency, String column) {
        String selectFromExchangeCurrentSQL = String.format("SELECT %s FROM EXCHANGE_TABLE WHERE CURRENCY = '%s'", column.toUpperCase(), currency.toUpperCase());
        Integer parameter = null;
        try {
            ResultSet rs = statement.executeQuery(selectFromExchangeCurrentSQL);
            while (rs.next()) {
                parameter = Integer.parseInt(rs.getString(column));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return parameter;
    }
}
