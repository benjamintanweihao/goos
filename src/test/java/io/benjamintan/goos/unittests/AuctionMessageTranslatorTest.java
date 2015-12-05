package io.benjamintan.goos.unittests;

import io.benjamintan.goos.AuctionEventListener;
import io.benjamintan.goos.AuctionMessageTranslator;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.packet.Message;
import org.junit.Test;

import static org.mockito.Mockito.mock;

public class AuctionMessageTranslatorTest {
    public static final Chat UNUSED_CHAT = null;

    private final AuctionEventListener listener = mock(AuctionEventListener.class);

    private final AuctionMessageTranslator translator = new AuctionMessageTranslator();


    @Test
    public void notifiesAuctionClosedWhenCLosedMessageReceived() {
        Message message = new Message();
        message.setBody("SOLVersion: 1.1; Event: CLOSE;");

        translator.processMessage(UNUSED_CHAT, message);
    }
}
