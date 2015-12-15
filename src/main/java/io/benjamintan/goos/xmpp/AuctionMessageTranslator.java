package io.benjamintan.goos.xmpp;

import io.benjamintan.goos.AuctionEventListener;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.packet.Message;

import java.util.HashMap;
import java.util.Map;

import static io.benjamintan.goos.AuctionEventListener.*;
import static io.benjamintan.goos.AuctionEventListener.PriceSource.*;

public class AuctionMessageTranslator implements MessageListener {
    private AuctionEventListener listener;
    private XMPPFailureReporter failureReporter;
    private final String sniperId;

    public AuctionMessageTranslator(String sniperId, AuctionEventListener listener, XMPPFailureReporter failureReporter) {
        this.sniperId = sniperId;
        this.listener = listener;
        this.failureReporter = failureReporter;
    }

    @Override
    public void processMessage(Chat unusedChat, Message message) {
        String messageBody = message.getBody();

        try {
            translate(messageBody);
        } catch (Exception parseException) {
        failureReporter.cannotTranslateMessage(sniperId, messageBody, parseException);
            listener.auctionFailed();
        }
    }

    private void translate(String messageBody) throws MissingValueException {
        AuctionEvent event = AuctionEvent.from(messageBody);

        String type = event.type();
        if ("CLOSE".equals(type)) {
            listener.auctionClosed();
        } else if ("PRICE".equals(type)) {
            listener.currentPrice(event.currentPrice(), event.increment(), event.isFrom(sniperId));
        }
    }

    private static class AuctionEvent {
        private final Map<String, String> fields = new HashMap<>();

        public String type() throws MissingValueException {
            return get("Event");
        }

        public int increment() throws MissingValueException {
            return getInt("Increment");
        }

        public int currentPrice() throws MissingValueException {
            return getInt("CurrentPrice");
        }

        public static AuctionEvent from(String messageBody) {
            AuctionEvent event = new AuctionEvent();

            for (String field : fieldsIn(messageBody)) {
                event.addField(field);
            }

            return event;
        }

        private void addField(String field) {
            String[] pair = field.split(":");
            fields.put(pair[0].trim(), pair[1].trim());
        }

        private static String[] fieldsIn(String messageBody) {
            return messageBody.split(";");
        }

        public PriceSource isFrom(String sniperId) throws MissingValueException {
            return sniperId.equals(bidder()) ? FromSniper : FromOtherBidder;
        }

        private String get(String fieldName) throws MissingValueException {
            String value = fields.get(fieldName);
            if (value == null) {
                throw new MissingValueException(fieldName);
            } else {
                return value;
            }
        }

        private int getInt(String fieldName) throws MissingValueException {
            return Integer.parseInt(get(fieldName));
        }

        private String bidder() throws MissingValueException {
            return get("Bidder");
        }
    }
}
