package io.benjamintan.goos;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {
    private static final String SNIPERS_TABLE_NAME = "Snipers";
    public static final String MAIN_WINDOW_NAME = "Auction Sniper Main";
    public static final String APPLICATION_NAME = "Auction Sniper";

    private SnipersTableModel snipers;


    public MainWindow(SnipersTableModel snipers) {
        super(APPLICATION_NAME);
        this.snipers = snipers;
        setName(MAIN_WINDOW_NAME);
        fillContentPane(makeSnipersTable());
        pack();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void fillContentPane(JTable snipersTable) {
        final Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        contentPane.add(new JScrollPane(snipersTable), BorderLayout.CENTER);
    }

    private JTable makeSnipersTable() {
        final JTable snipersTable = new JTable(snipers);
        snipersTable.setName(SNIPERS_TABLE_NAME);
        return snipersTable;
    }
}
