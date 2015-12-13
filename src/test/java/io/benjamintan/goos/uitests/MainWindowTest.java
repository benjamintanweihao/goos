package io.benjamintan.goos.uitests;

import com.objogate.wl.swing.probe.ValueMatcherProbe;
import io.benjamintan.goos.AuctionSniperDriver;
import io.benjamintan.goos.MainWindow;
import io.benjamintan.goos.SnipersTableModel;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class MainWindowTest {
    private final SnipersTableModel tableModel = new SnipersTableModel();
    private final MainWindow mainWindow = new MainWindow(tableModel);
    private final AuctionSniperDriver driver = new AuctionSniperDriver(100);

    @Before
    public void setupKeyboardLayoutForWindowLicker() {
        System.setProperty("com.objogate.wl.keyboard", "Mac-GB");
    }

    @Test
    public void makesUserRequestWhenJoinButtonClicked() {
        final ValueMatcherProbe<String> buttonProbe =
            new ValueMatcherProbe<>(equalTo("an item id"), "join request");

        mainWindow.addUserRequestListener(buttonProbe::setReceivedValue);

        driver.startBiddingFor("an item id");
        driver.check(buttonProbe);
    }
}
