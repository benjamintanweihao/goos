package io.benjamintan.goos;

public class SniperSnapshot {
    public final String itemId;
    public final int lastPrice;
    public final int lastBid;
    public final SniperState state;

    public SniperSnapshot(String itemId, int lastPrice, int lastBid, SniperState state) {
        this.itemId = itemId;
        this.lastPrice = lastPrice;
        this.lastBid = lastBid;
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SniperSnapshot that = (SniperSnapshot) o;

        if (lastPrice != that.lastPrice) return false;
        if (lastBid != that.lastBid) return false;
        if (itemId != null ? !itemId.equals(that.itemId) : that.itemId != null) return false;
        return state == that.state;

    }

    @Override
    public int hashCode() {
        int result = itemId != null ? itemId.hashCode() : 0;
        result = 31 * result + lastPrice;
        result = 31 * result + lastBid;
        result = 31 * result + (state != null ? state.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "SniperSnapshot{" +
                "itemId='" + itemId + '\'' +
                ", lastPrice=" + lastPrice +
                ", lastBid=" + lastBid +
                ", state=" + state +
                '}';
    }

    public SniperSnapshot bidding(int price, int bid) {
        return new SniperSnapshot(itemId, price, bid, SniperState.BIDDING);
    }

    public SniperSnapshot winning(int price) {
        return new SniperSnapshot(itemId, price, lastBid, SniperState.WINNING);
    }

    public static SniperSnapshot joining(String itemId) {
        return new SniperSnapshot(itemId, 0, 0, SniperState.JOINING);
    }

    public SniperSnapshot closed() {
        return new SniperSnapshot(itemId, lastPrice, lastPrice, state.whenAuctionClosed());
    }

    public boolean isForTheSameItemAs(SniperSnapshot snapshot) {
        return itemId.equals(snapshot.itemId);
    }
}
