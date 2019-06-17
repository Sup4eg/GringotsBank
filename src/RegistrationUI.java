import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class RegistrationUI extends JFrame {
    private String[] sexItems = {"Man", "Woman"};
    private String[] bloodStatusItems = {"Pure-blood", "Half-blood","Muggle-born"};
    private String[] storageLevelItems = {"1 (Individual)", "2 (Individual)","3 (Individual)","4 (Entity)"};

    private JLabel clientLB = new JLabel("   Input Client information");
    private JTextField firstNameTF = new JTextField("", 25);
    private JLabel firstNameLB = new JLabel(" * First Name:");
    private JTextField secondNameTF = new JTextField("", 25);
    private JLabel secondNameLB = new JLabel(" * Last Name:");
    private JTextField patronymicTF = new JTextField("", 25);
    private JLabel patronymicLB = new JLabel(" * Second Name:");
    private JComboBox sexCB = new JComboBox(sexItems);

    private JLabel sexLB = new JLabel(" * Sex:");

    private JComboBox bloodStatusCB = new JComboBox(bloodStatusItems);
    private JLabel bloodStatusLB = new JLabel(" * Blood Status:");
    private JTextField jobTF = new JTextField("", 25);
    private JLabel jobLB = new JLabel(" * Job:");

    private JComboBox storageLevelCB = new JComboBox(storageLevelItems);

    private JLabel storageLevelLB = new JLabel(" * Storage Level:");
    private JTextField dateOfBirthTF = new JTextField("dd.mm.yyyy", 25);
    private JLabel dateOfBirthLB = new JLabel(" * Date of Birth:");

    private JLabel addressLB = new JLabel("   Input Address");
    private JTextField cityTF = new JTextField("", 25);
    private JLabel cityLB = new JLabel(" City:");
    private JTextField streetTF = new JTextField("", 25);
    private JLabel streetLB = new JLabel(" * Street:");
    private JTextField areaTF = new JTextField("", 25);
    private JLabel areaLB = new JLabel(" * Area:");
    private JTextField homeTF = new JTextField("", 25);
    private JLabel homeLB = new JLabel(" * House number:");
    private JTextField flatTF = new JTextField("", 25);
    private JLabel flatLB = new JLabel(" Flat number:");

    private JLabel wandLB = new JLabel("   Input Wand parameters");
    private JTextField lengthTF = new JTextField("", 25);
    private JLabel lengthLB = new JLabel(" * Length (inches):");
    private JTextField woodTF = new JTextField("", 25);
    private JLabel woodLB = new JLabel(" * Wood:");
    private JTextField coreTF = new JTextField("", 25);
    private JLabel coreLB = new JLabel(" * Core:");
    private JTextField madeByTF = new JTextField("", 25);
    private JLabel madeByLB = new JLabel(" * Made by:");

    private JButton registerB = new JButton("Register");

    public RegistrationUI(){
        super("Registration form");
        this.setBounds(200,100,350,550);
        //this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container container = this.getContentPane();
        container.setLayout(new GridLayout(22,2,2,2));

        container.add(clientLB);
        container.add(new Label(""));
        container.add(firstNameLB);
        container.add(firstNameTF);
        container.add(secondNameLB);
        container.add(secondNameTF);
        container.add(patronymicLB);
        container.add(patronymicTF);
        container.add(sexLB);

        container.add(sexCB);
        container.add(bloodStatusLB);

        container.add(bloodStatusCB);
        container.add(jobLB);
        container.add(jobTF);
        container.add(storageLevelLB);

        container.add(storageLevelCB);
        container.add(dateOfBirthLB);
        container.add(dateOfBirthTF);

        container.add(addressLB);
        container.add(new Label(""));
        container.add(cityLB);
        container.add(cityTF);
        container.add(streetLB);
        container.add(streetTF);
        container.add(areaLB);
        container.add(areaTF);
        container.add(homeLB);
        container.add(homeTF);
        container.add(flatLB);
        container.add(flatTF);

        container.add(wandLB);
        container.add(new Label(""));
        container.add(lengthLB);
        container.add(lengthTF);
        container.add(woodLB);
        container.add(woodTF);
        container.add(coreLB);
        container.add(coreTF);
        container.add(madeByLB);
        container.add(madeByTF);

        container.add(new JLabel(""));
        container.add(new JLabel("*Fields must be filled"));

        registerB.addActionListener(new RegisterButtonAction());
        container.add(registerB);

    }

    public static void main(String[] args) {
        RegistrationUI app = new RegistrationUI();
        app.setVisible(true);
    }

    class RegisterButtonAction implements ActionListener{
        Registration reg = new Registration();
        Storage storage = new Storage();
        private String firstName, secondName, patronymic, sex, bloodStatus, job, dateOfBirth,
                city, street, area,
                wood, core, madeBy;
        private int storageLevel, home, flat, length;

        @Override
        public void actionPerformed(ActionEvent e){
            try {
                firstName = firstNameTF.getText().trim();
                secondName = secondNameTF.getText().trim();
                patronymic = patronymicTF.getText().trim();
                sex = sexCB.getSelectedItem().toString();
                bloodStatus = bloodStatusCB.getSelectedItem().toString();
                job = jobTF.getText().trim();
                dateOfBirth = dateOfBirthTF.getText().trim();
                storageLevel = storageLevelCB.getSelectedIndex() + 1;// Integer.parseInt(storageLevelTF.getText());

                SimpleDateFormat stringDate = new SimpleDateFormat("dd.MM.yyyy");
                Calendar birthDay = Calendar.getInstance();
                Date date = stringDate.parse(dateOfBirth);
                Calendar now = Calendar.getInstance();
                birthDay.setTime(date);

                city = cityTF.getText();
                street = streetTF.getText();
                area = areaTF.getText();
                home = Integer.parseInt(homeTF.getText());
                if (flatTF.getText().equals(""))
                    flat = 0;
                else
                    flat = Integer.parseInt(flatTF.getText());

                length = Integer.parseInt(lengthTF.getText());
                wood = woodTF.getText();
                core = coreTF.getText();
                madeBy = madeByTF.getText();

                if (firstName.equals("") || secondName.equals("") || patronymic.equals("") || sex.equals("") || bloodStatus.equals("") || job.equals("") || dateOfBirth.equals("") ||
                        street.equals("") || area.equals("") ||
                        wood.equals("") || core.equals("") || madeBy.equals("") ||
                        storageLevel < 1 || storageLevel > 4 || home < 0 || flat < 0 || length < 0) //проверка на заполнение полей
                    JOptionPane.showMessageDialog(null, "Some fields are empty or incorrect. Please fill all of the form fields correctly.");
                else{
                    if ((now.get(Calendar.YEAR) - birthDay.get(Calendar.YEAR)) > 11) { //проверка на возраст
                        boolean isInsertClientSQL = reg.insertClientSQL(firstName, secondName, patronymic, sex, bloodStatus,
                                job, storageLevel, dateOfBirth);
                        boolean isInsertAddressSQL = reg.insertAddressSQL(firstName, secondName, patronymic, city, street, area, home, flat);
                        boolean isInsertWandParametersSQL = reg.insertWandParametersSQL(firstName, secondName, patronymic, length, wood, core, madeBy);
                        if (storageLevel == 4) {
                            storage.insertClientToCashSQL(firstName, secondName, patronymic);
                        }
                        if (isInsertAddressSQL && isInsertWandParametersSQL
                                 && isInsertClientSQL
                                ) {
                            JOptionPane.showMessageDialog(null, "Client is registered ");
                            RegistrationUI.this.setVisible(false);
                        } else JOptionPane.showMessageDialog(null, "Client was not registered");
                }
                    else JOptionPane.showMessageDialog(null,"Not an appropriate age. Client must be older than 11 years old");
               }
            }
            catch (NumberFormatException ex){
                JOptionPane.showMessageDialog(null, "Incorrect numbers");
            }
            catch (ParseException ex){
                JOptionPane.showMessageDialog(null, "Incorrect date of birth");
            }

        }
    }
}
