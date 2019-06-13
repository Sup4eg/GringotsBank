import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Exchange {

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
}
