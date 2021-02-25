public class Commands extends SQL{

    private int userID;

    public boolean checkUser(String userName, String password){
        if(SQL.isRunning()){
            if(validUser(userName, password))
                userID = getUserID(userName, password);
                return true;
        }
        return false;
    }

    public void userOnline() {
        updateUserStatus(userID, 1);
    }

    public void userOffline(){
        updateUserStatus(userID, 0);
    }

}
