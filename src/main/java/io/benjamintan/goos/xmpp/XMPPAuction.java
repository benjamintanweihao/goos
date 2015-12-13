package io.benjamintan.goos.xmpp;

import io.benjamintan.goos.Announcer;
import io.benjamintan.goos.Auction;
import io.benjamintan.goos.AuctionEventListener;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import static java.lang.String.format;

public class XMPPAuction implements Auction {
    private Chat chat;

    final Announcer<AuctionEventListener> auctionEventListeners =
            Announcer.to(AuctionEventListener.class);

    public static final String JOIN_COMMAND_FORMAT =
            "SOLVersion 1.1; Command: JOIN;";
    public static final String BID_COMMAND_FORMAT =
            "SOLVersion 1.1; Command: BID; Price: %d;";

    public XMPPAuction(XMPPConnection connection, String auctionId) {
        this.chat = connection.getChatManager().createChat(
                auctionId,
                new AuctionMessageTranslator(connection.getUser(),
                        auctionEventListeners.announce()));
    }

    @Override
    public void bid(int amount) {
        sendMessage(format(BID_COMMAND_FORMAT, amount));
    }

    @Override
    public void join() {
        sendMessage(JOIN_COMMAND_FORMAT);
    }

    @Override
    public void addAuctionEventListener(AuctionEventListener auctionEventListener) {
        auctionEventListeners.addListener(auctionEventListener);
    }

    private void sendMessage(final String message) {
        try {
            chat.sendMessage(message);
        } catch (XMPPException e) {
            e.printStackTrace();
        }
    }
}
