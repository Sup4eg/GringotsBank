import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import javax.swing.plaf.basic.BasicComboBoxRenderer;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JList;
import javax.swing.UIManager;


public class ExchangeUI extends JFrame{
    private String[] currencyItems = {"MAG", "GBP", "USD", "RUB"};
   // private String[] currencyItemsMUG = {"GBR", "USD", "RUB"};
    //private String[] currencyItemsMAG = {"MAG"};
    private JComboBox currencyInCB = new JComboBox(currencyItems);
    private JComboBox currencyOutCB1 = new JComboBox(currencyItems);
//    private JComboBox currencyOutCB2 = new JComboBox(currencyItemsMAG);

    private JTextField moneyInTF = new JTextField(10);
    private JTextField moneyOutTF = new JTextField(25);
    private JButton exchangeB = new JButton("Exchange");
    private JButton currencyInfoB = new JButton("Currency information");

    public ExchangeUI(){
        super("Exchange currency");
        this.setBounds(200,100,300,150);
        //this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        final Container container = this.getContentPane();
        container.setLayout(new GridLayout(3,2,2,2));

        moneyOutTF.setEnabled(false);

        container.add(currencyInCB);
        container.add(moneyInTF);
        container.add(currencyOutCB1);
        container.add(moneyOutTF);
        container.add(exchangeB);
        container.add(currencyInfoB);


        final Exchange exchange = new Exchange();

        exchangeB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (Integer.parseInt(moneyInTF.getText())<0) {
                        JOptionPane.showMessageDialog(null,"Incorrect numbers");
                    }
                    else {
                        Double moneyOut = exchange.changeMoney(currencyInCB.getSelectedItem().toString(), currencyOutCB1.getSelectedItem().toString(), Integer.parseInt(moneyInTF.getText()));
                        moneyOutTF.setText(moneyOut.toString());
                        JOptionPane.showMessageDialog(null, "Money was exchanged");
                    }
                }
                catch (NumberFormatException ex){
                    JOptionPane.showMessageDialog(null,"Incorrect numbers");
                }
            }
        });

        currencyInfoB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new CurrencyInfoUI().setVisible(true);
            }
        });
    }

    public static void main(String[] args) {
        ExchangeUI app = new ExchangeUI();
        app.setVisible(true);
    }

}
