package comets.irc.consumer;

import comets.irc.service.IrcService;
import comets.irc.service.UserInterfaceService;
import comets.irc.util.Constants;

import java.util.ArrayList;

public abstract class AbstractIrcClient implements IrcConsumer {

    protected IrcService ircService = null;
    protected UserInterfaceService uiService = null;
    protected String nick = null;
    protected String channel = null;
    protected String server = null;
    protected int port = 0;
    protected String state = null;

    public AbstractIrcClient(IrcService ircService, UserInterfaceService uiService) {
        this.ircService = ircService;
        this.uiService = uiService;
        setStateToNotConnected();
    }

    @Override
    public void setNick(String nick) {
        this.nick = nick;
    }

    @Override
    public void setChannel(String channel) {
        this.channel = channel;
    }

    @Override
    public void setServer(String server) {
        this.server = server;
    }

    @Override
    public void setPort(int port) {
        this.port = port;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getState() {
        return this.state;
    }

    @Override
    public void connect() {
        setStateToConnecting();
        if (this.server != null && this.port != 0) {
            if (!ircService.connect(this.server, this.port)) {
                setStateToNotConnected();
            }
        }
    }

    @Override
    public void connect(String server, int port) {
        setStateToConnecting();
        if (!ircService.connect(server, port)) {
            setStateToNotConnected();
        }
    }

    @Override
    public void join() {

    }

    @Override
    public void join(String channel) {

    }

    @Override
    public void login() {

    }

    @Override
    public void login(String channel, String nick, String pass) {

    }

    @Override
    public void login(String nick, String pass, String realName, String login,
                      int mode) {

    }

    @Override
    public void idle() {

    }

    @Override
    public void send(String line) {
        ircService.sendLine(line);
    }

    @Override
    public String receive() {
        return ircService.readLine();
    }

    @Override
    public String processLine(String line) {
        return null;
    }

    @Override
    public void post(String line) {
        send(line);
    }

    @Override
    public void print(String line) {
        uiService.printLine(line);
    }

    @Override
    public void leave() {
        setStateToLeftChannel();
    }

    @Override
    public void disconnect() {
        ircService.disconnect();
    }

    @Override
    public void quit() {
        disconnect();
        ircService.quit();
    }

    @Override
    public void addUserToList(String user) {
        uiService.addUserToList(user);
    }

    @Override
    public void updateUserList(ArrayList<String> userList) {
        uiService.updateUserList(userList);
    }

    @Override
    public void setStateToConnecting() {
        setState(Constants.CONNECTING_STATUS);
        uiService.setCurrentConnectionStatus(Constants.CONNECTING_STATUS);
    }

    @Override
    public void setStateToConnected() {
        setState(Constants.CONNECTED_STATUS);
        uiService.setCurrentConnectionStatus(Constants.CONNECTED_STATUS);
    }

    @Override
    public void setStateToLoggingIn() {
        setState(Constants.LOGGING_IN_STATUS);
        uiService.setCurrentLoginStatus(Constants.LOGGING_IN_STATUS);
    }

    @Override
    public void setStateToLoggedIn() {
        setState(Constants.LOGGED_IN_STATUS);
        uiService.setCurrentLoginStatus(Constants.LOGGED_IN_STATUS);
    }

    @Override
    public void setStateToJoining() {
        setState(Constants.JOINING_CHANNEL_STATUS);
        uiService.setCurrentJoinStatus(Constants.JOINING_CHANNEL_STATUS);
    }

    @Override
    public void setStateToJoined() {
        setState(Constants.JOINED_CHANNEL_STATUS);
        uiService.setCurrentJoinStatus(Constants.JOINED_CHANNEL_STATUS);
    }

    @Override
    public void setStateToNotJoined() {
        setState(Constants.NOT_JOINED_CHANNEL_STATUS);
        uiService.setCurrentJoinStatus(Constants.NOT_JOINED_CHANNEL_STATUS);
        setStateToNotConnected();
    }

    @Override
    public void setStateToLeftChannel() {
        setState(Constants.LEFT_CHANNEL_STATUS);
        uiService.setCurrentJoinStatus(Constants.LEFT_CHANNEL_STATUS);
        setStateToLoggedOut();
    }

    @Override
    public void setStateToNotLoggedIn() {
        setState(Constants.NOT_LOGGED_IN_STATUS);
        uiService.setCurrentLoginStatus(Constants.NOT_LOGGED_IN_STATUS);
    }

    @Override
    public void setStateToLoggedOut() {
        setState(Constants.LOGGED_OUT_STATUS);
        uiService.setCurrentLoginStatus(Constants.LOGGED_OUT_STATUS);
        setStateToDisconnected();
    }

    @Override
    public void setStateToNotConnected() {
        setState(Constants.NOT_CONNECTED_STATUS);
        uiService.setCurrentConnectionStatus(Constants.NOT_CONNECTED_STATUS);
        uiService.setCurrentLoginStatus(Constants.NOT_LOGGED_IN_STATUS);
        uiService.setCurrentJoinStatus(Constants.NOT_JOINED_CHANNEL_STATUS);
    }

    @Override
    public void setStateToDisconnected() {
        disconnect();
        setState(Constants.DISCONNECTED_STATUS);
        uiService.setCurrentConnectionStatus(Constants.DISCONNECTED_STATUS);
    }

    @Override
    public IrcConsumer getIrcClient() {
        return this;
    }

    public UserInterfaceService getGuiService() {
        return uiService;
    }

    @Override
    public ArrayList<String> getPostHistory() {
        return ircService.getPostHistory();
    }

    @Override
    public ArrayList<String> getUserList() {
        return ircService.getUserList();
    }

    @Override
    public void whois(String user) {

    }

    @Override
    public void ignore(String user) {

    }

    @Override
    public void saveUser(String user) {

    }

    @Override
    public void queryUser(String user) {

    }

}
