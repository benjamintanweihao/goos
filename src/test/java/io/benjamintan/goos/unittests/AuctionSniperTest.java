package io.benjamintan.goos.unittests;

import io.benjamintan.goos.Auction;
import io.benjamintan.goos.AuctionSniper;
import io.benjamintan.goos.SniperListener;
import io.benjamintan.goos.SniperSnapshot;
import org.junit.Test;

import static io.benjamintan.goos.AuctionEventListener.PriceSource.FromOtherBidder;
import static io.benjamintan.goos.AuctionEventListener.PriceSource.FromSniper;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class AuctionSniperTest {
    private static final String ITEM_ID = "item-54321";
    private final Auction auction = mock(Auction.class);
    private final SniperListener sniperListenerSpy = spy(new SniperListenerStub());
    private final AuctionSniper sniper = new AuctionSniper(auction, ITEM_ID, sniperListenerSpy);


    private enum SniperStateForTests {
        idle, winning, bidding
    }

    private SniperStateForTests sniperStateForTests = SniperStateForTests.idle;

    @Test
    public void reportsLostIfAuctionClosesImmediately() {
        sniper.auctionClosed();

        verify(sniperListenerSpy, times(1)).sniperLost();
    }

    @Test
    public void reportsLostWhenAuctionClosesWhenBidding() {
        sniper.currentPrice(123, 45, FromOtherBidder);
        sniper.auctionClosed();

        verify(sniperListenerSpy, times(1)).sniperLost();
        assertEquals(SniperStateForTests.bidding, sniperStateForTests);
    }

    @Test
    public void bidsHigherAndReportsBiddingWhenNewPriceArrives() {
        final int price = 1001;
        final int increment = 25;
        final int bid = price + increment;

        sniper.currentPrice(price, increment, FromOtherBidder);

        verify(auction, times(1)).bid(bid);
        verify(sniperListenerSpy, atLeastOnce()).sniperBidding(
               new SniperSnapshot(ITEM_ID, price, bid)
        );
    }

    @Test
    public void reportsIsWinningWhenCurrentPriceComesFromSniper() {
        sniper.currentPrice(123, 45, FromSniper);

        verify(sniperListenerSpy, atLeastOnce()).sniperWinning(new SniperSnapshot(ITEM_ID, 123, 123));
        assertEquals(SniperStateForTests.winning, sniperStateForTests);
    }

    @Test
    public void reportsWonIfAuctionClosesWhenWinning() {
        sniper.currentPrice(123, 45, FromSniper);
        sniper.auctionClosed();

        verify(sniperListenerSpy, atLeastOnce()).sniperWon();
        assertEquals(SniperStateForTests.winning, sniperStateForTests);
    }

    private class SniperListenerStub implements SniperListener {
        @Override
        public void sniperLost() {
        }

        @Override
        public void sniperBidding(SniperSnapshot sniperSnapshot) {
            sniperStateForTests = SniperStateForTests.bidding;
        }

        @Override
        public void sniperWinning(SniperSnapshot sniperSnapshot) {
            sniperStateForTests = SniperStateForTests.winning;
        }

        @Override
        public void sniperWon() {

        }
    }
}
