package io.benjamintan.goos;

public interface AuctionHouse {
    Auction auctionFor(String itemId);

    void disconnect();
}
