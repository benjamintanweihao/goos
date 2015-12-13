package io.benjamintan.goos;

import io.benjamintan.goos.uitests.AuctionSniperDriver;

import static io.benjamintan.goos.SnipersTableModel.textFor;

public class ApplicationRunner {
    public static final String SNIPER_ID = "sniper";
    public static final String SNIPER_PASSWORD = "sniper";
    public static final String XMPP_HOSTNAME = "localhost";
    public static final String SNIPER_XMPP_ID = "sniper@localhost/auction";

    private AuctionSniperDriver driver;

    public void startBiddingIn(final FakeAuctionServer... auctions) {
        startSniper();
        for (FakeAuctionServer auction : auctions) {
            String itemId = auction.getItemId();
            driver.startBiddingFor(itemId);
            driver.showSniperStatus(itemId, 0, 0, textFor(SniperState.JOINING));
        }
    }

    private void startSniper() {
        Thread thread = new Thread("Test Application") {
            @Override
            public void run() {
                try {
                    Main.main(XMPP_HOSTNAME, SNIPER_ID, SNIPER_PASSWORD);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

        };

        thread.setDaemon(true);
        thread.start();

        driver = new AuctionSniperDriver(1000);
        driver.hasTitle(MainWindow.APPLICATION_NAME);
        driver.hasColumnTitles();
    }

    public void showSniperHasLostAuction(FakeAuctionServer auction) {
        driver.showSniperStatus(auction.getItemId(), SnipersTableModel.STATUS_LOST);
    }

    public void showSniperHasWonAuction(FakeAuctionServer auction, int lastPrice) {
        driver.showSniperStatus(auction.getItemId(), lastPrice, lastPrice, SnipersTableModel.STATUS_WON);
    }

    public void hasShownSniperIsWinning(FakeAuctionServer auction, int winningBid) {
        driver.showSniperStatus(auction.getItemId(), winningBid, winningBid, SnipersTableModel.STATUS_WINNING);
    }

    public void hasShownSniperIsBidding(FakeAuctionServer auction, int lastPrice, int lastBid) {
        driver.showSniperStatus(auction.getItemId(), lastPrice, lastBid, SnipersTableModel.STATUS_BIDDING);
    }

    public void stop() {
        if (driver != null) {
            driver.dispose();
        }
    }
}
