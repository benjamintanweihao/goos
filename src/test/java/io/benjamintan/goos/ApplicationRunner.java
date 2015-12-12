package io.benjamintan.goos;

public class ApplicationRunner {
    public static final String SNIPER_ID = "sniper";
    public static final String SNIPER_PASSWORD = "sniper";
    public static final String XMPP_HOSTNAME = "localhost";
    public static final String SNIPER_XMPP_ID = "sniper@localhost/Smack";

    private AuctionSniperDriver driver;

    public void startBiddingIn(final FakeAuctionServer... auctions) {
        Thread thread = new Thread("Test Application") {
            @Override
            public void run() {
                try {
                    Main.main(arguments(auctions));
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

    private static String[] arguments(FakeAuctionServer... auctions) {
        String[] arguments = new String[auctions.length + 3];
        arguments[0] = XMPP_HOSTNAME;
        arguments[1] = SNIPER_ID;
        arguments[2] = SNIPER_PASSWORD;
        for (int i = 0; i < auctions.length; i++) {
            arguments[i + 3] = auctions[i].getItemId();
        }

        return arguments;
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
