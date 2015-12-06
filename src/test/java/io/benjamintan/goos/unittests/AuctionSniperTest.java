package io.benjamintan.goos.unittests;

import io.benjamintan.goos.Auction;
import io.benjamintan.goos.AuctionSniper;
import io.benjamintan.goos.Main;
import io.benjamintan.goos.SniperListener;
import org.junit.Test;

import static io.benjamintan.goos.AuctionEventListener.PriceSource.FromOtherBidder;
import static io.benjamintan.goos.AuctionEventListener.PriceSource.FromSniper;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class AuctionSniperTest {
    private final Auction auction = mock(Auction.class);
    private final SniperListener sniperListenerSpy = spy(new SniperListenerStub());
    private final AuctionSniper sniper = new AuctionSniper(auction, sniperListenerSpy);

    private enum SniperState {
        idle, winning, bidding
    }

    private SniperState sniperState = SniperState.idle;

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
        assertEquals(SniperState.bidding, sniperState);
    }

    @Test
    public void bidsHigherAndReportsBiddingWhenNewPriceArrives() {
        final int price = 1001;
        final int increment = 25;

        sniper.currentPrice(price, increment, FromOtherBidder);

        verify(auction, times(1)).bid(price + increment);
        verify(sniperListenerSpy, atLeastOnce()).sniperBidding();
    }

    @Test
    public void reportsIsWinningWhenCurrentPriceComesFromSniper() {
        sniper.currentPrice(123, 45, FromSniper);

        verify(sniperListenerSpy, atLeastOnce()).sniperWinning();
        assertEquals(SniperState.winning, sniperState);
    }

    @Test
    public void reportsWonIfAuctionClosesWhenWinning() {
        sniper.currentPrice(123, 45, FromSniper);
        sniper.auctionClosed();

        verify(sniperListenerSpy, atLeastOnce()).sniperWon();
        assertEquals(SniperState.winning, sniperState);
    }

    private class SniperListenerStub implements SniperListener {
        @Override
        public void sniperLost() {
        }

        @Override
        public void sniperBidding() {
            sniperState = SniperState.bidding;
        }

        @Override
        public void sniperWinning() {
            sniperState = SniperState.winning;
        }

        @Override
        public void sniperWon() {

        }
    }
}
