package io.benjamintan.goos;

import io.benjamintan.goos.xmpp.XMPPAuctionHouse;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Main {
    public static final int ARG_HOSTNAME = 0;
    public static final int ARG_USERNAME = 1;
    public static final int ARG_PASSWORD = 2;

    private MainWindow ui;
    private final SniperPortfolio portfolio = new SniperPortfolio();

    public Main() throws Exception {
        startUserInterface();
    }

    public static void main(String... args) throws Exception {
        Main main = new Main();

        XMPPAuctionHouse auctionHouse =
                XMPPAuctionHouse.connect(
                        args[ARG_HOSTNAME], args[ARG_USERNAME], args[ARG_PASSWORD]);

        main.disconnectWhenUICloses(auctionHouse);
        main.addUserRequestListenerFor(auctionHouse);
    }

    private void addUserRequestListenerFor(final AuctionHouse auctionHouse) {
        SniperLauncher sniperLauncher = new SniperLauncher(auctionHouse, portfolio);
        ui.addUserRequestListener(sniperLauncher);
    }

    private void disconnectWhenUICloses(AuctionHouse connection) {
        ui.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                connection.disconnect();
            }
        });
    }

    private void startUserInterface() throws Exception {
        SwingUtilities.invokeAndWait(() -> ui = new MainWindow(portfolio));
    }

}


