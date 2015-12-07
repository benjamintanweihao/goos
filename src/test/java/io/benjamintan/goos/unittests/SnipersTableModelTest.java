package io.benjamintan.goos.unittests;

import io.benjamintan.goos.Column;
import io.benjamintan.goos.MainWindow;
import io.benjamintan.goos.SniperState;
import io.benjamintan.goos.SnipersTableModel;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

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
        model.sniperStatusChanged(new SniperState("item id", 555, 666),
                MainWindow.STATUS_BIDDING);

        verify(listener).tableChanged(argThat(aRowChangedEvent()));

        assertColumnEquals(Column.ITEM_IDENTIFIER, "item id");
        assertColumnEquals(Column.LAST_PRICE, 555);
        assertColumnEquals(Column.LAST_BID, 666);
        assertColumnEquals(Column.SNIPER_STATUS, MainWindow.STATUS_BIDDING);
    }

    private void assertColumnEquals(Column column, Object expected) {
        final int rowIndex = 0;
        final int columnIndex = column.ordinal();
        assertEquals(expected, model.getValueAt(rowIndex, columnIndex));
    }

    private Matcher<TableModelEvent> aRowChangedEvent() {
        return samePropertyValuesAs(new TableModelEvent(model, 0));
    }

}
