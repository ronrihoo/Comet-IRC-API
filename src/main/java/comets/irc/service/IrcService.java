package comets.irc.service;

public interface IrcService extends ConnectionService, MessageService {

    String getTimeStamp();
    void quit();

}
