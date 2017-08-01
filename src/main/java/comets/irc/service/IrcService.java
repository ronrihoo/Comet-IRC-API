package comets.irc.service;

import java.util.ArrayList;

public interface IrcService extends ConnectionService, MessageService {

    void quit();
    void addPostToHistory(String post);
    ArrayList<String> getPostHistory();
    ArrayList<String> getUserList();
    String getTimeStamp();

}
