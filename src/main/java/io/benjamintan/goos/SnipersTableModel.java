package io.benjamintan.goos;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class SnipersTableModel extends AbstractTableModel
    implements SniperListener, PortfolioListener {

    private ArrayList<SniperSnapshot> snapshots = new ArrayList<>();

    public static final String STATUS_JOINING = "joining";
    public static final String STATUS_LOST = "lost";
    public static final String STATUS_BIDDING = "bidding";
    public static final String STATUS_WINNING = "winning";
    public static final String STATUS_LOSING = "winning";
    public static final String STATUS_WON = "won";
    public static final String STATUS_FAILED = "failed";

    private static String[] STATUS_TEXT = {STATUS_JOINING, STATUS_BIDDING,
            STATUS_WINNING, STATUS_LOSING, STATUS_LOST, STATUS_WON, STATUS_FAILED};

    @Override
    public String getColumnName(int columnIndex) {
        return Column.at(columnIndex).name;
    }

    @Override
    public int getRowCount() {
        return snapshots.size();
    }

    @Override
    public int getColumnCount() {
        return Column.values().length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return Column.at(columnIndex).valueIn(snapshots.get(rowIndex));
    }

    public static String textFor(SniperState state) {
        return STATUS_TEXT[state.ordinal()];
    }

    @Override
    public void sniperStateChanged(SniperSnapshot snapshot) {
        int rowOfTheSniperThatChanged = rowOfTheSniperThatChanged(snapshot);
        snapshots.set(rowOfTheSniperThatChanged, snapshot);
        fireTableRowsUpdated(rowOfTheSniperThatChanged,
                rowOfTheSniperThatChanged);
    }

    private int rowOfTheSniperThatChanged(SniperSnapshot snapshot) {
        for (int rowNumber = 0; rowNumber < snapshots.size(); rowNumber++) {
            if (snapshots.get(rowNumber).isForTheSameItemAs(snapshot)) {
                return rowNumber;
            }
        }
        throw new Defect("Cannot find any previous snapshot for "
                + snapshot.itemId);
    }

    public void addSniper(SniperSnapshot snapshot) {
        snapshots.add(snapshot);
        final int lastInsertedRow = snapshots.size() - 1;
        fireTableRowsInserted(lastInsertedRow, lastInsertedRow);
    }

    private void addSniperSnapshot(SniperSnapshot snapshot) {
       snapshots.add(snapshot);
       int row = snapshots.size() - 1;
       fireTableRowsInserted(row, row);
    }

    @Override
    public void sniperAdded(AuctionSniper sniper) {
        addSniperSnapshot(sniper.getSnapshot());
        sniper.addSniperListener(new SwingThreadSniperListener(this));
    }

    public class SwingThreadSniperListener implements SniperListener {

        private SnipersTableModel snipers;

        public SwingThreadSniperListener(SnipersTableModel snipers) {
            this.snipers = snipers;
        }

        @Override
        public void sniperStateChanged(final SniperSnapshot snapshot) {
            SwingUtilities.invokeLater(() -> snipers.sniperStateChanged(snapshot));
        }
    }
}
