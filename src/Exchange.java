import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Exchange {

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

}
