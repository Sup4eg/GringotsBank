import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

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
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        storage_journal.insertInParameters(new Registration().getCurrentParametr("Harry", "Potter", "James", "dbuser", "storage"),
                Calendar.getInstance().getTime().toString(), Calendar.getInstance().getTime().toString());
    }

    private Boolean insertInParameters(int storage, String start_time, String data) {
        String insertInData = "INSERT INTO STORAGE_JOURNAL"
                + "(STORAGE, START_TIME, DATA) "
                + "VALUES" + String.format("(%d, to_date('%s', 'dd.mm.yyyy'), to_date('%s', 'dd.mm.yyyy'))", storage, start_time, data);

        try {
            statement.executeUpdate(insertInData);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("Data wasn't insert to STORAGE_JOURNAL");
            return false;
        }
        return true;
    }

}
