package io.benjamintan.goos;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Main {
    public static final int ARG_HOSTNAME = 0;
    public static final int ARG_USERNAME = 1;
    public static final int ARG_PASSWORD = 2;

    private final SnipersTableModel snipers = new SnipersTableModel();
    private MainWindow ui;

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

    private void addUserRequestListenerFor(AuctionHouse auctionHouse) {
        SniperLauncher sniperLauncher = new SniperLauncher(auctionHouse, snipers);
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
        SwingUtilities.invokeAndWait(() -> ui = new MainWindow(snipers));
    }

}


