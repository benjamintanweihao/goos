package io.benjamintan.goos.xmpp;

import io.benjamintan.goos.Auction;
import io.benjamintan.goos.AuctionHouse;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import static java.lang.String.format;

public class XMPPAuctionHouse implements AuctionHouse {

    public static final String AUCTION_RESOURCE = "auction";
    public static final String ITEM_ID_AS_LOGIN = "auction-%s";
    private static final String AUCTION_ID_FORMAT =
            ITEM_ID_AS_LOGIN + "@%s/" + AUCTION_RESOURCE;

    private final XMPPConnection connection;

    public XMPPAuctionHouse(XMPPConnection connection) {
        this.connection = connection;
    }

    @Override
    public Auction auctionFor(String itemId) {
        return new XMPPAuction(connection, auctionId(itemId, connection));
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
}
