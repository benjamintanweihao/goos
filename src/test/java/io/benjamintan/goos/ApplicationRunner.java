package io.benjamintan.goos;

public class ApplicationRunner {
    private String itemId;

    public static final String SNIPER_ID = "sniper";
    public static final String SNIPER_PASSWORD = "sniper";
    public static final String XMPP_HOSTNAME = "localhost";
    public static final String SNIPER_XMPP_ID = "sniper@localhost/Smack";

    private AuctionSniperDriver driver;

    public void startBiddingIn(final FakeAuctionServer auction) {
        itemId = auction.getItemId();

        Thread thread = new Thread("Test Application") {
            @Override
            public void run() {
                try {
                    Main.main(XMPP_HOSTNAME, SNIPER_ID, SNIPER_PASSWORD, auction.getItemId());
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
        driver.showSniperStatus(SnipersTableModel.STATUS_JOINING);
    }

    public void showSniperHasLostAuction() {
        driver.showSniperStatus(SnipersTableModel.STATUS_LOST);
    }

    public void showSniperHasWonAuction(int lastPrice) {
        driver.showSniperStatus(itemId, lastPrice, lastPrice, SnipersTableModel.STATUS_WON);
    }

    public void hasShownSniperIsWinning(int winningBid) {
        driver.showSniperStatus(itemId, winningBid, winningBid, SnipersTableModel.STATUS_WINNING);
    }

    public void hasShownSniperIsBidding(int lastPrice, int lastBid)  {
        driver.showSniperStatus(itemId, lastPrice, lastBid, SnipersTableModel.STATUS_BIDDING);
    }

    public void stop() {
        if (driver != null) {
            driver.dispose();
        }
    }

}
