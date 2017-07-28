package comets.irc.object;

import java.util.ArrayList;
import java.util.Collections;

public class Irc {

    // constants
    public static final String CONNECTION_CONFIRMATION = "Found your hostname";
    public static final String LOGIN_CONFIRMATION_CODE = "004";
    public static final String NICKNAME_IN_USE_CODE = "433";
    public static final String NICKNAME_ALREADY_IN_USE = "Nickname is " +
            "already in use.";
    public static final String NICKNAME_ALREADY_IN_USE_PROMPT = "Disconnected" +
            " from server. \n" +
            "Please try connecting again with a different nick.\n";
    public static final String RPL_TOPIC_CODE = "332";
    public static final String JOIN_CONFIRMATION_CODE = RPL_TOPIC_CODE;
    public static final String NICKNAME_ALREADY_REGISTERED_CODE = "462";
    public static final String NICKNAME_ALREADY_REGISTERED = "Nickname is " +
            "already registered.";
    public static final String DISCONNECTION_CODE = "451 * ";
    public static final String PONG_MSG = "Responded to ping.";
    public static final String NICKSERV = "NICKSERV ";
    public static final String NICK = "NICK ";
    public static final String PASS = "PASS ";
    public static final String USER = "USER ";
    public static final String LOGIN = "LOGIN ";
    public static final String JOIN = "JOIN ";
    public static final String PING = "PING ";
    public static final String PONG = "PONG ";
    public static final String PRIVMSG = "PRIVMSG ";
    public static final String PART = "PART ";
    public static final String LOGOUT = "LOGOUT ";
    public static final String QUIT = "QUIT";
    public static final String WHOIS = "WHOIS ";
    public static final String IGNORE = "IGNORE ";
    public static final String UNIGNORE = "UNIGNORE ";
    public static final String QUERY = "QUERY ";
    public static final String COLON = " :";
    public static final String SERVER_TALK = "== ";
    public static final String NEWLINE = "\r\n";
    public static final String[] TYPE = {COLON, PRIVMSG, JOIN, PART,
            "QUIT ", "MODE ", "= "};
    public static final String[] BRIEFING = {"IRC Operators online",
            "unknown connection(s)", "channels formed", "ERROR :Closing Link: "};
    public static final String[] REJECT = {"are supported by this server",
            "elvum", "/NAMES"};

    // variables
    private ArrayList<String> users;
    private String nick;
    private String channel;
    private String lineType;
    boolean connectionStatus;
    boolean loginStatus;
    boolean joinStatus;
    boolean userListStatus;
    boolean readable;
    boolean writable;
    boolean printable;
    boolean parse;
    boolean currentState;

    public Irc(String nick, String channel) {
        this.nick = nick;
        this.channel = channel;
        this.connectionStatus = true;
        this.loginStatus = false;
        this.joinStatus = false;
        this.readable = false;
        this.writable = false;
        this.printable = false;
    }

    public String listenForConnectionConfirmation(String line) {
        setReadable(false);
        if (line.contains(CONNECTION_CONFIRMATION)) {
            return CONNECTION_CONFIRMATION;
        }
        return null;
    }

    public String listenForLoginConfirmation(String line) {
        setReadable(false);
        if (line.contains(LOGIN_CONFIRMATION_CODE)) {
            return LOGIN_CONFIRMATION_CODE;
        }
        else if (line.contains(NICKNAME_IN_USE_CODE)) {
            setReadable(true);
            return NICKNAME_ALREADY_IN_USE;
        } else {
            setReadable(true);
            return generalLineFormat(line);
        }
    }

    public String listenForJoinConfirmation(String line) {
        setReadable(false);
        if (line.contains(JOIN_CONFIRMATION_CODE) || line.contains(" " + JOIN)) {
            setJoinStatus(true);
            return JOIN_CONFIRMATION_CODE;
        }
        else if (line.contains(NICKNAME_ALREADY_REGISTERED_CODE)) {
            setJoinStatus(true);
            setReadable(true);
            return NICKNAME_ALREADY_REGISTERED;
        } else {
            setJoinStatus(false);
            setReadable(true);
            return generalLineFormat(line);
        }
    }

