import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MainForm extends JFrame{
    private JButton storageB = new JButton("Storage operations");
    private JButton registerB = new JButton("Register new client");
    private JButton cashCheckB = new JButton("Cash the check");
    private JButton journalB = new JButton("View visitors journal");
    private JButton exchangeB = new JButton("Currency exchange");

    public MainForm(){
        super("Gringotts Bank");
        this.setBounds(200,100,300,300);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container mainFormContainer = this.getContentPane();
        mainFormContainer.setLayout(new GridLayout(7,1,5,5));

        mainFormContainer.add(new JLabel(""));
        mainFormContainer.add(storageB);
        mainFormContainer.add(registerB);
        mainFormContainer.add(cashCheckB);
        mainFormContainer.add(journalB);
        mainFormContainer.add(exchangeB);

        storageB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AuthorizationUI().setVisible(true);
            }
        });

        registerB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new RegistrationUI().setVisible(true);
            }
        });

        journalB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new JournalUI().setVisible(true);
            }
        });

        exchangeB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ExchangeUI().setVisible(true);
            }
        });

        cashCheckB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new CashCheckUI().setVisible(true);
            }
        });
    }

    public static void main(String[] args) {
        MainForm app = new MainForm();
        app.setVisible(true);
    }
}
