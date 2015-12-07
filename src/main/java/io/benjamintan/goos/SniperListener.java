package io.benjamintan.goos;

import java.util.EventListener;

public interface SniperListener extends EventListener {
    void sniperLost();
    void sniperBidding(SniperState sniperState);
    void sniperWinning(SniperState sniperState);
    void sniperWon();
}
