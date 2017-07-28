package comets.irc.consumer;

import comets.irc.object.Irc;
import comets.irc.service.IrcService;
import comets.irc.service.UserInterfaceService;
import comets.irc.util.Constants;

import java.util.ArrayList;

public class IrcClient extends AbstractIrcClient {

    Irc irc = null;
    int id;

    public IrcClient(IrcService ircService, UserInterfaceService uiService, Irc irc) {
        super(ircService, uiService);
        this.irc = irc;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setNick(String nick) {
        super.setNick(nick);
        irc.setNick(nick);
    }

    @Override
    public void setChannel(String channel) {
        super.setChannel(channel);
        irc.setChannel(channel);
    }

    public String getState() {
        return state;
    }

    @Override
    public void connect() {
        super.connect();
        String line = null;
        setStateToConnecting();
        while ((line = receive()) != null) {
            processLine(line);
            if (irc.listenForJoinConfirmation(line) != null) {
                setStateToConnected();
                break;
            }
        }
    }

    @Override
    public void connect(String server, int port) {
        super.connect(server, port);
        String line = null;
        setStateToConnecting();
        while ((line = receive()) != null) {
            processLine(line);
            if (irc.listenForJoinConfirmation(line) != null) {
                setStateToConnected();
                break;
            }
        }
    }

    public void login() {
        login(irc.getNick(), "", "", "", 8);
    }

    @Override
    public void login(String nick, String pass, String realName, String login,
                      int mode) {
        String line = null;
        if (state.equals(Constants.CONNECTED_STATUS)) {
            setStateToLoggingIn();
            send(irc.login(nick, pass, realName, login, mode));
            while ((line = receive()) != null) {
                line = irc.listenForLoginConfirmation(line);
                if (line.equals(irc.LOGIN_CONFIRMATION_CODE)) {
                    setStateToLoggedIn();
                    break;
                } else if (line.equals(irc.NICKNAME_ALREADY_IN_USE)) {
                    setStateToNotConnected();
                    print(line);
                    print(irc.NICKNAME_ALREADY_IN_USE_PROMPT);
                    line = null;
                    break;
                } else if (irc.isReadable()) {
                    print(line);
                }
            }
            if (state.equals(Constants.CONNECTED_STATUS)) {
                state = Constants.NOT_LOGGED_IN_STATUS;
            }
        }
        // receive messages
        if (line != null) {
            while ((line = receive()) != null) {
                line = irc.reformatLine(line);
                if (line != null) {
                    if (irc.isWritable()) {
                        send(line);
                    } else if (irc.isLoggedIn()) {
                        break;
                    } else {
                        print(line);
                    }
                }
            }
            setStateToConnected();
        }
    }

    @Override
    public void join() {
        if (state.equals(Constants.CONNECTED_STATUS) ||
                state.equals(Constants.NOT_LOGGED_IN_STATUS)) {
            if (channel == null) {
                System.out.println(Constants.CHANNEL_NOT_SPECIFIED);
                setStateToNotJoined();
            } else {
                setStateToJoining();
                String line = null;
                irc.setChannel(channel);
                send(irc.join());
                while ((line = receive()) != null) {
                    irc.listenForJoinConfirmation(line);
                    if (irc.isJoinedInChannel()) {
                        print(processLine(line));
                        setStateToJoined();
                        break;
                    } else if (line.contains(irc.NICKNAME_ALREADY_REGISTERED_CODE)) {
                        print(irc.NICKNAME_ALREADY_REGISTERED);
                        setStateToNotJoined();
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void idle() {
        String line = null;
        if (state.equals(Constants.JOINED_CHANNEL_STATUS)) {
            while ((line = receive()) != null) {
                line = processLine(line);
                if (line != null) {
                    if (irc.isWritable()) {
                        send(line);
                        if (irc.isPrintable()) {
                            print(line);
                        }
                    } else if (!irc.isConnected() &&
                            !irc.isLoggedIn()) {
                        setStateToNotConnected();
                        break;
                    } else {
                        print(line);
                    }
                }
                maintainUserListChanges();
            }
            setStateToNotConnected();
        }
    }

    @Override
    public String processLine(String line) {
        String lineType = irc.checkLineType(line);
        if (lineType != null) {
            if (lineType.equals(irc.PING)) {
                silentPong(line);
                return null;
            }
        }
        return irc.reformatLine(line);
    }

    @Override
    public void send(String line) {
        super.send(line);
    }


    @Override
    public void post(String line) {
        super.post(irc.postMessage(line));
        ircService.addPostToHistory(line);
        print(irc.readMessage(irc.getNick(), line));
    }

    @Override
    public void leave() {
        irc.getUsers().clear();
        irc.setUserListSignal(true);
        maintainUserListChanges();
        send(irc.part());
    }

    @Override
    public void logout() {
        if (irc.isLoggedIn()) {
            send(irc.logout());
        } else {
            print(getTimeStamp() + "Not logged in.");
            System.out.println("Is not logged in");
        }
    }

    @Override
    public void disconnect() {
        send(irc.quit());
    }

    @Override
    public void ping() {
        print(irc.readMessage(irc.getNick(), Constants.PING));
        send(irc.postMessage(Constants.PING));
    }

    @Override
    public void pong() {
        print(irc.readMessage(irc.getNick(), Constants.PONG));
        send(irc.postMessage(Constants.PONG));
    }

    public void silentPong(String line) {
        send(irc.pong(line));
    }

    @Override
    public void updateUserList(ArrayList<String> userList) {

    }

    @Override
    public void setCurrentSessionInfo(String provider, String channel,
                                      String nick, String connectionStatus,
                                      String loginStatus, String joinStatus) {

    }

    public void maintainUserListChanges() {
        if (irc.userListHasChanged()) {
            updateUserList(irc.getUsers());
            irc.setUserListSignal(false);
        }
    }

    public void keepUpWithConnectionStatus() {
        if (!irc.isConnected() && !irc.isLoggedIn()) {
            setStateToNotConnected();
            updateUserList(irc.getUsers());
        } else if (irc.isConnected() && !irc.isLoggedIn()) {
            setStateToNotLoggedIn();
        } else if (irc.isLoggedIn() && !irc.isJoinedInChannel()) {
            setStateToNotJoined();
        } else if (irc.isLoggedIn() && irc.isJoinedInChannel()) {
            setStateToJoined();
        }
    }

    @Override
    public IrcConsumer getIrcClient() {
        return this;
    }

    @Override
    public void print(String line) {
        super.print(ircService.getTimeStamp() + line);
    }

    public String getTimeStamp() {
        return ircService.getTimeStamp();
    }

    @Override
    public void whois(String user) {
        System.out.println(user);
        send(irc.whois(user));
    }

    @Override
    public void ignore(String user) {
        send(irc.ignore(user));
    }

    @Override
    public void unignore(String user) {
        send(irc.unignore(user));
    }

    @Override
    public void saveUser(String user) {

    }

    @Override
    public void queryUser(String user) {

    }

}
