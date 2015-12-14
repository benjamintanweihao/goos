package io.benjamintan.goos;

public interface AuctionHouse {
    Auction auctionFor(Item item);

    void disconnect();
}
