package io.benjamintan.goos;

import javax.swing.*;
import java.util.ArrayList;

public class SniperLauncher implements UserRequestListener {
    private final ArrayList<Auction> notToBeGCd = new ArrayList<>();
    private final AuctionHouse auctionHouse;
    private final SnipersTableModel snipers;

    public SniperLauncher(AuctionHouse auctionHouse, SnipersTableModel snipers) {
        this.auctionHouse = auctionHouse;
        this.snipers = snipers;
    }

    @Override
    public void joinAuction(String itemId) {
        snipers.addSniper(SniperSnapshot.joining(itemId));
        Auction auction = auctionHouse.auctionFor(itemId);
        notToBeGCd.add(auction);

        AuctionSniper sniper = new AuctionSniper(auction, itemId,
                new SwingThreadSniperListener(snipers));

        auction.addAuctionEventListener(sniper);
        auction.join();
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
