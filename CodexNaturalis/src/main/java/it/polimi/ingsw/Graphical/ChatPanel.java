package it.polimi.ingsw.Graphical;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChatPanel extends JPanel {
    private final JTextArea chatArea;
    private final JTextArea inputArea;
    private final JComboBox<String> recipientComboBox;
    private MainFrame f;

    public ChatPanel(MainFrame f, String[] nicknames) {
        super(new BorderLayout());
        this.f = f;
        setMinimumSize(new Dimension(300, 500));
        setPreferredSize(new Dimension(300, 500));

        Font plainFont = new Font("Serif", Font.PLAIN, 16);

        chatArea = new JTextArea(20, 30);
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        chatArea.setBackground(Color.decode("#d9dbc1"));
        chatArea.setFont(plainFont);

        inputArea = new JTextArea(4, 30);
        inputArea.setLineWrap(true);
        inputArea.setWrapStyleWord(true);
        inputArea.setFont(plainFont);

        recipientComboBox = new JComboBox<>(getRecipientOptions(nicknames));
        recipientComboBox.setFont(plainFont);
        recipientComboBox.setToolTipText("Recipient (select 'Public' for public message)");

        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendChatMessage();
            }
        });

        JScrollPane chatScrollPane = new JScrollPane(chatArea);
        JScrollPane inputScrollPane = new JScrollPane(inputArea);

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(inputScrollPane, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        JPanel recipientPanel = new JPanel(new BorderLayout());
        recipientPanel.add(recipientComboBox, BorderLayout.CENTER);
        recipientPanel.add(new JLabel("Recipient:"), BorderLayout.WEST);

        add(chatScrollPane, BorderLayout.CENTER);
        add(recipientPanel, BorderLayout.NORTH);
        add(inputPanel, BorderLayout.SOUTH);
    }

    private String[] getRecipientOptions(String[] nicknames) {
        String[] options = new String[nicknames.length + 1];
        options[0] = "Public";
        System.arraycopy(nicknames, 0, options, 1, nicknames.length);
        return options;
    }

    private void sendChatMessage() {
        String message = inputArea.getText().trim();
        String recipient = (String) recipientComboBox.getSelectedItem();
        if (!message.isEmpty()) {
            if ("Public".equals(recipient)) {
                chatArea.append(" Me: " + message + "\n");
                f.sendChatMessage(message);
            } else {
                chatArea.append(" Me to " + recipient + ": " + message + "\n");
                f.sendPrivateChatMessage(message, recipient);
            }
            inputArea.setText("");
            recipientComboBox.setSelectedIndex(0); // Reset to "Public"
        }
    }

    public void addChatMessage(String message, String nickname) {
        chatArea.append(" " + nickname.toUpperCase() + ": " + message + "\n");
    }


    public static void main(String[] args) {
        JFrame frame = new JFrame("Chat Panel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 600);
        String[] nicknames = {"Player1", "Player2", "Player3"};
        ChatPanel c = new ChatPanel(null, nicknames);
        frame.add(c);
        frame.setVisible(true);
        c.addChatMessage("test", "test");
        c.addChatMessage("test", "game");
    }
}

