import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SQL {

    private Connection con;
    private final String password = "UHjSDJeJ3kFMDFxn";
    private final String username = "JavaInterface";
    private String url = "jdbc:mysql://localhost:3306";;

    SQL(){

        Log.logLine("Checking server on " + url);
        try {
            con = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            Log.logLine(e.getMessage());
            Log.logLine("Error connecting to SQL database on " + url);
        }
    }

    public boolean checkServerStatus(){
        try {
            con = DriverManager.getConnection(url, username, password);
            Statement s = con.createStatement();
            s.executeQuery("SELECT 1 AS A");
            return true;
        } catch (SQLException e) {
            Log.logLine(e.getMessage());
            Log.logLine("Error connecting to SQL database on " + url);
            return false;
        }
    }

}
