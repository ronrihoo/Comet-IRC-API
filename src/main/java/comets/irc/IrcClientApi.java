package comets.irc;

import comets.irc.consumer.IrcConsumer;
import comets.irc.injector.IrcInjector;
import comets.irc.injector.IrcServiceInjector;
import comets.irc.service.UserInterface;
import comets.irc.service.UserInterfaceService;
import comets.irc.util.Constants;

import java.util.ArrayList;

public class IrcClientApi {

    private IrcConsumer client = null;
    private String line = null;

    public IrcClientApi initialize() {
        if (!hasBeenInitialized()) {
            UserInterfaceService userInterface = new UserInterface();
            IrcInjector ircInjector = new IrcServiceInjector();
            setClient(ircInjector.getConsumer(userInterface));
        }
        return this;
    }
    public boolean hasBeenInitialized() {
        return client != null;
    }
    private IrcClientApi setClient(IrcConsumer ircClient) {
        client = ircClient;
        return this;
    }
    public IrcClientApi setNick(String nick) {
        client.setNick(nick);
        return this;
    };
    public IrcClientApi setChannel(String channel) {
        client.setChannel(channel);
        return this;
    };
    public IrcClientApi setServer(String server) {
        client.setServer(server);
        return this;
    };
    public IrcClientApi setPort(int port) {
        client.setPort(port);
        return this;
    };
    public IrcClientApi setSession(String server, int port, String channel, String nick) {
        client.setServer(server);
        client.setPort(port);
        client.setChannel(channel);
        client.setNick(nick);
        return this;
    };
    public String getState() { return client.getState(); }
    public IrcClientApi connect() {
        client.connect();
        return this;
    };
    public boolean isConnected() {
        return client.isConnected();
    }
    public IrcClientApi connect(String server, int port) {
        client.connect(server, port);
        return this;
    };
    public IrcClientApi login() {
        client.login();
        return this;
    };
    public IrcClientApi login(String nick) {
        client.login(nick);
        return this;
    };
    public IrcClientApi login(String channel, String nick, String pass) {
        client.login(channel, nick, pass);
        return this;
    };
    public IrcClientApi login(String nick, String pass, String realName, String login,
               int mode) {
        client.login(nick, pass, realName, login, mode);
        return this;
    };
    public IrcClientApi join() {
        client.join();
        return this;
    };
    public IrcClientApi join(String channel) {
        client.join(channel);
        return this;
    };
    public IrcClientApi idle() {
        client.idle();
        return this;
    };
    public IrcClientApi send(String rawLine) {
        /*
         * Takes raw IRC-formatted lines
         *
         * This method does not store sent line in post history
         */
        client.send(rawLine);
        return this;
    };
    public String receive() {
        return (this.line = client.receive());
    };
    public String processLine(String line) {
        return (this.line = client.processLine(line));
    };
    public String read() {
        /*
         * Reads, reshapes, and returns the line
         */
        return (this.line = processLine(receive()));
    }
    public IrcClientApi post(String line) {
        /*
         * Takes a regular string and sends it through a pipeline to get formatted
         * before going outbound to the IRC server
         *
         * This method stores each sent line in the post history (ArrayList<String>)
         */
        client.post(line);
        return this;
    };
    public IrcClientApi print(String line) {
        client.print(line);
        return this;
    };
    public IrcClientApi printLine() {
        client.print(this.line);
        return this;
    }
    public IrcClientApi ping() {
        client.ping();
        return this;
    };
    public IrcClientApi pong() {
        client.pong();
        return this;
    };
    public IrcClientApi leave() {
        client.leave();
        return this;
    };
    public IrcClientApi logout() {
        client.logout();
        return this;
    };
    public IrcClientApi disconnect() {
        client.disconnect();
        return this;
    };
    public IrcClientApi quit() {
        client.quit();
        return this;
    };
    public IrcClientApi updateUserList(ArrayList<String> userList) {
        client.updateUserList(userList);
        return this;
    };
    public IrcClientApi setCurrentSessionInfo(String provider, String channel, String nick,
                               String connectionStatus, String loginStatus,
                               String joinStatus) {
        client.setCurrentSessionInfo(provider, channel, nick, connectionStatus,
                loginStatus, joinStatus);
        return this;
    };
    public ArrayList<String> getPostHistory() {
        return client.getPostHistory();
    };
    public ArrayList<String> getUserList() {
        return client.getUserList();
    }
    public String getLine() { return this.line; }
    // user list events
    public IrcClientApi whois(String user) {
        client.whois(user);
        return this;
    };
    public IrcClientApi ignore(String user) {
        client.ignore(user);
        return this;
    };
    public IrcClientApi unignore(String user) {
        client.unignore(user);
        return this;
    };
    public IrcClientApi saveUser(String user) {
        client.saveUser(user);
        return this;
    };
    public IrcClientApi queryUser(String user) {
        client.queryUser(user);
        return this;
    };
    public void updateUserList() {
        client.maintainUserListChanges();
    }
    // quick tests
    public void runQuickTest(String nick) {
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