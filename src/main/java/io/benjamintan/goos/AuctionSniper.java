package io.benjamintan.goos;


public class AuctionSniper implements AuctionEventListener {
    private final Auction auction;
    private Item item;
    private SniperSnapshot snapshot;
    private final Announcer<SniperListener> announcer = Announcer.to(SniperListener.class);

    public AuctionSniper(Auction auction, Item item) {
        this.auction = auction;
        this.item = item;
        this.snapshot = SniperSnapshot.joining(item.identifier);
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
                if (item.allowsBid(bid)) {
                    auction.bid(bid);
                    snapshot = snapshot.bidding(price, bid);
                } else {
                    snapshot = snapshot.losing(price);
                }
                break;
        }

        notifyChange();
    }

    @Override
    public void auctionFailed() {
        snapshot = snapshot.failed();
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
