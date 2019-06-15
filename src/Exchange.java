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
//Test
    public static void main(String[] args) {
        Exchange exchange = new Exchange();
//        exchange.insertDataToExchangeCurrencySQL("rub", 1300280, 0.00435, 0.00439, 0, 0, 0);
//        exchange.insertDataToExchangeCurrencySQL("usd", 763800, 0.01512, 0.01712, 0, 0, 0);
//        exchange.insertDataToExchangeCurrencySQL("gbp", 940000, 0.20312, 0.22712, 0, 0, 0);
//        exchange.insertDataToExchangeCurrencySQL("mag", 3750000, 4.93012, 4.93912, 0, 0, 0); //Относитльно фунта стерлингов
//        exchange.getExchangeCurrency();
//        System.out.println(exchange.changeMoney("mag","gbp", 1234));
    }

    //Метод для вставки данных в таблицу EXCHANGE_CURRENCY

    public Boolean insertDataToExchangeCurrencySQL(String currency, int balance_beginning, double purchase_rate, double selling_rate, int remainder, int bought, int sold) {
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

    public double changeMoney(String input_money, String to_money, int cost) {

        // увеличить покупку и продажу соответствующей валюты

        double input_money_new_sold = getMoneyParameter(input_money, "sold").getArgument().doubleValue() + cost;
        double input_money_bought = getMoneyParameter(input_money, "bought").getArgument().doubleValue();

        double purchase_rate = getMoneyParameter(to_money, "purchase_rate").getArgument().doubleValue();
        double exchange_money = (float) cost / purchase_rate;

        double to_money_new_bought = getMoneyParameter(to_money, "bought").getArgument().doubleValue() + exchange_money;
        double to_money_sold = getMoneyParameter(to_money, "sold").getArgument().doubleValue();

        updateTable("exchange_currency", "sold", new MultiArgument<Double>(input_money_new_sold), "currency", new MultiArgument<String>(input_money.toUpperCase()));
        updateTable("exchange_currency", "bought", new MultiArgument<Double>(to_money_new_bought), "currency", new MultiArgument<String>(to_money.toUpperCase()));

        //обновить остаток

        double input_money_balance_beginning = getMoneyParameter(input_money, "balance_beginning").getArgument().doubleValue();
        double to_money_balance_beginning = getMoneyParameter(to_money, "balance_beginning").getArgument().doubleValue();

        double new_input_money_remainder = input_money_balance_beginning + input_money_bought - input_money_new_sold;
        double new_to_money_remainder = to_money_balance_beginning + to_money_new_bought - to_money_sold;

        updateTable("exchange_currency", "remainder", new MultiArgument<Double>(new_input_money_remainder), "currency", new MultiArgument<String>(input_money.toUpperCase()));
        updateTable("exchange_currency", "remainder", new MultiArgument<Double>(new_to_money_remainder), "currency", new MultiArgument<String>(to_money.toUpperCase()));

        return exchange_money;
    }


    //Метод возвращает все строки из таблицы EXCHANGE_CURRENCY

    public Map getExchangeCurrency() {
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

//    Метод получает числовой (double или int) параметр из таблцы EXCHANGE_TABLE

    public NumberArguments getMoneyParameter(String currency, String column) {
        String selectFromExchangeCurrentSQL = String.format("SELECT %s FROM EXCHANGE_CURRENCY WHERE CURRENCY = '%s'", column.toUpperCase(), currency.toUpperCase());
        NumberArguments parameter = null;
        try {
            ResultSet rs = statement.executeQuery(selectFromExchangeCurrentSQL);
            while (rs.next()) {
                if (!rs.getString(column).contains(".")) {
                    parameter = new NumberArguments<Integer>(Integer.parseInt(rs.getString(column)));
                } else {
                    parameter = new NumberArguments<Double>(Double.parseDouble(rs.getString(column)));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return parameter;
    }

// Класс generic для извлечения double и int инф. из строк таблицы

    private class NumberArguments<T extends Number> {
        private T argument;
        NumberArguments(T argument) {
            this.argument = argument;
        }
        public T getArgument() {
            return argument;
        }
    }
}
