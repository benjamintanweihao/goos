package io.benjamintan.goos;


public class AuctionSniper implements AuctionEventListener {
    private final Auction auction;
    private String itemId;
    private SniperSnapshot snapshot;
    private final Announcer<SniperListener> announcer = Announcer.to(SniperListener.class);

    public AuctionSniper(Auction auction, String itemId) {
        this.auction = auction;
        this.itemId = itemId;
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
        announcer.announce().sniperStateChanged(snapshot);
    }

    public SniperSnapshot getSnapshot() {
        return snapshot;
    }

    public void addSniperListener(SniperListener listener) {
        announcer.addListener(listener);
    }
}
