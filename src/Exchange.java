import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

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
        exchange.insertDataToExchangeCurrencySQL("rub", 1300280, 0.00435, 0.00439, 1120000, 0, 0);
        exchange.insertDataToExchangeCurrencySQL("usd", 763800, 0.01512, 0.01712, 965350, 0, 0);
        exchange.insertDataToExchangeCurrencySQL("gbp", 940000, 0.20312, 0.22712, 1400000, 0, 0);
        exchange.insertDataToExchangeCurrencySQL("mag", 3750000, 4.93012, 4.93912, 2840000, 0, 0); //Относитльно фунта стерлингов
        exchange.getExchangeCurrency();
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


    //Метод делает обмен галлионов в любую валюту

    private Boolean changeGalleonsMoney(String to_money, int galleons) {
        int current_sold = getMoneyParameter("mag", "sold") + galleons ;
        int purchase_rate = getMoneyParameter(to_money, "purchase_rate");
        int exchange_money = purchase_rate * galleons;
        int current_bought = getMoneyParameter(to_money, "bought") + exchange_money;

//        updateTable("exchange_currency", current_sold)



        return true;
    }


    //Метод возвращает все строки из таблицы EXCHANGE_CURRENCY

    private Map getExchangeCurrency() {
        String selectAllFromExchangeCurrencySQL = "SELECT * FROM EXCHANGE_CURRENCY";
        Map <String, String[]> exchange_map = new HashMap<String, String[]>();
        try {
            ResultSet rs = statement.executeQuery(selectAllFromExchangeCurrencySQL);
            while (rs.next()) {
                String currency = rs.getString("CURRENCY");
                String balance_beginning = rs.getString("BALANCE_BEGINNING");
                String purchase_rate = rs.getString("PURCHASE_RATE");
                String selling_rate = rs.getString("SELLING_RATE");
                String remainder = rs.getString("REMAINDER");
                String bought = rs.getString("BOUGHT");
                String sold = rs.getString("SOLD");
                exchange_map.put(currency, new String[]{balance_beginning, purchase_rate, selling_rate, remainder, bought, sold});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exchange_map;
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
