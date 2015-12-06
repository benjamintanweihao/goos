package io.benjamintan.goos;

public class ApplicationRunner {
    public static final String SNIPER_ID = "sniper";
    public static final String SNIPER_PASSWORD = "sniper";
    public static final String XMPP_HOSTNAME = "localhost";

    public static final String STATUS_JOINING = "joining";
    public static final String STATUS_LOST = "lost";
    public static final String STATUS_BIDDING = "bidding";
    public static final String STATUS_WINNING = "winning";
    public static final String STATUS_WON = "won";
    public static final String SNIPER_XMPP_ID = "sniper@localhost/Smack";

    private AuctionSniperDriver driver;

    public void startBiddingIn(final FakeAuctionServer auction) {

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
        driver.showSniperStatus(STATUS_JOINING);
    }

    public void showSniperHasLostAuction() {
        driver.showSniperStatus(STATUS_LOST);
    }

    public void showSniperHasWonAuction() {
        driver.showSniperStatus(STATUS_WON);
    }

    public void hasShownSniperIsBidding() {
        driver.showSniperStatus(STATUS_BIDDING);
    }

    public void hasShownSniperIsWinning() {
        driver.showSniperStatus(STATUS_WINNING);
    }

    public void stop() {
        if (driver != null) {
            driver.dispose();
        }
    }
}
