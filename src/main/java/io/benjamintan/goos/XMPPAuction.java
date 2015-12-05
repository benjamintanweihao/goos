package io.benjamintan.goos;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.XMPPException;

import static java.lang.String.format;

public class XMPPAuction implements Auction {
    private Chat chat;

    public static final String JOIN_COMMAND_FORMAT =
            "SOLVersion 1.1; Command: JOIN;";
    public static final String BID_COMMAND_FORMAT =
            "SOLVersion 1.1; Command: BID; Price: %d;";

    public XMPPAuction(Chat chat) {
        this.chat = chat;
    }

    @Override
    public void bid(int amount) {
        sendMessage(format(BID_COMMAND_FORMAT, amount));
    }

    @Override
    public void join() {
        sendMessage(format(JOIN_COMMAND_FORMAT));
    }

    private void sendMessage(final String message) {
        try {
            chat.sendMessage(message);
        } catch (XMPPException e) {
            e.printStackTrace();
        }
    }
}
