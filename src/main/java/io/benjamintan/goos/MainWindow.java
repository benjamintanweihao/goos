package io.benjamintan.goos;

import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;
import java.util.ArrayList;

public class MainWindow extends JFrame {
    private static final String SNIPERS_TABLE_NAME = "Snipers";
    public static final String MAIN_WINDOW_NAME = "Auction Sniper Main";
    public static final String APPLICATION_NAME = "Auction Sniper";
    public static final String NEW_ITEM_STOP_PRICE_NAME = "stop price";
    public static final String NEW_ITEM_ID_NAME = "item id";
    public static final String JOIN_BUTTON_NAME = "Join Auction";

    JTextField itemIdField;
    JFormattedTextField stopPriceField;

    ArrayList<UserRequestListener> userRequests = new ArrayList<>();

    public MainWindow(SniperPortfolio portfolio) {
        super(APPLICATION_NAME);
        setName(MAIN_WINDOW_NAME);
        fillContentPane(makeSnipersTable(portfolio), makeControls());
        pack();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private JPanel makeControls() {
        JPanel controls = new JPanel(new FlowLayout());
        itemIdField = new JTextField();
        itemIdField.setColumns(25);
        itemIdField.setName(NEW_ITEM_ID_NAME);

        stopPriceField = new JFormattedTextField(NumberFormat.getIntegerInstance());
        stopPriceField.setColumns(7);
        stopPriceField.setName(NEW_ITEM_STOP_PRICE_NAME);

        controls.add(itemIdField);
        controls.add(stopPriceField);

        JButton joinAuctionButton = new JButton("Join Auction");
        joinAuctionButton.setName(JOIN_BUTTON_NAME);
        joinAuctionButton.addActionListener(e -> {
            for (UserRequestListener userRequest : userRequests) {
                userRequest.joinAuction(new Item(
                        itemId(), stopPrice()));
            }
        });
        controls.add(joinAuctionButton);

        return controls;
    }

    private String itemId() {
        return itemIdField.getText();
    }

    private int stopPrice() {
        return ((Number)stopPriceField.getValue()).intValue();
    }

    private void fillContentPane(JTable snipersTable, JPanel controls) {
        final Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(controls, BorderLayout.NORTH);
        contentPane.add(new JScrollPane(snipersTable), BorderLayout.CENTER);
    }

    private JTable makeSnipersTable(SniperPortfolio portfolio) {
        SnipersTableModel model = new SnipersTableModel();
        portfolio.addPortfolioListener(model);
        final JTable snipersTable = new JTable(model);
        snipersTable.setName(SNIPERS_TABLE_NAME);
        return snipersTable;
    }

    public void addUserRequestListener(UserRequestListener userRequestListener) {
        userRequests.add(userRequestListener);
    }
}
