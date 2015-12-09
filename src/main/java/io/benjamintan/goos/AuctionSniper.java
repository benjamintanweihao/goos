package io.benjamintan.goos;

import static io.benjamintan.goos.AuctionEventListener.PriceSource.*;

public class AuctionSniper implements AuctionEventListener {
    private final Auction auction;
    private String itemId;
    private final SniperListener sniperListener;
    private boolean isWinning = false;
    private SniperSnapshot snapshot;

    public AuctionSniper(Auction auction, String itemId, SniperListener sniperListener) {
        this.auction = auction;
        this.sniperListener = sniperListener;
        this.snapshot = SniperSnapshot.joining(itemId);
    }

    @Override
    public void auctionClosed() {
        if (isWinning) {
            snapshot = snapshot.won();
        } else {
            snapshot = snapshot.lost();
        }

        sniperListener.sniperStateChanged(snapshot);
    }

    @Override
    public void currentPrice(int price, int increment, PriceSource priceSource) {
        isWinning = priceSource == FromSniper;

        if (isWinning) {
            snapshot = snapshot.winning(price);
        } else {
            final int bid = price + increment;
            auction.bid(bid);
            snapshot = snapshot.bidding(price, bid);
        }

        sniperListener.sniperStateChanged(snapshot);
    }
}
