package io.benjamintan.goos.unittests;

import io.benjamintan.goos.Column;
import io.benjamintan.goos.SnipersTableModel;
import org.junit.Before;
import org.junit.Test;

import javax.swing.event.TableModelListener;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

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
        // TODO: Stopped here.
    }

}
