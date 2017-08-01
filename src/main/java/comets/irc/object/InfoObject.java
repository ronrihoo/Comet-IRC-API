package comets.irc.object;

import java.util.ArrayList;

public interface InfoObject {

    int getId();
    void updateUserList(ArrayList<String> userList);
    void setConnectionStatus(String status);
    void setLoginStatus(String status);
    void setJoinStatus(String status);
    void addPostToHistory(String post);
    void addUserToList(String user);
    void setUserList(ArrayList<String> userList);
    ArrayList<String> getPostHistory();
    ArrayList<String> getUserList();


}