    public String checkLineType(String str) {
        if (str.startsWith(PING)) {
            return PING;
        }
        // PRIVMSG
        // Original     :[user]![account]@[domain] PRIVMSG #irchacks :this is a message
        // Reshaped     <[user]> this is a message
        else if (str.contains(TYPE[1] + channel)) {
            return TYPE[1];
        }
        // JOIN
        // Original     :[user]![account]@[domain] JOIN #irchacks
        // Reshaped     [user] has joined #irchacks
        else if (str.contains(TYPE[2] + channel)) {
            return TYPE[2];
        }
        // PART
        // Original     :[nick]![account]@[domain] PART #irchacks
        // Reshaped     [user] has left #irchacks
        else if (str.contains(TYPE[3] + channel)) {
            return TYPE[3];
        }
        // QUIT
        // Original     :[user]![account]@[domain] QUIT :Remote host closed the connection
        // Reshaped     [user] has quit [Remote host closed the connection]
        else if (str.contains(TYPE[4] + ":")) {
            return TYPE[4];
        }
        // MODE is reshaped for printout
        // Original     :[nick] MODE [nick] :+i
        // Reshaped     Usermode changed: +i
        else if (str.contains(TYPE[5])) {
            return TYPE[5];
        }
        // 353 is extracted for user list, but not printed out to main TextArea
        // :weber.freenode.net 353 [nick] = #irchacks :[nick] [user_1] [user_2] ... [user_n]
        else if (str.contains("353") || str.contains(TYPE[6] + channel + TYPE[0])) {
            users = new ArrayList<>();
            for (String user : str.substring(
                    str.indexOf(channel) + channel.length() + 2,
                    str.length()).split(" ")) {
                addUser(user);
            }
            return TYPE[6];
        }
        /* 005, 333, 366 are rejected for print out
         * REJECT[0] :weber.freenode.net 005 [nick] CPRIVMSG CNOTICE WHOX ETRACE :are supported by this server
         * REJECT[1] :weber.freenode.net 333 [nick] #irchacks elvum 1245694020
         * REJECT[2] :weber.freenode.net 366 [nick] #irchacks :End of /NAMES list.
        */
        else if (str.contains(REJECT[0]) ||
                str.contains(REJECT[1]) ||
                str.contains(REJECT[2])){
            return null;
        }
        /* 252, 253, 254 share the same reshaping characteristics
         *
         * Original     :[host] [code] [nick] [number] :[message]
         * Reshaped     [number] [message]
         *
         * BRIEFING[0] :weber.freenode.net 252 [nick] 22 :IRC Operators online
         * BRIEFING[1] :weber.freenode.net 253 [nick] 17 :unknown connection(s)
         * BRIEFING[2] :weber.freenode.net 254 [nick] 58081 :channels formed
        */
        else if (str.contains(BRIEFING[0]) ||
                str.contains(BRIEFING[1]) ||
                str.contains(BRIEFING[2])) {
            return BRIEFING[0];
        }
        // BRIEFING[3] ERROR :Closing Link: [domain] (Irc Quit)
        else if (str.contains(BRIEFING[3])) {
            return BRIEFING[3];
        }
        // All other messages
        else if (str.contains(TYPE[0])) {
            return TYPE[0];
        }
        return null;
    }

    public String reformatLine(String str) {
        String line = str;
        lineType = checkLineType(str);
        setWritable(false);
        // null
        if (lineType == null) {
            return null;
        }
        // PING
        else if (lineType.equals(PING)) {
            setWritable(true);
            return pong(str);
        }
        // briefing type
        else if (lineType.contains(BRIEFING[0])) {
            return SERVER_TALK + briefingFormat(line);
        }
        // general type
        else if (lineType.contains(TYPE[0])) {
            if (line.contains(DISCONNECTION_CODE)) {
                setConnectionStatus(true);
            }
            return SERVER_TALK + generalLineFormat(str);
        }
        // PRIVMSG
        else if (lineType.contains(TYPE[1])) {
            setParse(true);
            return readMessage(extractUserName(str), extractPrivMsg(str));
        }
        // JOIN
        else if (lineType.contains(TYPE[2])) {
            line = extractUserName(str);
            if (line.equals(nick)) {
                setConnectionStatus(false);
            } else {
                addUser(line);
            }
            return SERVER_TALK + joinMessage(line, false);
        }
        // PART
        else if (lineType.contains(TYPE[3])) {
            line = extractUserName(str);
            removeUser(line);
            return SERVER_TALK + partMessage(line, false);
        }
        // QUIT
        else if (lineType.contains(TYPE[4])) {
            line = extractUserName(str);
            removeUser(line);
            return SERVER_TALK + quitMessage(line, str);
        }
        // MODE
        else if (lineType.contains(TYPE[5])) {
            setLoginStatus(true);
            return SERVER_TALK + userModeChangeMessage(line);
        }
        // =
        else if (lineType.contains(TYPE[6])) {
            return null;
        }
        // ERROR :Closing Link:
        else if (lineType.contains(BRIEFING[3])) {
            setLoginStatus(false);
            setConnectionStatus(false);
            return null;
        }
        return null;
    }

    // strings generated for the IRC

    public String login(String nick, String pass, String realName,
                        String login, int userMode) {
        String line = "";
        line += NICK + nick + NEWLINE;
        if (pass != null && !pass.equals("")) {
            line += PASS + pass + NEWLINE;
        }
        line += USER + nick + " " + userMode + " * : ";
        if (realName != null)
            line += realName;
        line += NEWLINE;
        return line;
    }

    public String postMessage(String message) {
        return PRIVMSG + channel + COLON + message + NEWLINE;
    }

