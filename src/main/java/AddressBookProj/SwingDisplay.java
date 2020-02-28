package AddressBookProj;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

@Component
public class SwingDisplay {
    private String title = "Address Book";
    private int width = 500;
    private int height = 500;

    private int selectedBuddy = -1;

    private JTextField nameTF;
    private JTextField phoneNumTF;
    private JTextField addressTF;

    SwingController observer = null;

    @Autowired
    public SwingDisplay(){
        //render();
    }

    public void setController(SwingController newController) {
        this.observer = newController;
    }

    public DefaultListModel loadList() {
        DefaultListModel listModel = new DefaultListModel();

        if (observer != null) {
            ArrayList<String> data = observer.getData();

            for (String elem : data) {
                listModel.addElement(elem);
            }
        }

        return listModel;
    }


    private void render() {
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(width, height);

        JList list = new JList(loadList());
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setLayoutOrientation(JList.VERTICAL_WRAP);
        list.setVisibleRowCount(-1);
        list.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting() == false) {
                    if (!(list.getSelectedIndex() == -1)) {
                        selectedBuddy = list.getSelectedIndex();
                    }
                }
            }
        });

        JScrollPane listScroller = new JScrollPane(list);
        listScroller.setPreferredSize(new Dimension(250, 80));
        JPanel listPane = new JPanel();
        listPane.setLayout(new BoxLayout(listPane, BoxLayout.PAGE_AXIS));

        JLabel label = new JLabel("Address Book");
        label.setLabelFor(list);
        listPane.add(label);
        listPane.add(Box.createRigidArea(new Dimension(0,5)));
        listPane.add(listScroller);
        listPane.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JLabel nameLabel = new JLabel("Name: ");
        nameTF = new JTextField("");
        JPanel namePanel = new JPanel(new BorderLayout());
        namePanel.add(nameLabel, BorderLayout.WEST);
        namePanel.add(nameTF, BorderLayout.CENTER);
        listPane.add(namePanel);

        JLabel phoneNumLabel = new JLabel("Phone Number: ");
        phoneNumTF = new JTextField("");
        JPanel phoneNumPanel = new JPanel(new BorderLayout());
        phoneNumPanel.add(phoneNumLabel, BorderLayout.WEST);
        phoneNumPanel.add(phoneNumTF, BorderLayout.CENTER);
        listPane.add(phoneNumPanel);

        JLabel addressLabel = new JLabel("Address: ");
        addressTF = new JTextField("");
        JPanel addressPanel = new JPanel(new BorderLayout());
        addressPanel.add(addressLabel, BorderLayout.WEST);
        addressPanel.add(addressTF, BorderLayout.CENTER);
        listPane.add(addressPanel);

        JButton button = new JButton("Clear Address Book");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                observer.handleButtonPress("CLEAR_BUDDIES");
                list.setModel(loadList());
            }
        });

        JButton removeButton = new JButton("Remove Buddy");
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                observer.handleButtonPress("REMOVE_BUDDY");
                list.setModel(loadList());
            }
        });

        JButton addButton = new JButton("Add Buddy");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                observer.handleButtonPress("ADD_BUDDY");
                list.setModel(loadList());
            }
        });

        JButton readDbButton = new JButton("Read DB");
        readDbButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                observer.handleButtonPress("READ_DB");
            }
        });

        //Lay out the buttons from left to right.
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
        buttonPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        buttonPane.add(Box.createHorizontalGlue());
        buttonPane.add(removeButton);
        //buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPane.add(button);
        //buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPane.add(addButton);
        buttonPane.add(readDbButton);

        //Put everything together, using the content pane's BorderLayout.
        Container contentPane = frame.getContentPane();
        contentPane.add(listPane, BorderLayout.CENTER);
        contentPane.add(buttonPane, BorderLayout.PAGE_END);

        frame.setVisible(true);
    }

    public void displayDataInAlert(ArrayList<String> data) {
        String msg = "";
        for (String str : data){
            System.out.println(str);
            msg += str;
        }
        JOptionPane.showMessageDialog(null, msg);
    }

    public int getSelectedBuddy() {
        return this.selectedBuddy;
    }

    public String getName() {
        return nameTF.getText();
    }

    public String getPhoneNumber() {
        return phoneNumTF.getText();
    }

    public String getAddress() {
        return addressTF.getText();
    }


}
