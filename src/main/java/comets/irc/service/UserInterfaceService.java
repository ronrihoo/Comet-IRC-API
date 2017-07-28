package comets.irc.service;

import java.util.ArrayList;

public interface UserInterfaceService {

    void setCurrentSessionInfo(String server, int port);
    void setCurrentSessionInfo(String server, int port, String channel, String nick);
    void dropCurrentSessionInfo();
    void printLine(String line);
    void connectionSuccessful();
    void connectionSuccessful(String server, int port);
    void connectionRefused();
    void connectionRefused(String server, int port);
    void unableToConnect();
    void unableToConnect(String server, int port);
    void printReadError();
    void printReadError(String server, int port);
    String getNick();
    String getChannel();
    void updateUserList(ArrayList<String> userList);
    void setCurrentConnectionStatus(String status);
    void setCurrentLoginStatus(String status);
    void setCurrentJoinStatus(String status);

}