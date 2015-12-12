package io.benjamintan.goos;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import static java.lang.String.format;

public class Main {
    public static final int ARG_HOSTNAME = 0;
    public static final int ARG_USERNAME = 1;
    public static final int ARG_PASSWORD = 2;
    public static final int ARG_ITEM_ID = 3;

    public static final String AUCTION_RESOURCE = "auction";
    public static final String ITEM_ID_AS_LOGIN = "auction-%s";

    private static final String AUCTION_ID_FORMAT =
            ITEM_ID_AS_LOGIN + "@%s/" + AUCTION_RESOURCE;

    private final SnipersTableModel snipers = new SnipersTableModel();


    private MainWindow ui;
    private ArrayList<Chat> notToBeGCd = new ArrayList<>();

    public Main() throws Exception {
        startUserInterface();
    }

    public static void main(String... args) throws Exception {
        Main main = new Main();
        XMPPConnection connection =
            connection(args[ARG_HOSTNAME], args[ARG_USERNAME], args[ARG_PASSWORD]);
        main.disconnectWhenUICloses(connection);

        for (int i = 3; i < args.length; i++) {
            main.joinAuction(connection, args[i]);
        }
    }

    private static XMPPConnection connection(String hostname, String username, String password) throws XMPPException {
        XMPPConnection connection = new XMPPConnection(hostname);
        connection.connect();
        connection.login(username, password);

        return connection;
    }

    private static String auctionId(String itemId, XMPPConnection connection) {
        return format(AUCTION_ID_FORMAT, itemId, connection.getServiceName());
    }

    private void joinAuction(XMPPConnection connection, String itemId) throws Exception {
        safelyAddItemToModel(itemId);

        final Chat chat = connection.getChatManager().createChat(
                auctionId(itemId, connection), null);

        this.notToBeGCd.add(chat);

        Auction auction = new XMPPAuction(chat);
        chat.addMessageListener(
                new AuctionMessageTranslator(
                connection.getUser(),
                new AuctionSniper(auction, itemId, new SwingThreadSniperListener(snipers))));
        auction.join();
    }

    private void safelyAddItemToModel(String itemId) throws Exception {
        SwingUtilities.invokeAndWait(() -> snipers.addSniper(SniperSnapshot.joining(itemId)));
    }

    private void disconnectWhenUICloses(XMPPConnection connection) {
        ui.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                connection.disconnect();
            }
        });
    }

    private void startUserInterface() throws Exception {
        SwingUtilities.invokeAndWait(() -> ui = new MainWindow(snipers));
    }

    public class SwingThreadSniperListener implements SniperListener {

        private SnipersTableModel snipers;

        public SwingThreadSniperListener(SnipersTableModel snipers) {
            this.snipers = snipers;
        }

        @Override
        public void sniperStateChanged(final SniperSnapshot snapshot) {
            SwingUtilities.invokeLater(() -> snipers.sniperStateChanged(snapshot));
        }
    }
}

