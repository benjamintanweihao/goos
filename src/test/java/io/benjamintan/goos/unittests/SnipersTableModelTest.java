package io.benjamintan.goos.unittests;

import io.benjamintan.goos.*;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import static io.benjamintan.goos.SnipersTableModel.textFor;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.*;

public class SnipersTableModelTest {

    private TableModelListener listener = mock(TableModelListener.class);
    private SnipersTableModel model = new SnipersTableModel();

    @Before
    public void attachModelListener() {
        model.addTableModelListener(listener);
    }

    @Test
    public void hasEnoughColumns() {
        assertThat(model.getColumnCount(), equalTo(Column.values().length));
    }

    @Test
    public void setsSniperValuesInColumns() {
        SniperSnapshot joining = SniperSnapshot.joining("item id");
        SniperSnapshot bidding = joining.bidding(555, 666);

        model.addSniper(joining);
        model.sniperStateChanged(bidding);

        verify(listener, times(1)).tableChanged(argThat(anyInsertionEvent()));
        verify(listener, times(1)).tableChanged(argThat(aChangeInRow(0)));

        assertRowMatchesSnapshot(0, bidding);
    }

    @Test
    public void setUpColumnHeadings() {
        for (Column column : Column.values()) {
            assertEquals(column.name, model.getColumnName(column.ordinal()));
        }
    }

    @Test
    public void notifiesListenersWhenAddingASniper() {
        SniperSnapshot joining = SniperSnapshot.joining("item123");
        assertEquals(0, model.getRowCount());

        model.addSniper(joining);
        verify(listener).tableChanged(argThat(anInsertionAtRow(0)));

        assertEquals(1, model.getRowCount());
        assertRowMatchesSnapshot(0, joining);
    }

    private void assertRowMatchesSnapshot(int row, SniperSnapshot snapshot) {
        assertEquals(snapshot.itemId, getValueAt(row, Column.ITEM_IDENTIFIER));
        assertEquals(snapshot.lastBid, getValueAt(row, Column.LAST_BID));
        assertEquals(snapshot.lastPrice, getValueAt(row, Column.LAST_PRICE));
        assertEquals(textFor(snapshot.state), getValueAt(row, Column.SNIPER_STATE));
    }

    private Object getValueAt(final int rowIndex, Column column) {
        return model.getValueAt(rowIndex, column.ordinal());
    }

    private Matcher<TableModelEvent> anInsertionAtRow(final int row) {
        return samePropertyValuesAs(
                new TableModelEvent(model, row, row,
                        TableModelEvent.ALL_COLUMNS,
                        TableModelEvent.INSERT));
    }


    private Matcher<TableModelEvent> aChangeInRow(final int row) {
        return samePropertyValuesAs(new TableModelEvent(model, row, row,
                TableModelEvent.ALL_COLUMNS,
                TableModelEvent.UPDATE));
    }

    private Matcher<TableModelEvent> anyInsertionEvent() {
        return hasProperty("type", equalTo(TableModelEvent.INSERT));
    }
}
