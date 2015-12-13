package io.benjamintan.goos;

public interface Auction {
    void bid(int amount);

    void join();

    void addAuctionEventListener(AuctionSniper auctionSniper);
}
