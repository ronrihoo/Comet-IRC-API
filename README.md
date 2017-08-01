# Comet IRC API

[![Release](https://img.shields.io/github/release/ronrihoo/Comet-IRC-API/all.svg)](https://github.com/ronrihoo/Comet-IRC-API/releases)

An Internet Relay Chat API for Java.

## Setup

1. Either build the JAR or download it from the latest [release](https://github.com/ronrihoo/Comet-IRC-API/releases)

2. Create a folder, "lib", in the directory of a new/existing project

3. Move or copy the JAR file into the _lib_ folder

4. Add the JAR file to the build path

   IDEA: From the _Project_ window, select and right-click the _lib_ folder, then click **Add as Library...**

   Eclipse: From the _Package Explorer_, select and right-click the JAR file, then select **Build** and click **Add to Build Path**

## Usage

1. In a Java source file, import the IRC client API

```java
import comets.irc.IrcClientApi;
```

2. Instantiate a new client

```java
IrcServiceApi client = new IrcServiceApi();
```

3. Initialize client

```java
client.initialize();
```

4. Set server, port, channel, and nickname

```java
client.setServer("irc.freenode.net");
client.setPort(6667);
client.setChannel("#irchacks");
client.setNick("my_nickname");
```

5. Connect, login/join, and idle

```java
client.connect();
client.login();
client.join();
client.idle(); // just for listening on a channel; see below
```

## Use Your Own Idle Method

Example: 

```java
    public static void idle(IrcClientApi client) {
        String line = null;
        while(client.isConnected()) {
            line = client.read();
            if (line != null) {
                client.print(line);
                if (line.contains("ping")) {
                    client.post("pong");
                }
            }
        }
    }
```

Result:

```
...
[07:59] <remote_user> a
[08:00] <remote_user> ping
[08:00] <comet_irc_api> pong
```

## A Simple Full Example

```java
import comets.irc.IrcClientApi;

public class Main {

    public static void main(String[] args) {
        startIrcClient();
    }

    private static void startIrcClient() {
        IrcClientApi client = new IrcClientApi();
        client.initialize()
                .connect("irc.freenode.net", 6667)
                .login("my_nickname")
                .join("#irchacks");
        idle(client);
    }

    private static void idle(IrcClientApi client) {
        String line = null;
        while(client.isConnected()) {
            line = client.read();
            if(line != null) {
                client.print(line);
                if(line.contains("ping")) {
                    client.post("pong");
                }
            }
            client.updateUserList();
        }
    }

}
```

## API Reference

`connect()`

&nbsp;&nbsp;&nbsp;&nbsp;Connects with server and port information set previously using setServer(String) and setPort(int)

`connect(String server, int port)`

&nbsp;&nbsp;&nbsp;&nbsp;Connects with new server and port information

`disconnect()`

&nbsp;&nbsp;&nbsp;&nbsp;Disconnects from server

`getLine()`

&nbsp;&nbsp;&nbsp;&nbsp;Returns the current line

`getPostHistory()`

&nbsp;&nbsp;&nbsp;&nbsp;Returns an ArrayList<String> object with all recorded outgoing posts

`getState()`

&nbsp;&nbsp;&nbsp;&nbsp;Returns the connection state

&nbsp;&nbsp;&nbsp;&nbsp;States:
 * not connected
 * connecting
 * connected
 * disconnected
 * not logged in
 * logging in
 * logged in
 * logged out
 * not joined channel
 * joining channel
 * joined channel
 * left channel

`getUserList()`

&nbsp;&nbsp;&nbsp;&nbsp;Returns an ArrayList<String> object containing the names of users present on the current channel

&nbsp;&nbsp;&nbsp;&nbsp;In order to receive an updated and accurate userlist, the `updateUserList()` method must be invoked upon reading each incoming line
 
`idle()`

&nbsp;&nbsp;&nbsp;&nbsp;Listens to channel and prints processed lines to standard output

&nbsp;&nbsp;&nbsp;&nbsp;To receive each line elsewhere, build a custom `idle()` method, then use the `read()` and `post(String)` methods

`ignore(String user)`

&nbsp;&nbsp;&nbsp;&nbsp;Sends request to ignore a specified user

`isConnected()`

&nbsp;&nbsp;&nbsp;&nbsp;Returns a boolean value that indicates the connection status

&nbsp;&nbsp;&nbsp;&nbsp;At this time, this method returns true for both _connected_ and _connecting_ states

`join()`

&nbsp;&nbsp;&nbsp;&nbsp;Joins previously specified channel

`join(String channel)`

&nbsp;&nbsp;&nbsp;&nbsp;Joins a new channel

`leave()`

&nbsp;&nbsp;&nbsp;&nbsp;Parts from the current channel

`login()`

&nbsp;&nbsp;&nbsp;&nbsp;Logs in using nick and password information set previously

`login(String nick)`

&nbsp;&nbsp;&nbsp;&nbsp;Logs in using new nick

`login(String channel, String nick, String pass)`

&nbsp;&nbsp;&nbsp;&nbsp;Logs in using new channel, nick, and pass

`login(String nick, String pass, String realName, String login, int mode)`

&nbsp;&nbsp;&nbsp;&nbsp;Logs in using new nick, pass, real name, login, and mode

`logout()`

&nbsp;&nbsp;&nbsp;&nbsp;Logs nickname out

`ping()`

&nbsp;&nbsp;&nbsp;&nbsp;Sends a 'ping' message

&nbsp;&nbsp;&nbsp;&nbsp;This message is not saved in the post history

`pong()`

&nbsp;&nbsp;&nbsp;&nbsp;Sends a 'pong' message

&nbsp;&nbsp;&nbsp;&nbsp;This message is not saved in the post history and it is not the same as sending a pong back to a ping request from the server. Ping responses are handled silently in the background

`post(String message)`

&nbsp;&nbsp;&nbsp;&nbsp;Adds the post to history, then reshapes it into an IRC message line and sends it to the connected channel

`print(String line)`

&nbsp;&nbsp;&nbsp;&nbsp;Takes a string, adds a time-stamp to the beginning of it, using the [HR:MN] format, then prints it to standard output

&nbsp;&nbsp;&nbsp;&nbsp;```[08:00] <remote_user> hi```

`printLine()`

&nbsp;&nbsp;&nbsp;&nbsp;Prints the current line

&nbsp;&nbsp;&nbsp;&nbsp;Same as: `print(line)`

`process(String line)`

&nbsp;&nbsp;&nbsp;&nbsp;Takes a raw line and reshapes it into a more human-readable format

&nbsp;&nbsp;&nbsp;&nbsp;```<remote_user> hi```

`quit()`

&nbsp;&nbsp;&nbsp;&nbsp;Disconnects and drops current connection information (server, port, channel, nick, user list)

`read()`

&nbsp;&nbsp;&nbsp;&nbsp;Returns the read line after processing it

&nbsp;&nbsp;&nbsp;&nbsp;Same as: `processLine(receive())`

&nbsp;&nbsp;&nbsp;&nbsp;```<remote_user> hi```

`receive()`

&nbsp;&nbsp;&nbsp;&nbsp;Returns the raw line (no processing or reshaping)

&nbsp;&nbsp;&nbsp;&nbsp;```:remote_user!4dd17b5c@gateway/web/freenode/ip.##.###.###.### PRIVMSG #irchacks :hi```

`send(String rawLine)`

&nbsp;&nbsp;&nbsp;&nbsp;Takes an IRC-formatted line and sends it to the connected channel

&nbsp;&nbsp;&nbsp;&nbsp;Does not record the line in post history

`setChannel(String channel)`

&nbsp;&nbsp;&nbsp;&nbsp;Sets channel information so that it won't have to be done later

`setNick(String nick)`

&nbsp;&nbsp;&nbsp;&nbsp;Sets nickname with which to login

`setPort(int port)`

&nbsp;&nbsp;&nbsp;&nbsp;Sets port number for future connection

`setServer(String server)`

&nbsp;&nbsp;&nbsp;&nbsp;Sets server address for future connection

`setSession(String server, int port, String channel, String nick)`

&nbsp;&nbsp;&nbsp;&nbsp;Sets server, port, channel, and nick for future connection

`unignore(String user)`

&nbsp;&nbsp;&nbsp;&nbsp;Sends request to unignore a specified user

`updateUserList()`

&nbsp;&nbsp;&nbsp;&nbsp;Keeps track of the users logged into the current channel by reading the lines for user activity

&nbsp;&nbsp;&nbsp;&nbsp;This method is meant to be independent of the `NAMES` command, which requests the usernames from the server

`whois(String user)`

&nbsp;&nbsp;&nbsp;&nbsp;Sends a `whois` request for a specified user

&nbsp;&nbsp;&nbsp;&nbsp;Note: must read lines to receive response

## Contributing

All pull requests are welcome.