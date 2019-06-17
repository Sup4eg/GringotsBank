import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class balanceInfoUI extends JFrame {
    JLabel clientInfoLB = new JLabel("");
    private JLabel galleonsLB = new JLabel("Galleons: ");
    private JTextField galleonsTF = new JTextField(25);
    private JLabel seclesLB = new JLabel("Sickles: ");
    private JTextField seclesTF = new JTextField(25);
    private JLabel knatsLB = new JLabel("Knuts: ");
    private JTextField knatsTF = new JTextField(25);

    public balanceInfoUI(String firstName, String secondName, String patronymic){
        super("Balance information");
        this.setBounds(200,100,400,250);
        //this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container container = this.getContentPane();
        container.setLayout(new GridLayout(4,2,2,2));

        clientInfoLB.setText("Client: " + firstName + " " + patronymic + " " + secondName);
        Storage storage = new Storage();
        ArrayList<Integer> money = new ArrayList<Integer>(storage.getBalance(firstName, secondName, patronymic));
        Integer galleons = money.get(0);
        Integer sickles = money.get(1);
        Integer knuts = money.get(2);
        galleonsTF.setText(galleons.toString());
        seclesTF.setText(sickles.toString());
        knatsTF.setText(knuts.toString());

        container.add(clientInfoLB);
        container.add(new JLabel(""));
        container.add(galleonsLB);
        container.add(galleonsTF);
        container.add(seclesLB);
        container.add(seclesTF);
        container.add(knatsLB);
        container.add(knatsTF);

        galleonsTF.setEnabled(false);
        seclesTF.setEnabled(false);
        knatsTF.setEnabled(false);

    }
}
