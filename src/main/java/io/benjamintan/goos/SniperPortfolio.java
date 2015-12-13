package io.benjamintan.goos;

import java.util.ArrayList;

public class SniperPortfolio implements SniperCollector {

    private final Announcer<PortfolioListener> announcer = Announcer.to(PortfolioListener.class);
    private final ArrayList<AuctionSniper> snipers = new ArrayList<>();


    @Override
    public void addSniper(AuctionSniper sniper) {
        snipers.add(sniper);
        announcer.announce().sniperAdded(sniper);
    }

    public void addPortfolioListener(PortfolioListener listener) {
        announcer.addListener(listener);
    }
}
