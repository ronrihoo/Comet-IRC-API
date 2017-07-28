package comets.irc.service;

import comets.irc.object.IrcSessionInfo;
import comets.irc.util.Constants;

import java.util.ArrayList;

public class UserInterface implements UserInterfaceService {

    private IrcSessionInfo ircSessionInfo = null;

    public UserInterface() {
        initialize();
        ircSessionInfo.setServer(Constants.DEFAULT_SERVER);
        ircSessionInfo.setPort(Constants.DEFAULT_PORT);
    }

    public UserInterface(String server, int port) {
        initialize();
        ircSessionInfo.setServer(server);
        ircSessionInfo.setPort(String.valueOf(port));
    }

    public void initialize() {
        ircSessionInfo = new IrcSessionInfo();
    }

    public void setCurrentSessionInfo(String server, int port) {
        if (ircSessionInfo == null) {
            initialize();
        }
        ircSessionInfo.setServer(server);
        ircSessionInfo.setPort(String.valueOf(port));
    }

    public void setCurrentSessionInfo(String server, int port, String channel, String nick) {
        if (ircSessionInfo == null) {
            initialize();
        }
        ircSessionInfo.setServer(server);
        ircSessionInfo.setPort(String.valueOf(port));
        ircSessionInfo.setChannel(channel);
        ircSessionInfo.setNick(nick);
    }

    public void dropCurrentSessionInfo() {
        ircSessionInfo = null;
    }

    public void printLine(String line) {
        System.out.println(line);
    }

    public void connectionSuccessful() {
        printLine(Constants.CONNECTED_TO + ircSessionInfo.getServer() + ":" + ircSessionInfo.getPort());
    }

    public void connectionSuccessful(String server, int port) {
        printLine(Constants.CONNECTED_TO + server + ":" + String.valueOf(port));
    }

    public void connectionRefused() {
        printLine(Constants.CONNECTION_REFUSED_BY + ircSessionInfo.getServer() + ":" + ircSessionInfo.getPort());
    }

    public void connectionRefused(String server, int port) {
        printLine(Constants.CONNECTION_REFUSED_BY + server + ":" + String.valueOf(port));
    }

    public void unableToConnect() {
        printLine(Constants.UNABLE_TO_CONNECT_TO + ircSessionInfo.getServer() + ":" + ircSessionInfo.getPort());
    }

    public void unableToConnect(String server, int port) {
        printLine(Constants.UNABLE_TO_CONNECT_TO + server + ":" + String.valueOf(port));
    }

    public void printReadError() {
        printLine(Constants.IO_ERROR + ircSessionInfo.getServer() + ":" + ircSessionInfo.getPort());
    }

    public void printReadError(String server, int port) {
        printLine(Constants.IO_ERROR + server + ":" + String.valueOf(port));
    }

    @Override
    public String getNick() {
        return ircSessionInfo.getNick();
    }

    @Override
    public String getChannel() {
        return ircSessionInfo.getChannel();
    }

    @Override
    public void updateUserList(ArrayList<String> userList) {

    }

    @Override
    public void setCurrentConnectionStatus(String status) {

    }

    @Override
    public void setCurrentLoginStatus(String status) {

    }

    @Override
    public void setCurrentJoinStatus(String status) {

    }

    @Override
    public void addPostToHistory(String post) {
        ircSessionInfo.addPostToHistory(post);
    }

    @Override
    public ArrayList<String> getPostHistory() {
        return ircSessionInfo.getPostHistory();
    }

}
