package comets.irc.consumer;

import java.util.ArrayList;

public interface IrcConsumer {

    int getId();
    void setNick(String nick);
    void setChannel(String channel);
    void setServer(String server);
    void setPort(int port);
    String getState();
    void connect();
    void connect(String server, int port);
    void login();
    void login(String channel, String nick, String pass);
    void login(String nick, String pass, String realName, String login,
               int mode);
    void join();
    void join(String channel);
    void idle();
    void send(String line);     // not saved in post history
    String receive();
    String processLine(String line);
    void post(String line);     // is saved in post history
    void print(String line);
    void ping();
    void pong();
    void leave();
    void logout();
    void disconnect();
    void quit();
    void updateUserList(ArrayList<String> userList);
    void setStateToConnecting();
    void setStateToConnected();
    void setStateToLoggingIn();
    void setStateToLoggedIn();
    void setStateToJoining();
    void setStateToJoined();
    void setStateToNotJoined();
    void setStateToLeftChannel();
    void setStateToNotLoggedIn();
    void setStateToLoggedOut();
    void setStateToNotConnected();
    void setStateToDisconnected();
    void setCurrentSessionInfo(String provider, String channel, String nick,
                               String connectionStatus, String loginStatus,
                               String joinStatus);
    ArrayList<String> getPostHistory();
    // user list events
    void whois(String user);
    void ignore(String user);
    void unignore(String user);
    void saveUser(String user);
    void queryUser(String user);
    // client
    IrcConsumer getIrcClient();

}
