package io.benjamintan.goos;

import java.util.EventListener;

public interface SniperListener extends EventListener {
    void sniperLost();
    void sniperBidding(SniperSnapshot sniperSnapshot);
    void sniperWinning(SniperSnapshot sniperSnapshot);
    void sniperWon();
}
