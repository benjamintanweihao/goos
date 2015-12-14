package io.benjamintan.goos.unittests;

import io.benjamintan.goos.*;
import org.junit.Before;
import org.junit.Test;

import static io.benjamintan.goos.AuctionEventListener.PriceSource.FromOtherBidder;
import static io.benjamintan.goos.AuctionEventListener.PriceSource.FromSniper;
import static org.mockito.Mockito.*;

public class AuctionSniperTest {
    private static final String ITEM_ID = "item-54321";
    private final Item item = new Item(ITEM_ID, 1234);
    private final Auction auction = mock(Auction.class);
    private final SniperListener sniperListener = mock(SniperListener.class);
    private final AuctionSniper sniper = new AuctionSniper(auction, item);

    @Before
    public void addSniperListener() {
        sniper.addSniperListener(sniperListener);
    }

    @Test
    public void reportsLostIfAuctionClosesImmediately() {
        sniper.auctionClosed();

        verify(sniperListener, times(1)).sniperStateChanged(
                new SniperSnapshot(ITEM_ID, 0, 0, SniperState.LOST));
    }

    @Test
    public void reportsLostWhenAuctionClosesWhenBidding() {
        sniper.currentPrice(123, 45, FromOtherBidder);
        sniper.auctionClosed();

        verify(sniperListener, times(1)).sniperStateChanged(
                new SniperSnapshot(ITEM_ID, 123, 168, SniperState.BIDDING));

        verify(sniperListener, times(1)).sniperStateChanged(
                new SniperSnapshot(ITEM_ID, 123, 123, SniperState.LOST));
    }

    @Test
    public void bidsHigherAndReportsBiddingWhenNewPriceArrives() {
        final int price = 1001;
        final int increment = 25;
        final int bid = price + increment;

        sniper.currentPrice(price, increment, FromOtherBidder);

        verify(auction, times(1)).bid(bid);
        verify(sniperListener, atLeastOnce()).sniperStateChanged(
                new SniperSnapshot(ITEM_ID, price, bid, SniperState.BIDDING)
        );
    }

    @Test
    public void reportsIsWinningWhenCurrentPriceComesFromSniper() {
        sniper.currentPrice(123, 45, FromSniper);

        verify(sniperListener, atLeastOnce()).sniperStateChanged(
                new SniperSnapshot(ITEM_ID, 123, 0, SniperState.WINNING));
    }

    @Test
    public void reportsWonIfAuctionClosesWhenWinning() {
        sniper.currentPrice(123, 45, FromSniper);
        sniper.auctionClosed();

        verify(sniperListener, times(1)).sniperStateChanged(
                new SniperSnapshot(ITEM_ID, 123, 0, SniperState.WINNING));

        verify(sniperListener, times(1)).sniperStateChanged(
                new SniperSnapshot(ITEM_ID, 123, 123, SniperState.WON));
    }

    @Test
    public void reportsFailedIfAuctionFailsWhenBidding() {
        sniper.currentPrice(123, 45, FromOtherBidder);
        sniper.auctionFailed();

        verify(sniperListener).sniperStateChanged(
                new SniperSnapshot(ITEM_ID, 0, 0, SniperState.FAILED));
    }

}
