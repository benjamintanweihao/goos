package io.benjamintan.goos;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class MainWindow extends JFrame {
    private static final String SNIPERS_TABLE_NAME = "Snipers";
    public static final String MAIN_WINDOW_NAME = "Auction Sniper Main";
    public static final String APPLICATION_NAME = "Auction Sniper";
    public static String NEW_ITEM_ID_NAME = "item id";
    public static String JOIN_BUTTON_NAME = "Join Auction";

    private SnipersTableModel snipers;
    ArrayList<UserRequestListener> userRequests = new ArrayList<>();


    public MainWindow(SnipersTableModel snipers) {
        super(APPLICATION_NAME);
        this.snipers = snipers;
        setName(MAIN_WINDOW_NAME);
        fillContentPane(makeSnipersTable(), makeControls());
        pack();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private JPanel makeControls() {
       JPanel controls = new JPanel(new FlowLayout());
       final JTextField itemIdField = new JTextField();
       itemIdField.setColumns(25);
       itemIdField.setName(NEW_ITEM_ID_NAME);
       controls.add(itemIdField);

       JButton joinAuctionButton = new JButton("Join Auction");
       joinAuctionButton.setName(JOIN_BUTTON_NAME);
       joinAuctionButton.addActionListener(e -> {
           for (UserRequestListener userRequest : userRequests) {
               userRequest.joinAuction(itemIdField.getText());
           }
       });
       controls.add(joinAuctionButton);

       return controls;
    }

    private void fillContentPane(JTable snipersTable, JPanel controls) {
        final Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(controls, BorderLayout.NORTH);
        contentPane.add(new JScrollPane(snipersTable), BorderLayout.CENTER);
    }

    private JTable makeSnipersTable() {
        final JTable snipersTable = new JTable(snipers);
        snipersTable.setName(SNIPERS_TABLE_NAME);
        return snipersTable;
    }

    public void addUserRequestListener(UserRequestListener userRequestListener) {
        userRequests.add(userRequestListener);
    }
}
