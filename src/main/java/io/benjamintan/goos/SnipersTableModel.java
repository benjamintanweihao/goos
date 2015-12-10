package io.benjamintan.goos;

import javax.swing.table.AbstractTableModel;

public class SnipersTableModel extends AbstractTableModel implements SniperListener {
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
    public String getColumnName(int columnIndex) {
        return Column.at(columnIndex).name;
    }

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
        return Column.at(columnIndex).valueIn(snapshot);
    }

    public static String textFor(SniperState state) {
        return STATUS_TEXT[state.ordinal()];
    }

    @Override
    public void sniperStateChanged(SniperSnapshot newSnapshot) {
        this.snapshot = newSnapshot;

        fireTableRowsUpdated(0, 0);
    }
}
