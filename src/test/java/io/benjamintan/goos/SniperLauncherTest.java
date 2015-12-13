package io.benjamintan.goos;

import org.junit.Test;
import org.mockito.InOrder;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SniperLauncherTest {

    AuctionHouse auctionHouse = mock(AuctionHouse.class);
    Auction auction = mock(Auction.class);
    SniperCollector sniperCollector = mock(SniperCollector.class);

    SniperLauncher sniperLauncher;

    @Test
    public void addsNewSniperToCollectorThenJoinsAuction() {
        String itemId = "item 123";

        sniperLauncher = new SniperLauncher(auctionHouse, sniperCollector);

        when(auctionHouse.auctionFor(itemId)).thenReturn(auction);
        InOrder orderedVerifier = inOrder(auction, sniperCollector, auction);

        sniperLauncher.joinAuction(itemId);

        orderedVerifier.verify(auction).addAuctionEventListener(any(AuctionSniper.class));
        orderedVerifier.verify(sniperCollector).addSniper(any(AuctionSniper.class));
        orderedVerifier.verify(auction).join();
    }
}
