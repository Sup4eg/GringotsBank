import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class CurrencyInfoUI extends JFrame{
    Exchange exchange = new Exchange();
    Map <String, String[]> exchangeMap = new HashMap<String, String[]>();

    private JTextArea currencyTA = new JTextArea(10,20);

    public CurrencyInfoUI(){
        super("Currency information");
        this.setBounds(200,100,300,250);
        //this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        final Container container = this.getContentPane();
        container.setLayout(new FlowLayout());
        currencyTA.setEditable(false);
        currencyTA.setText("Currency, Purchase rate, Sailing rate\r\n\r\n");
        container.add(new JLabel("Currency rate to Galleon"));
        container.add(currencyTA);

        exchangeMap = exchange.getExchangeCurrency();
        for (Map.Entry<String, String[]> value:exchangeMap.entrySet()) {
            currencyTA.append(value.getKey() + ", " + value.getValue()[1] + ", " + value.getValue()[2] + "\r\n");
        }

    }
    public static void main(String[] args) {
        CurrencyInfoUI app = new CurrencyInfoUI();
        app.setVisible(true);
    }
}
