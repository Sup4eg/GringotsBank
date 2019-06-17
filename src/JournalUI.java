import javax.swing.*;
import java.util.HashMap;
import java.util.Map;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class JournalUI extends JFrame{
    Object[] headers = { "Storage cell", "Start time of visit", "End time of visit" };
    Journal journal = new Journal();
    private JTextField dateTF = new JTextField("dd.MM.yyyy",20);
    private JButton showJournalB = new JButton("Show Journal");
    private JTextArea journalTA = new JTextArea(20,20);

    public JournalUI(){
        super("Currency information");
        this.setBounds(200,100,300,450);
        //this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        final Container container = this.getContentPane();
        container.setLayout(new FlowLayout());
        journalTA.setEditable(false);
        JScrollPane scroll = new JScrollPane(journalTA);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        container.add(dateTF);
        container.add(showJournalB);
        container.add(scroll);
       // container.add(journalTA);

        showJournalB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(dateTF.getText().equals(""))
                    JOptionPane.showMessageDialog(null,"Input date");
                else{
                    Map<Integer, String[]> journalMap = new HashMap<Integer, String[]>();
                    journalMap = journal.getJournalByDay(dateTF.getText());
                   // journalMap.put(1,new String[]{"1","2","3"});
                   // journalMap.put(2,new String[]{"4","5","6"});
                   // journalMap.put(3,new String[]{"sdf","sdf","sdf"});
                    journalTA.setText("Storage cell, start time, end time\r\n\r\n");
                    for (String[] value:journalMap.values()) {
                        for (int i = 0; i < 3; i++)
                            journalTA.append(value[i] + ", ");
                        journalTA.append("\r\n");
                    }
                }
            }
        });

    }

    public static void main(String[] args) {
        JournalUI app = new JournalUI();
        app.setVisible(true);
    }


}
