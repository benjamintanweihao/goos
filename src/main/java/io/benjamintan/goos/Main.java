package io.benjamintan.goos;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

import javax.swing.*;

public class Main {
    public static final int ARG_HOSTNAME = 0;
    public static final int ARG_USERNAME = 1;
    public static final int ARG_PASSWORD = 2;
    public static final int ARG_ITEM_ID  = 3;

    public static final String AUCTION_RESOURCE = "auction";
    public static final String ITEM_ID_AS_LOGIN = "auction-%s";
    private static final String AUCTION_ID_FORMAT =
        ITEM_ID_AS_LOGIN + "@%s/" + AUCTION_RESOURCE;

    private MainWindow ui;
    private Chat notToBeGCd;

    public Main() throws Exception {
        startUserInterface();
    }

    private void joinAuction(XMPPConnection connection, String itemId) throws XMPPException {
        final Chat chat = connection.getChatManager().createChat(
            auctionId(itemId, connection),
            (chat1, message) -> SwingUtilities.invokeLater(() -> ui.showStatus(MainWindow.STATUS_LOST))
        );

        this.notToBeGCd = chat;
        chat.sendMessage(new Message());
    }

    public static void main(String... args) throws Exception {
        Main main = new Main();
        main.joinAuction(
            connection(
                args[ARG_HOSTNAME],
                args[ARG_USERNAME],
                args[ARG_PASSWORD])
                ,args[ARG_ITEM_ID]);

    }

    private static XMPPConnection connection(String hostname, String username, String password) throws XMPPException {
        XMPPConnection connection = new XMPPConnection(hostname);
        connection.connect();
        connection.login(username, password);

        return connection;
    }

    private void startUserInterface() throws Exception {
        SwingUtilities.invokeAndWait(() -> ui = new MainWindow());
    }

    private static String auctionId(String itemId, XMPPConnection connection) {
        return String.format(AUCTION_ID_FORMAT, itemId, connection.getServiceName());
    }

}
