package io.benjamintan.goos;

import javax.swing.table.AbstractTableModel;

public class SnipersTableModel extends AbstractTableModel {
    private final static SniperSnapshot STARTING_UP = new SniperSnapshot("", 0, 0, SniperState.JOINING);

    private SniperSnapshot snapshot = STARTING_UP;

    public static final String STATUS_JOINING = "joining";
    public static final String STATUS_LOST = "lost";
    public static final String STATUS_BIDDING = "bidding";
    public static final String STATUS_WINNING = "winning";
    public static final String STATUS_WON = "won";

    private static String[] STATUS_TEXT = {STATUS_JOINING, STATUS_BIDDING,
            STATUS_WINNING, STATUS_LOST, STATUS_WON};


    @Override
    public int getRowCount() {
        return 1;
    }

    @Override
    public int getColumnCount() {
        return Column.values().length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (Column.at(columnIndex)) {
            case ITEM_IDENTIFIER:
                return snapshot.itemId;
            case LAST_PRICE:
                return snapshot.lastPrice;
            case LAST_BID:
                return snapshot.lastBid;
            case SNIPER_STATE:
                return textFor(snapshot);
            default:
                throw new IllegalArgumentException("No column at " + columnIndex);
        }
    }

    private String textFor(SniperSnapshot snapshot) {
        return STATUS_TEXT[snapshot.state.ordinal()];
    }

    public void sniperStateChanged(SniperSnapshot newSnapshot) {
        this.snapshot = newSnapshot;

        fireTableRowsUpdated(0, 0);
    }
}
