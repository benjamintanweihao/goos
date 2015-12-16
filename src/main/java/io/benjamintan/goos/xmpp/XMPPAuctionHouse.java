package io.benjamintan.goos.xmpp;

import io.benjamintan.goos.Auction;
import io.benjamintan.goos.AuctionHouse;
import io.benjamintan.goos.Item;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import static java.lang.String.format;
import static org.apache.commons.io.FilenameUtils.getFullPath;

public class XMPPAuctionHouse implements AuctionHouse {

    public static final String AUCTION_RESOURCE = "auction";
    public static final String ITEM_ID_AS_LOGIN = "auction-%s";
    private static final String AUCTION_ID_FORMAT =
            ITEM_ID_AS_LOGIN + "@%s/" + AUCTION_RESOURCE;
    public static final String LOG_FILE_NAME = "auction-sniper.log";
    private static final String LOGGER_NAME = "auction sniper logger";

    private final XMPPConnection connection;
    private final XMPPFailureReporter failureReporter;

    public XMPPAuctionHouse(XMPPConnection connection) throws XMPPAuctionException {
        this.connection = connection;
        this.failureReporter = new LoggingXMPPFailureReporter(makeLogger());
    }

    @Override
    public Auction auctionFor(Item item) {
        return new XMPPAuction(connection, auctionId(item.identifier, connection), failureReporter);
    }

    @Override
    public void disconnect() {
        connection.disconnect();
    }

    public static XMPPAuctionHouse connect(String hostname, String username, String password) throws Exception {
        XMPPConnection connection = new XMPPConnection(hostname);
        try {
            connection.connect();
            connection.login(username, password, AUCTION_RESOURCE);
            return new XMPPAuctionHouse(connection);

        } catch (XMPPException xmppe) {
            throw new Exception("Could not connect to auction: " + connection, xmppe);
        }
    }

    private static String auctionId(String itemId, XMPPConnection connection) {
        return format(AUCTION_ID_FORMAT, itemId, connection.getServiceName());
    }

    private Logger makeLogger() throws XMPPAuctionException {
        Logger logger = Logger.getLogger(LOGGER_NAME);
        logger.setUseParentHandlers(false);
        logger.addHandler(simppleFileHandler());
        return logger;
    }

    private FileHandler simppleFileHandler() throws XMPPAuctionException {
        try {
            FileHandler handler = new FileHandler(LOG_FILE_NAME);
            handler.setFormatter(new SimpleFormatter());
            return handler;
        } catch (Exception e) {
            throw new XMPPAuctionException("Could not create logger FileHandler " + getFullPath(LOG_FILE_NAME), e);
        }
    }

}
