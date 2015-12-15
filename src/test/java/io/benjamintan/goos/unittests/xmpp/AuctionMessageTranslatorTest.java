package io.benjamintan.goos.unittests.xmpp;

import io.benjamintan.goos.ApplicationRunner;
import io.benjamintan.goos.AuctionEventListener;
import io.benjamintan.goos.xmpp.AuctionMessageTranslator;
import io.benjamintan.goos.xmpp.XMPPFailureReporter;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.packet.Message;
import org.junit.Test;

import static io.benjamintan.goos.AuctionEventListener.*;
import static org.mockito.Mockito.*;

public class AuctionMessageTranslatorTest {
    public static final Chat UNUSED_CHAT = null;

    private final AuctionEventListener listener = mock(AuctionEventListener.class);
    private final XMPPFailureReporter failureReporter = mock(XMPPFailureReporter.class);
    private final AuctionMessageTranslator translator = new AuctionMessageTranslator(
            ApplicationRunner.SNIPER_ID, listener, failureReporter);


    @Test
    public void notifiesAuctionClosedWhenClosedMessageReceived() {
        String closedMessage =
                "SOLVersion: 1.1; Event: CLOSE;";

        translator.processMessage(UNUSED_CHAT, message(closedMessage));

        verify(listener, times(1)).auctionClosed();
    }

    @Test
    public void notifiesBidDetailsWhenCurrentPriceMessageReceivedFromOtherBidder() {
        String currentMessage =
                "SOLVersion: 1.1; Event: PRICE; CurrentPrice: 192; Increment: 7; Bidder: Someone else;";

        translator.processMessage(UNUSED_CHAT, message(currentMessage));

        verify(listener, times(1)).currentPrice(192, 7, PriceSource.FromOtherBidder);
    }

    @Test
    public void notifiesBidDetailsWhenCurrentPriceMessageReceivedFromSniper() {
        String currentMessage =
                "SOLVersion: 1.1; Event: PRICE; CurrentPrice: 234; Increment: 5; Bidder: " + ApplicationRunner.SNIPER_ID + ";";

        translator.processMessage(UNUSED_CHAT, message(currentMessage));

        verify(listener, times(1)).currentPrice(234, 5, PriceSource.FromSniper);
    }

    @Test
    public void notifiesAuctionFailedWhenBadMessageReceived() {
        String badMessage = "a bad message";

        translator.processMessage(UNUSED_CHAT, message(badMessage));

        expectFailureWithMessage(badMessage);
    }


    @Test
    public void notifiesAuctionFailedWhenEventTypeMissing() {
        String missingEventTypeMessage =
                "SOLVersion: 1.1; CurrentPrice: 234; Increment: 5; Bidder: " + ApplicationRunner.SNIPER_ID + ";";

        translator.processMessage(UNUSED_CHAT, message(missingEventTypeMessage));

        expectFailureWithMessage(missingEventTypeMessage);
    }

    private Message message(String body) {
        Message message = new Message();
        message.setBody(body);
        return message;
    }

    private void expectFailureWithMessage(String badMessage) {
        verify(listener).auctionFailed();
        verify(failureReporter).cannotTranslateMessage(
                eq(ApplicationRunner.SNIPER_ID),
                eq(badMessage),
                any(Exception.class)
        );
    }
}
