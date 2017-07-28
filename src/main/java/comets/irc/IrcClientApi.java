package comets.irc;

import comets.irc.consumer.IrcConsumer;
import comets.irc.injector.IrcInjector;
import comets.irc.injector.IrcServiceInjector;
import comets.irc.service.UserInterface;
import comets.irc.service.UserInterfaceService;
import comets.irc.util.Constants;

import java.util.ArrayList;

public class IrcClientApi {

    private static IrcConsumer client = null;

    public static void initialize() {
        if (!hasBeenInitialized()) {
            UserInterfaceService userInterface = new UserInterface();
            IrcInjector ircInjector = new IrcServiceInjector();
            setClient(ircInjector.getConsumer(userInterface));
        }
    }
    private static void setClient(IrcConsumer ircClient) { client = ircClient; }
    public static void setNick(String nick) {
        client.setNick(nick);
    };
    public static boolean hasBeenInitialized() {
        return client != null;
    }
    public static void setChannel(String channel) {
        client.setChannel(channel);
    };
    public static void setServer(String server) {
        client.setServer(server);
    };
    public static void setPort(int port) {
        client.setPort(port);
    };
    public static String getState() { return client.getState(); }
    public static void connect() {
        client.connect();
    };
    public static void connect(String server, int port) {
        client.connect(server, port);
    };
    public static void login() {
        client.login();
    };
    public static void login(String channel, String nick, String pass) {
        client.login(channel, nick, pass);
    };
    public static void login(String nick, String pass, String realName, String login,
               int mode) {
        client.login(nick, pass, realName, login, mode);
    };
    public static void join() {
        client.join();
    };
    public static void join(String channel) {
        client.join(channel);
    };
    public static void idle() {
        client.idle();
    };
    public static void send(String line) {
        /*
         * This method does not store sent line in post history
         */
        client.send(line);
    };
    public static String receive() {
        return client.receive();
    };
    public static String processLine(String line) {
        return client.processLine(line);
    };
    public static void post(String line) {
        /*
         * This method is meant for storing each line in post history;
         * however, post history is not currently implemented
         *
         * TODO: create an optional post history implementation
         */
        client.post(line);
    };
    public static void print(String line) {
        client.print(line);
    };
    public static void ping(String target) {
        client.ping(target);
    };
    public static void pong() {
        client.pong();
    };
    public static void leave() {
        client.leave();
    };
    public static void logout() {
        client.logout();
    };
    public static void disconnect() {
        client.disconnect();
    };
    public static void quit() {
        client.quit();
    };
    public static void updateUserList(ArrayList<String> userList) {
        client.updateUserList(userList);
    };
    public static void setCurrentSessionInfo(String provider, String channel, String nick,
                               String connectionStatus, String loginStatus,
                               String joinStatus) {
        client.setCurrentSessionInfo(provider, channel, nick, connectionStatus,
                loginStatus, joinStatus);
    };
    // user list events
    public static void whoisUser(String user) {
        client.whoisUser(user);
    };
    public static void ignoreUser(String user) {
        client.ignoreUser(user);
    };
    public static void unignoreUser(String user) {
        client.unignoreUser(user);
    };
    public static void saveUser(String user) {
        client.saveUser(user);
    };
    public static void queryUser(String user) {
        client.queryUser(user);
    };
    // quick tests
    public static void runQuickTest(String nick) {
        if (hasBeenInitialized()) {
            setServer(Constants.DEFAULT_SERVER);
            setPort(Constants.DEFAULT_PORT_INT);
            setChannel(Constants.DEFAULT_CHANNEL);
            setNick(nick);
            connect();
            login();
            join();
            idle();
        } else {
            System.out.println(Constants.QUICK_TEST_FAILED);
        }
    }

}