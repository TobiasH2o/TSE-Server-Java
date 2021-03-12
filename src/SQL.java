import java.sql.*;
import java.util.ArrayList;

public class SQL {

    static private Connection con;
    private final String password = "UHjSDJeJ3kFMDFxn";
    private final String username = "JavaInterface";
    private final String url = "jdbc:mysql://localhost:3306";
    static private boolean running = false;

    SQL(){ }

    public void checkServerStatus(){
        try {
            con = DriverManager.getConnection(url, username, password);
            Statement s = con.createStatement();
            s.executeQuery("SELECT 1 AS A");
            running = true;
        } catch (SQLException e) {
            Log.logLine(e.getMessage());
            Log.logLine("Error connecting to SQL database on " + url);
            running = false;
        }
    }

    public static boolean isRunning(){
        return running;
    }

    private String[][] sendQuery(String statement) {
        Connection con = null;
        String[][] result = null;
        try {
            con = DriverManager.getConnection(url, username, password);
            for (String statement2 : statement.split(";")) {
                result = _sendQuery(con, statement2.trim());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                assert con != null;
                con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    private String[][] _sendQuery(Connection con, String statement) {
        try {
            // Establishes connection to the server
            Statement s = con.createStatement();
            if (statement.contains("SELECT") || statement.contains("SHOW")) {
                ResultSet rs = s.executeQuery(statement);
                return parseResultSet(rs);
            } else {
                s.executeUpdate(statement);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String[][] parseResultSet(ResultSet rs) {
        String[][] result = null;
        try {
            int width = rs.getMetaData().getColumnCount();
            ArrayList<String> serverResponse = new ArrayList<>(0);
            while (rs.next()) {
                StringBuilder s1 = new StringBuilder();
                for (int i = 1;i <= width;i++) {
                    s1.append((rs.getString(i) + " ").trim()).append("~~");
                }
                s1.deleteCharAt(s1.length() - 1);
                s1.deleteCharAt(s1.length() - 1);
                serverResponse.add(s1.toString());
            }
            String[][] returnData = new String[serverResponse.size()][width];
            for (int i = 0;i < serverResponse.size();i++) {
                returnData[i] = serverResponse.get(i).split("~~").clone();
            }
            result = returnData;
        } catch (Exception ignored) {

        }
        return result;
    }

    public boolean makeConnection(){
        Log.logLine("Checking server on " + url);
        try {
            con = DriverManager.getConnection(url, username, password);
            running = true;
            return true;
        } catch (SQLException e) {
            Log.logLine(e.getMessage());
            Log.logLine("Error connecting to SQL database on " + url);
            return false;
        }
    }

    public void updateUserStatus(int userID, int online){
        sendQuery("USE A;UPDATE userInfo SET `online` = '" + online + "' WHERE `userID` = '" + userID + "';");
    }

    public boolean validUser(String username, String password){
        return sendQuery("USE A;SELECT `userInfo`.`userID` FROM `userInfo` WHERE `userName` = '" + username + "' AND `password` = '" + password + "';").length > 0;
    }

    public int getUserID(String userName, String password) {
        return Integer.parseInt(sendQuery("USE A;SELECT `userID` FROM `userInfo` WHERE `userName` = '" + userName + "' AND `password` = '" + password + "';")[0][0]);
    }
}
