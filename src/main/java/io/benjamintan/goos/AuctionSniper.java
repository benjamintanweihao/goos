package io.benjamintan.goos;

import static io.benjamintan.goos.AuctionEventListener.PriceSource.*;

public class AuctionSniper implements AuctionEventListener {
    private final Auction auction;
    private String itemId;
    private final SniperListener sniperListener;
    private boolean isWinning = false;

    public AuctionSniper(Auction auction, String itemId, SniperListener sniperListener) {
        this.auction = auction;
        this.itemId = itemId;
        this.sniperListener = sniperListener;
    }

    @Override
    public void auctionClosed() {
        if (isWinning) {
            sniperListener.sniperWon();
        } else {
            sniperListener.sniperLost();
        }
    }

    @Override
    public void currentPrice(int price, int increment, PriceSource priceSource) {
        isWinning = priceSource == FromSniper;

        if (isWinning) {
            sniperListener.sniperWinning(new SniperState(itemId, price, price));
        } else {
            int bid = price + increment;
            auction.bid(bid);
            sniperListener.sniperBidding(new SniperState(itemId, price, bid));
        }
    }
}
