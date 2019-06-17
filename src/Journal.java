import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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

//Test
    public static void main(String[] args) {
        Journal storage_journal = new Journal();
//        storage_journal.insertInParameters(new Registration().getCurrentParametr("Fleur", "Weasley", "Isabella", "dbcash", "storage"),
//                storage_journal.getCurrentTimeStorage(new Date(),"dd.MM.yyyy hh:mm:ss"));
//        storage_journal.insertOutParameters(new Registration().getCurrentParametr("Fleur", "Weasley", "Isabella", "dbcash", "storage"),
//                "13.06.2019 12:17:26", "27.02.2019 11:32:54");
//        System.out.println(storage_journal.getJournalByDay("16.06.2019").toString());
    }

//    Метод вставляет инофрмацию в журнал при заходе в ячейку (storage, start_time)

    public Boolean insertInParameters(int storage, String start_time) {
        String insertInData = "INSERT INTO STORAGE_JOURNAL"
                + "(STORAGE, START_TIME) "
                + "VALUES" + String.format("(%d, to_date('%s', 'dd.mm.yyyy hh24:mi:ss'))", storage, start_time);

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

    public Boolean insertOutParameters(int storage, String start_time, String end_time) {
        String updateParametersInTable = String.format("UPDATE STORAGE_JOURNAL SET END_TIME = to_date('%s', 'dd.mm.yyyy hh24:mi:ss') WHERE START_TIME = to_date('%s', 'dd.mm.yyyy hh24:mi:ss')", end_time, start_time);
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

//    Метод возвращает все записи в журнале за определенный день

    public Map getJournalByDay(String day_data) {
        String selectAllFromJournal = String.format("SELECT * FROM STORAGE_JOURNAL WHERE to_char(STORAGE_JOURNAL.START_TIME,'dd.mm.yyyy') = '%s'", day_data);
        Map<Integer, String[]> journal_map = new HashMap<Integer, String[]>();
        int id_row = 0;
        try {
            ResultSet rs = statement.executeQuery(selectAllFromJournal);
            while (rs.next()) {
                String storage = rs.getString("STORAGE");
                String start_time = rs.getString("START_TIME");
                String end_time = rs.getString("END_TIME");
                journal_map.put(id_row, new String[]{storage, start_time, end_time});
                id_row ++;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return journal_map;
    }

    //    Метод возвращает дату в нужном формате при работе с ячейкой хранилища

    public String getCurrentTimeStorage(Date time, String format) {
        DateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(time);
    }

}
