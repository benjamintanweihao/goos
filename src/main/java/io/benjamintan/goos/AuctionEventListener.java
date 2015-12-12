package io.benjamintan.goos;

public interface AuctionEventListener {
    enum PriceSource {
        FromSniper, FromOtherBidder
    }

    void auctionClosed();

    void currentPrice(int price, int increment, PriceSource fromOtherBidder);
}
