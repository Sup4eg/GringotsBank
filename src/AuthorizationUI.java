import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AuthorizationUI extends JFrame {
    private JTextField firstNameTF = new JTextField("", 25);
    private JLabel firstNameLB = new JLabel("* First Name:");
    private JTextField secondNameTF = new JTextField("", 25);
    private JLabel secondNameLB = new JLabel("* Last Name:");
    private JTextField patronymicTF = new JTextField("", 25);
    private JLabel patronymicLB = new JLabel("* Second Name:");

    private JTextField lengthTF = new JTextField("", 25);
    private JLabel lengthLB = new JLabel("* Length (inches):");
    private JTextField woodTF = new JTextField("", 25);
    private JLabel woodLB = new JLabel("* Wood:");
    private JTextField coreTF = new JTextField("", 25);
    private JLabel coreLB = new JLabel("* Core:");
    private JTextField madeByTF = new JTextField("", 25);
    private JLabel madeByLB = new JLabel("* Made by:");

    private JButton authB = new JButton("Enter");


    public AuthorizationUI(){
        super("Authorization form");
        this.setBounds(200,100,300,250);
       // this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container container = this.getContentPane();
        container.setLayout(new GridLayout(8,2,2,2));

        container.add(firstNameLB);
        container.add(firstNameTF);
        container.add(secondNameLB);
        container.add(secondNameTF);
        container.add(patronymicLB);
        container.add(patronymicTF);

        container.add(lengthLB);
        container.add(lengthTF);
        container.add(woodLB);
        container.add(woodTF);
        container.add(coreLB);
        container.add(coreTF);
        container.add(madeByLB);
        container.add(madeByTF);

        authB.addActionListener(new AuthorizationButtonAction());
        container.add(authB);

    }

    public static void main(String[] args) {
        AuthorizationUI app = new AuthorizationUI();
        app.setVisible(true);
    }

    class AuthorizationButtonAction implements ActionListener{
        Registration reg = new Registration();
        private String firstName, secondName, patronymic, wood, core, madeBy;
        private int length;

        public void actionPerformed(ActionEvent e){
            try {
                firstName = firstNameTF.getText().trim();
                secondName = secondNameTF.getText().trim();
                patronymic = patronymicTF.getText().trim();
                length = Integer.parseInt(lengthTF.getText());
                wood = woodTF.getText();
                core = coreTF.getText();
                madeBy = madeByTF.getText();

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

                if (firstName.equals("") || secondName.equals("") || patronymic.equals("") ||
                        wood.equals("") || core.equals("") || madeBy.equals("") ||
                        length < 0)
                    JOptionPane.showMessageDialog(null, "Some fields are empty or incorrect. Please fill all of the form fields correctly.");
                else {
                    if (!reg.insertWandParametersSQL(firstName, secondName, patronymic, length, wood, core, madeBy)) {
                        JOptionPane.showMessageDialog(null, "Client is authorised ");
                        AuthorizationUI.this.setVisible(false);
                        new StorageUI(firstName,secondName,patronymic, dateFormat.format(new Date())).setVisible(true);
                    }
                    else{
                        JOptionPane.showMessageDialog(null, "Client does not exist. Please, register client.");
                        AuthorizationUI.this.setVisible(false);
                        new RegistrationUI().setVisible(true);
                    }
                }
            }
            catch(NumberFormatException ex){
                JOptionPane.showMessageDialog(null, "Incorrect numbers");
            }
        }
    }
}
