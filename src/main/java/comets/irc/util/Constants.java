package comets.irc.util;

public class Constants {

    // App

    public static final String DEFAULT_NICK = "app_tester";
    public static final String DEFAULT_SERVER = "irc.freenode.net";
    public static final String DEFAULT_CHANNEL = "#irchacks";
    public static final String DEFAULT_PORT = "6667";
    public static final int DEFAULT_PORT_INT = 6667;

    public static final String CONNECTING_STATUS = "connecting...";
    public static final String CONNECTED_STATUS = "connected";
    public static final String NOT_CONNECTED_STATUS = "not connected";
    public static final String DISCONNECTED_STATUS = "disconnected";
    public static final String LOGGING_IN_STATUS = "logging in";
    public static final String LOGGED_IN_STATUS = "logged in";
    public static final String LOGGED_OUT_STATUS = "logged out";
    public static final String JOINING_CHANNEL_STATUS = "joining channel";
    public static final String JOINED_CHANNEL_STATUS = "joined channel";
    public static final String NOT_LOGGED_IN_STATUS = "not logged in";
    public static final String NOT_JOINED_CHANNEL_STATUS = "not joined channel";
    public static final String LEFT_CHANNEL_STATUS = "left channel";
    public static final String CONNECTION_ERROR_STATUS = "connection error";
    public static final String LOGIN_ERROR_STATUS = "login error";

    // Logging

    public static final String CONNECTED_TO = "Connected to ";
    public static final String CONNECTION_REFUSED_BY = "Error: Connection refused by ";
    public static final String UNABLE_TO_CONNECT_TO = "Error: Unable to connect to ";
    public static final String STREAM_INIT_ISSUES = "Error: Stream initialization issue";
    public static final String ERROR_NOT_CONNECTED = "Error: Not connected.";
    public static final String CHANNEL_NOT_SPECIFIED = "Unable to join: A channel has not been specified.";
    public static final String IO_ERROR = "Error: unable to read line from ";
    public static final String SEND_LINE_ISSUE = "Error: sendLine() issue in ircServiceImpl...";
    public static final String QUICK_TEST_FAILED = "Error: Test failed due to client not having been initialized.";

}
