# Comet IRC API

[![Release](https://img.shields.io/github/release/ronrihoo/Comet-IRC-API/all.svg)](https://github.com/ronrihoo/Comet-IRC-API/releases)

An Internet Relay Chat API for Java.

## Setup

1. Either build the JAR or download it from the latest [release](https://github.com/ronrihoo/Comet-IRC-API/releases)

2. Create a folder, "lib", in the directory of a new/existing project

3. Move or copy the JAR file into the _lib_ folder

4. Add the JAR to build path

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
        while(true) {
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

## API Reference

`connect()`

   Connects with server and port information set previously using setServer(String) and setPort(int)

`connect(String server, int port)`

   Connects with new server and port information

`disconnect()`

   Disconnects from server
   
`getPostHistory()`

   Returns an ArrayList<String> object with all recorded outgoing posts

`getState()`

   Returns the connection state
   
   States:
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

`idle()`

   Listens to channel and prints processed lines to standard output
   
   To receive each line elsewhere, build a custom `idle()` method, then use the `read()` and `post(String)` methods

`ignore(String user)`

   Sends request to ignore a specified user

`join()`

   Joins previously specified channel

`join(String channel)`

   Joins a new channel

`leave()`

   Parts from the current channel

`login()`

   Logs in using nick and pass information set previously

`login(String channel, String nick, String pass)`

   Logs in using new channel, nick, and pass

`login(String nick, String pass, String realName, String login, int mode)`

   Logs in using new nick, pass, real name, login, and mode

`logout()`

   Logs nickname out

`ping()`

   Sends a 'ping' message
   
   Same as: post("ping");

`pong()`

   Sends a 'pong' message
   
   Same as: post("pong");

`post(String message)`

   Adds the post to history, then reshapes it into an IRC message line and sends it to the connected channel

`print(String line)`

   Takes a string, adds a time-stamp to the beginning of it, using the [HR:MN] format, then prints it to standard output

   ```[08:00] <remote_user> hi```
   
`process(String line)`

   Takes a raw line and reshapes it into a more human-readable format
   
   ```<remote_user> hi```

`quit()`

   Disconnects and drops current connection information (server, port, channel, nick, user list)

`read()`

   Returns the read line after processing it
   
   Same as: processLine(receive());
   
   ```<remote_user> hi```

`receive()`

   Returns the raw line (no processing or reshaping)
   
   ```:remote_user!4dd17b5c@gateway/web/freenode/ip.##.###.###.### PRIVMSG #irchacks :hi```

`send(String message)`

   Reshapes the string into an IRC message line and sends it to the connected channel

`setChannel(String channel)`

   Sets channel information so that it won't have to be done later

`setNick(String nick)`

   Sets nickname with which to login

`setPort(int port)`

   Sets port number for future connection
   
`setServer(String server)`

   Sets server address for future connection

`unignore(String user)`

   Sends request to unignore a specified user

`whois(String user)`

   Sends a `whois` request for a specified user

   Note: must read lines to receive response
      
## Contributing

All pull requests are welcome.