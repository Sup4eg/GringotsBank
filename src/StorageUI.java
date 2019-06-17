import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class StorageUI extends JFrame{
    private JButton placeB = new JButton("Place money to the Storage");
    private JButton takeB = new JButton("Take money from the Storage");
    private JButton infoB = new JButton("Balance information");
    private boolean entity = true;

    public StorageUI(final String firstName, final String secondName, final String patronymic, final String startTime){
        super("Storage operations");
        this.setBounds(200,100,300,300);
        //this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container container = this.getContentPane();
        container.setLayout(new GridLayout(5,1,5,5));

        Storage storage = new Storage();

        entity = storage.getCashPermission(firstName,secondName,patronymic); //Флаг: true - юридическое лицо, false - физическое

        if (!entity)
            infoB.setEnabled(false);

        container.add(new Label(""));
        container.add(placeB);
        container.add(takeB);
        container.add(infoB);

        placeB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new PlaceUI(firstName, secondName, patronymic, startTime, entity).setVisible(true);
            }
        });

        takeB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new TakeUI(firstName, secondName, patronymic, startTime, entity).setVisible(true);
            }
        });

        infoB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new balanceInfoUI(firstName, secondName, patronymic).setVisible(true);
            }
        });
    }
}

