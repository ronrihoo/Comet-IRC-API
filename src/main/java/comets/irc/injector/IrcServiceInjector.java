package comets.irc.injector;

import comets.irc.consumer.IrcClient;
import comets.irc.consumer.IrcConsumer;
import comets.irc.object.Irc;
import comets.irc.service.IrcServiceImpl;
import comets.irc.service.UserInterfaceService;

public class IrcServiceInjector implements IrcInjector {

    @Override
    public IrcConsumer getConsumer(UserInterfaceService userInterface) {
        return new IrcClient(
                new IrcServiceImpl(userInterface),
                userInterface,
                new Irc(userInterface.getNick(), userInterface.getChannel()));
    }

}