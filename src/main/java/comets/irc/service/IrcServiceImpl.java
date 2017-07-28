package comets.irc.service;

import comets.irc.util.Constants;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class IrcServiceImpl implements IrcService {

    private UserInterfaceService userInterface = null;
    private Socket socket = null;
    private BufferedReader reader = null;
    private BufferedWriter writer = null;

    public IrcServiceImpl (UserInterfaceService userInterface) {
        this.userInterface = userInterface;
    }

    private void initializeStreams() {
        try {
            writer = new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            userInterface.printLine(Constants.STREAM_INIT_ISSUES);
            e.printStackTrace();
        }
        try {
            reader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ConnectionService

    @Override
    public boolean connect(String server, int port) {
        try {
            socket = new Socket(server, port);
            userInterface.setCurrentSessionInfo(server, port);
            userInterface.connectionSuccessful();
            initializeStreams();
            return true;
        } catch (ConnectException e) {
            userInterface.connectionRefused(server, port);
        } catch (IOException e) {
            userInterface.unableToConnect(server, port);
        }
        return false;
    }

    @Override
    public void disconnect() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // MessageService

    @Override
    public String readLine() {
        try {
            if (reader != null) {
                return reader.readLine();
            }
        } catch (IOException e) {
            userInterface.printReadError();
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void sendLine(String line) {
        if (line != null && line.length() > 0) {
            try {
                writer.write(line);
                writer.flush();
            } catch (IOException e1) {
                userInterface.printLine(Constants.SEND_LINE_ISSUE);
                e1.printStackTrace();
            }
        }
    }

    // IRC

    @Override
    public String getTimeStamp() {
        Date date = new Date();
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(date);
        String hour = String.valueOf(cal.get(Calendar.HOUR_OF_DAY));
        String minute = String.valueOf(cal.get(Calendar.MINUTE));
        if (cal.get(Calendar.HOUR_OF_DAY) < 10) {
            hour = "0" + hour;
        }
        if (cal.get(Calendar.MINUTE) < 10) {
            minute = "0" + minute;
        }
        return "[" + hour + ":" + minute + "] ";
    }

    @Override
    public void addPostToHistory(String post) {
        userInterface.addPostToHistory(post);
    }

    @Override
    public ArrayList<String> getPostHistory() {
        return userInterface.getPostHistory();
    }

    @Override
    public void quit() {
        userInterface.dropCurrentSessionInfo();
    }

}
