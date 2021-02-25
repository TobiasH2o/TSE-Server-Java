public class Commands {

    SQL sql = new SQL();

    public int checkUser(String userName, String password){
        if(SQL.isRunning()){
            if(sql.validUser(userName, password))
                return sql.getUserID(userName, password);
        }
        return -1;
    }

    public void userOnline(int userID) {
        sql.updateUserStatus(userID, 1);
    }
}
