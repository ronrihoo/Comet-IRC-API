package comets.irc.object;

import java.util.ArrayList;

public class IrcSessionInfo implements InfoObject {

    int id;
    ArrayList<String> userList = null;
    ArrayList<String> postHistory = null;
    String provider = null;
    String server = null;
    String port = null;
    String channel = null;
    String nick = null;
    String connectionStatus = null;
    String loginStatus = null;
    String joinStatus = null;
    String status = null;

    public IrcSessionInfo() {
        postHistory = new ArrayList<>();
    }

    public IrcSessionInfo(String provider, String server, String port,
                          String channel, String nick, String status) {
        this.provider = provider;
        this.server = server;
        this.port = port;
        this.channel = channel;
        this.nick = nick;
        this.status = status;
        this.postHistory = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    @Override
    public void updateUserList(ArrayList<String> userList) {
        this.userList = userList;
    }

    public String getProvider() {
        return provider;
    }

    public IrcSessionInfo setProvider(String provider) {
        this.provider = provider;
        return this;
    }

    public String getServer() {
        return server;
    }

    public IrcSessionInfo setServer(String server) {
        this.server = server;
        return this;
    }

    public String getPort() {
        return port;
    }

    public IrcSessionInfo setPort(String port) {
        this.port = port;
        return this;
    }

    public String getChannel() {
        return channel;
    }

    public IrcSessionInfo setChannel(String channel) {
        this.channel = channel;
        return this;
    }

    public String getNick() {
        return nick;
    }

    public IrcSessionInfo setNick(String nick) {
        this.nick = nick;
        return this;
    }

    public String getConnectionStatus() {
        return connectionStatus;
    }

    public String getLoginStatus() {
        return loginStatus;
    }

    public String getJoinStatus() {
        return joinStatus;
    }

    public String getStatus() {
        return status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setConnectionStatus(String connectionStatus) {
        this.connectionStatus = connectionStatus;
    }

    public void setLoginStatus(String loginStatus) {
        this.loginStatus = loginStatus;
    }

    public void setJoinStatus(String joinStatus) {
        this.joinStatus = joinStatus;
    }

    @Override
    public void addPostToHistory(String post) {
       postHistory.add(post);
    }

    @Override
    public ArrayList<String> getPostHistory() {
        return this.postHistory;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
