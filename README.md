# Comet IRC API

[![Release](https://img.shields.io/github/release/ronrihoo/Comet-IRC-API/all.svg)](https://github.com/ronrihoo/Comet-IRC-API/releases)

An Internet Relay Chat API for Java.

## Setup

1. Either build the JAR or download it from the latest [release](https://github.com/ronrihoo/Comet-IRC-API/releases)

2. Create a folder, "lib", in the directory of a new/existing project

3. Move or copy the JAR file into the "lib" folder

4. Add JAR to build path

   IDEA: From the _Project_ window (ALT+1), select and right-click the "lib" folder, then click `Add as Library...`

   Eclipse: From the _Package Explorer_ window, select and right-click the JAR file, then select `Build' and click `Add to Build Path`

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
client.idle();
```

## Contributing

All pull requests are welcome.