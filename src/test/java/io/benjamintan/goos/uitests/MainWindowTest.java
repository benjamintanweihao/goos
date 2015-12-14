package io.benjamintan.goos.uitests;

import com.objogate.wl.swing.probe.ValueMatcherProbe;
import io.benjamintan.goos.Item;
import io.benjamintan.goos.MainWindow;
import io.benjamintan.goos.SniperPortfolio;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class MainWindowTest {
    private final SniperPortfolio portfolio = new SniperPortfolio();
    private final MainWindow mainWindow = new MainWindow(portfolio);
    private final AuctionSniperDriver driver = new AuctionSniperDriver(100);

    @Before
    public void setupKeyboardLayoutForWindowLicker() {
        System.setProperty("com.objogate.wl.keyboard", "Mac-GB");
    }

    @Test
    public void makesUserRequestWhenJoinButtonClicked() {
        final ValueMatcherProbe<Item> itemProbe =
            new ValueMatcherProbe<>(equalTo(new Item("an item id", 789)), "item request");

        mainWindow.addUserRequestListener(itemProbe::setReceivedValue);

        driver.startBiddingFor("an item id", 789);
        driver.check(itemProbe);
    }
}
