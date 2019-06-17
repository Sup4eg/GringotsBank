import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CashCheckUI extends JFrame{
    private JLabel nameLB = new JLabel("Input full name: ");
    private JTextField nameTF = new JTextField();
    private JLabel checkNumberLB = new JLabel("Input check number: ");
    private JTextField checkNumberTF = new JTextField();
    private JLabel galleonsLB = new JLabel("Galleons: ");
    private JTextField galleonsTF = new JTextField();
    private JLabel seclesLB = new JLabel("Sickles: ");
    private JTextField seclesTF = new JTextField();
    private JLabel knatsLB = new JLabel("Knuts: ");
    private JTextField knatsTF = new JTextField();
    private JButton cashB = new JButton("Cash the Check");

    public CashCheckUI(){
        super("Cash the Check");
        this.setBounds(200,100,500,300);
        //this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container container = this.getContentPane();
        container.setLayout(new GridLayout(6,2,2,2));

        container.add(nameLB);
        container.add(nameTF);
        container.add(checkNumberLB);
        container.add(checkNumberTF);
        container.add(galleonsLB);
        container.add(galleonsTF);
        container.add(seclesLB);
        container.add(seclesTF);
        container.add(knatsLB);
        container.add(knatsTF);
        container.add(cashB);

        cashB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Storage storage = new Storage();
                String check = checkNumberTF.getText();
                ArrayList<String> name = new ArrayList<String>();


                try{
                    int galleons = Integer.parseInt(galleonsTF.getText());
                    int secles = Integer.parseInt(seclesTF.getText());
                    int knats = Integer.parseInt(knatsTF.getText());

                    if (!check.equals("")) {
                        name = storage.checkCashNumber(check);
                        ArrayList<Integer> balance = new ArrayList<Integer>(storage.getBalance(name.get(0),name.get(1),name.get(2)));
                        if (galleons > 0 || secles > 0 || knats > 0) {
                            if (balance.get(0) > galleons && balance.get(1) > secles && balance.get(2) > knats) {
                                storage.getMoneyFromCashSQL(name.get(0), name.get(1), name.get(2), galleons, secles, knats);
                                JOptionPane.showMessageDialog(null, "Money was taken from the Storage");
                            }
                            else JOptionPane.showMessageDialog(null,"There is no such amount of galleons/sickles/knuts");
                        }
                        else
                            JOptionPane.showMessageDialog(null, "Numbers must be positive. Please enter correct numbers.");
                    }
                    else {
                        JOptionPane.showMessageDialog(null, "Please enter check number.");
                    }
                }
                catch (NumberFormatException ex){
                    JOptionPane.showMessageDialog(null, "Incorrect numbers. Please enter correct numbers.");
                }

            }
        });
    }



}
