package io.benjamintan.goos.xmpp;

import java.util.logging.Logger;

public class LoggingXMPPFailureReporter implements XMPPFailureReporter {
    private Logger logger;
    private static final String MESSAGE_FORMAT = "<%s> Could not translate message \"%s\" because \"%s\"";


    public LoggingXMPPFailureReporter(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void cannotTranslateMessage(String auctionId, String failedMessage, Exception exception) {
        logger.severe(String.format(MESSAGE_FORMAT, auctionId, failedMessage, exception.toString()));
    }
}
