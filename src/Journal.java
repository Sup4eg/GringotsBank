import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Journal extends Registration{

    private Connection dbConnection = null;
    private Statement statement = null;

    {
        try {
            dbConnection = ConnectionDB.getDBConnection();
            statement = dbConnection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Journal storage_journal = new Journal();
//        storage_journal.insertInParameters(new Registration().getCurrentParametr("Fleur", "Weasley", "Isabella", "dbcash", "storage"),
//                storage_journal.getCurrentTimeStorage(new Date(),"dd.MM.yyyy hh:mm:ss"));
        storage_journal.insertOutParameters(new Registration().getCurrentParametr("Fleur", "Weasley", "Isabella", "dbcash", "storage"),
                "13.06.2019 12:17:26", "27.02.2019 11:32:54");
    }

//    Метод вставляет инофрмацию в журнал при заходе в ячейку (storage, start_time)

    private Boolean insertInParameters(int storage, String start_time) {
        String insertInData = "INSERT INTO STORAGE_JOURNAL"
                + "(STORAGE, START_TIME) "
                + "VALUES" + String.format("(%d, to_date('%s', 'dd.mm.yyyy hh:mi:ss'))", storage, start_time);

        try {
            statement.executeUpdate(insertInData);
            System.out.println("Input data was inserted !");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("Input data wasn't insert to STORAGE_JOURNAL");
            return false;
        }
        return true;
    }

    //    Метод вставляет инофрмацию в журнал при выходе из ячейки (storage, start_time)

    private Boolean insertOutParameters(int Storage, String start_time, String end_time) {
        String updateParametersInTable = String.format("UPDATE STORAGE_JOURNAL SET END_TIME = to_date('%s', 'dd.mm.yyyy hh:mi:ss') WHERE START_TIME = to_date('%s', 'dd.mm.yyyy hh:mi:ss')", end_time, start_time);
        try {
            statement.executeUpdate(updateParametersInTable);
            System.out.println("Out data was inserted !");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("Out data wasn't insert to STORAGE_JOURNAL");
            return false;
        }
        return true;
    }

    //    Метод возвращает дату в нужном формате при работе с ячейкой хранилища

    private String getCurrentTimeStorage(Date time, String format) {
        DateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(time);
    }

}
