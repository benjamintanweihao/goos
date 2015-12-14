package io.benjamintan.goos.integrationtests.xmpp;

import io.benjamintan.goos.*;
import io.benjamintan.goos.xmpp.XMPPAuctionHouse;
import org.jivesoftware.smack.XMPPException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;

public class XMPPAuctionHouseTest {

    private final FakeAuctionServer auctionServer = new FakeAuctionServer("item-54321");
    private XMPPAuctionHouse auctionHouse;

    @Before
    public void openConnection() throws Exception {
        auctionHouse = XMPPAuctionHouse.connect(
                FakeAuctionServer.XMPP_HOSTNAME,
                ApplicationRunner.SNIPER_ID,
                ApplicationRunner.SNIPER_PASSWORD
        );
    }

    @After
    public void closeConnection() {
        if (auctionHouse != null) {
            auctionHouse.disconnect();
        }
    }

    @Before
    public void startAuction() throws XMPPException {
        auctionServer.startSellingItem();
    }


    @After
    public void stopAuction() {
        auctionServer.stop();
    }

    @Test
    public void receivesEventsFromAuctionServerAfterJoining() throws Exception {
        final CountDownLatch auctionWasClosed = new CountDownLatch(1);

        final Auction auction = auctionHouse.auctionFor(new Item(auctionServer.getItemId(), 567));
        auction.addAuctionEventListener(auctionClosedListener(auctionWasClosed));

        auction.join();
        auctionServer.hasReceivedJoinRequestFrom(ApplicationRunner.SNIPER_XMPP_ID);
        auctionServer.announceClosed();

        assertTrue("should have been closed", auctionWasClosed.await(2, TimeUnit.SECONDS));
    }

    private AuctionEventListener auctionClosedListener(final CountDownLatch auctionWasClosed) {
        return new AuctionEventListener() {
            @Override
            public void auctionClosed() {
                auctionWasClosed.countDown();
            }

            @Override
            public void currentPrice(int price, int increment, PriceSource fromOtherBidder) {
                // not implemented
            }
        };
    }
}
