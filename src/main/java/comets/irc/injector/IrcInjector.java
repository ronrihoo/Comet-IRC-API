package comets.irc.injector;

import comets.irc.consumer.IrcConsumer;
import comets.irc.service.UserInterfaceService;

public interface IrcInjector {

    IrcConsumer getConsumer(UserInterfaceService userInterface);

}