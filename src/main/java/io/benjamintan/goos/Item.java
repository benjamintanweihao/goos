package io.benjamintan.goos;

public class Item {
    public final String identifier;
    public final int stopPrice;

    public Item(String identifier, int stopPrice) {
        this.identifier = identifier;
        this.stopPrice = stopPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Item item = (Item) o;

        if (stopPrice != item.stopPrice) return false;
        return identifier.equals(item.identifier);

    }

    @Override
    public int hashCode() {
        int result = identifier.hashCode();
        result = 31 * result + stopPrice;
        return result;
    }

    public boolean allowsBid(int bid) {
        return bid <= stopPrice;
    }
}