    public String join() {
        return JOIN + channel + NEWLINE;
    }

    public String join(String channel) {
        return JOIN + channel + NEWLINE;
    }

    public String pong(String line) {
        return PONG + line.substring(5) + NEWLINE;
    }

    public String part() {
        users.clear();
        return PART + channel + NEWLINE;
    }

    public String part(String reason) {
        users.clear();
        return PART + channel + " " + reason + NEWLINE;
    }

    public String logout() {
        return NICKSERV + LOGOUT + NEWLINE;
    }

    public String quit() {
        return QUIT + NEWLINE;
    }

    public String quit(String reason) {
        return QUIT + reason + NEWLINE;
    }

    public String whois(String userNick) {
        return WHOIS + userNick + NEWLINE;
    }

    public String ignore(String userNick) {
        return IGNORE + userNick + NEWLINE;
    }

    public String unignore(String userNick) {
        return UNIGNORE + userNick + NEWLINE;
    }

    // strings generated for the user

    public String extractUserName(String str) {
        int index = str.indexOf("@");
        str = str.substring(1, index);
        index = str.indexOf("!");
        str = str.substring(0, index);
        return str;
    }

    public String extractPrivMsg(String str) {
        int index = str.indexOf(TYPE[1] + channel + TYPE[0]);
        int length = TYPE[1].length() + channel.length() + TYPE[0].length();
        return str.substring(index + length, str.length());
    }

    public String extractQuitMessage(String str) {
        int index = str.indexOf(TYPE[4] + ":");
        int length = TYPE[4].length() + 1;
        return str.substring(index + length, str.length());
    }

    public String briefingFormat(String line) {
        return line.substring(line.indexOf(nick) + nick.length() + 1)
                .replace(":", "");
    }

    // :
    public String generalLineFormat(String line) {
        return line.substring(line.indexOf(TYPE[0]) + TYPE[0].length());
    }

    // PRIVMSG
    public String readMessage(String user, String message) {
        return "<" + user + "> " + message;
    }

    // JOIN
    public String joinMessage(String line) {
        return extractUserName(line) + " has joined " + channel;
    }

    // for the reformatting process, since user is already extracted
    public String joinMessage(String user, boolean throwAway) {
        return user + " has joined " + channel;
    }

    // PART
    public String partMessage(String line) {
        return extractUserName(line) + " has left " + channel;
    }

    // for the reformatting process, since user is already extracted
    public String partMessage(String user, boolean throwAway) {
        return user + " has left " + channel;
    }

    // QUIT
    public String quitMessage(String line) {
        return extractUserName(line) + " has quit " + "[" +
                extractQuitMessage(line) + "]";
    }

    public String quitMessage(String user, String line) {
        return user + " has quit " + "[" + extractQuitMessage(line) + "]";
    }

    // MODE
    public String userModeChangeMessage(String line) {
        line = line.substring(line.indexOf(TYPE[5]) +
                TYPE[5].length() + nick.length() + 2);
        return "Usermode change: " + line;
    }

    // add user to the user list
    public void addUser(String user) {
        if (users == null) {
            users = new ArrayList<>();
        }
        users.add(user);
        setUserListSignal(true);
        Collections.sort(users);
    }

    public void removeUser(String user) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).equals(user)) {
                users.remove(i);
                setUserListSignal(true);
                break;
            }
        }
    }

    public void setConnectionStatus(boolean status) {
        connectionStatus = status;
        if (!status) {
            if (users == null) {
                users = new ArrayList<>();
            } else {
                users.clear();
            }
            userListHasChanged();
        }
    }

    public boolean isConnected() {
        return connectionStatus;
    }

    public boolean isLoggedIn() {
        return loginStatus;
    }

    public void setLoginStatus(boolean loginStatus) {
        this.loginStatus = loginStatus;
    }

    public void setUserListSignal(boolean signal) {
        userListStatus = signal;
    }

    public boolean userListHasChanged() {
        return userListStatus;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public boolean isReadable() {
        currentState = readable;
        setReadable(false);
        return currentState;
    }

    public boolean isPrintable() {
        currentState = printable;
        setPrintable(false);
        return currentState;
    }

    public void setReadable(boolean readable) {
        this.readable = readable;
    }

    public boolean isWritable() {
        currentState = writable;
        setWritable(false);
        return currentState;
    }

    public boolean isParsable() {
        currentState = parse;
        setParse(false);
        return currentState;
    }

    public void setWritable(boolean write) {
        this.writable = write;
    }

    public void setPrintable(boolean print) {
        this.printable = print;
    }

    public void setParse(boolean b) {
        parse = b;
    }

    public ArrayList<String> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<String> users) {
        this.users = users;
    }

    public boolean isJoinedInChannel() {
        return joinStatus;
    }

    public void setJoinStatus(boolean joinStatus) {
        this.joinStatus = joinStatus;
    }

}
