package io.benjamintan.goos;

public interface AuctionEventListener {
    void auctionClosed();
    void currentPrice(int price, int increment);
}
