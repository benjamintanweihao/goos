package io.benjamintan.goos;


public class AuctionSniper implements AuctionEventListener {
    private final Auction auction;
    private String itemId;
    private final SniperListener sniperListener;
    private SniperSnapshot snapshot;

    public AuctionSniper(Auction auction, String itemId, SniperListener sniperListener) {
        this.auction = auction;
        this.itemId = itemId;
        this.sniperListener = sniperListener;
        this.snapshot = SniperSnapshot.joining(itemId);
    }

    @Override
    public void auctionClosed() {
        snapshot = snapshot.closed();
        notifyChange();
    }

    @Override
    public void currentPrice(int price, int increment, PriceSource priceSource) {
       switch (priceSource) {
            case FromSniper:
                snapshot = snapshot.winning(price);
                break;

            case FromOtherBidder:
                final int bid = price + increment;
                auction.bid(bid);
                snapshot = snapshot.bidding(price, bid);
                break;
        }

        notifyChange();
    }

    private void notifyChange() {
        sniperListener.sniperStateChanged(snapshot);
    }
}
