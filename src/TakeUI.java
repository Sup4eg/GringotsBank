import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TakeUI extends JFrame{
    private JLabel clientInfoLB = new JLabel("");
    private JLabel startTimeLB = new JLabel("");
    private JLabel galleonsLB = new JLabel("Galleons: ");
    private JTextField galleonsTF = new JTextField(25);
    private JLabel seclesLB = new JLabel("Sickles: ");
    private JTextField seclesTF = new JTextField(25);
    private JLabel knatsLB = new JLabel("Knuts: ");
    private JTextField knatsTF = new JTextField(25);
    private JButton takeB = new JButton("Take money");
    private JButton finishB = new JButton("Finish visit");
    private boolean entity;

    public TakeUI(final String firstName, final String secondName, final String patronymic, final String startTime, boolean entityFlag){
        super("Place money");
        this.setBounds(200,100,500,250);
        //this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container container = this.getContentPane();
        container.setLayout(new GridLayout(5,2,2,2));
        clientInfoLB.setText("Client: " + firstName + " " + patronymic + " " + secondName);
        startTimeLB.setText("Start time of visit: " + startTime);
        entity = entityFlag;

        if (!entity) {
            galleonsLB.setVisible(false);
            galleonsTF.setVisible(false);
            seclesLB.setVisible(false);
            seclesTF.setVisible(false);
            knatsLB.setVisible(false);
            knatsTF.setVisible(false);
            takeB.setVisible(false);
        }

        container.add(clientInfoLB);
        container.add(startTimeLB);
        container.add(galleonsLB);
        container.add(galleonsTF);
        container.add(seclesLB);
        container.add(seclesTF);
        container.add(knatsLB);
        container.add(knatsTF);
        container.add(takeB);
        container.add(finishB);

        takeB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    Storage storage = new Storage();
                    int galleons = Integer.parseInt(galleonsTF.getText());
                    int secles = Integer.parseInt(seclesTF.getText());
                    int knats = Integer.parseInt(knatsTF.getText());

                    ArrayList<Integer> balance = new ArrayList<Integer>(storage.getBalance(firstName,secondName,patronymic));

                    if (galleons > 0 || secles > 0 || knats > 0) {
                        if (balance.get(0) > galleons && balance.get(1) > secles && balance.get(2) > knats) {
                            storage.getMoneyFromCashSQL(firstName, secondName, patronymic, galleons, secles, knats);
                            JOptionPane.showMessageDialog(null, "Money was taken from the Storage");
                        }
                        else JOptionPane.showMessageDialog(null,"There is no such amount of galleons/sickles/knuts");
                    }
                    else
                        JOptionPane.showMessageDialog(null, "Numbers must be positive. Please enter correct numbers.");
                }
                catch(NumberFormatException ex){
                    JOptionPane.showMessageDialog(null, "Incorrect numbers. Please enter correct numbers.");
                }
            }
        });

        finishB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                Journal journal = new Journal();
                int storageNumber;
                if (TakeUI.this.entity)
                    storageNumber = new Registration().getCurrentParametr(firstName, secondName, patronymic, "DBCASH", "STORAGE");
                else
                    storageNumber = new Registration().getCurrentParametr(firstName, secondName, patronymic, "DBUSER", "STORAGE");
                String endTime = dateFormat.format(new Date());
                journal.insertInParameters(storageNumber, startTime);
                journal.insertOutParameters(storageNumber, startTime, endTime);
                JOptionPane.showMessageDialog(null,"Visit is finished. Information was saved in Journal. Start time: " + startTime + " End Time: " + endTime);
                TakeUI.this.setVisible(false);
            }
        });
    }
}
