package it.polimi.ingsw.Graphical;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * the panel that contains chat between all players and between only two players.
 */
public class ChatPanel extends JPanel {
    /**
     * Chat Area is where you can visualize all messages.
     */
    private final JTextArea chatArea;
    /**
     * Input area is where you can write any message.
     */
    private final JTextArea inputArea;
    /**
     * Recipient that contains all the nicknames of  message receievers and the public board option.
     */
    private  JComboBox<String> recipientComboBox;

    private final Font plainFont;
    private final MainFrame f;

    /**
     * Constructor: creates the input area , the chat area and the send button;
     * then it creates scroll pane to be able to send and receive any number of messages.
     * @param f: the UI that communicates with the Main Frame.
     */
    public ChatPanel(MainFrame f) {
        super(new BorderLayout());
        this.f = f;
        setMinimumSize(new Dimension(300, 500));
        setPreferredSize(new Dimension(300, 500));

        plainFont = new Font("Serif", Font.PLAIN, 16);

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

        add(chatScrollPane, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);
    }

    /**
     * Creates the Recipient with all the names and the "Public" option.
     * @param nickname : contains all the nicknames of other players who you can chat to.
     */
  public void createRecipient(String[] nickname) {
      recipientComboBox = new JComboBox<>(getRecipientOptions(nickname));
      recipientComboBox.setFont(plainFont);
      recipientComboBox.setToolTipText("Recipient (select 'Public' for public message)");

      JPanel recipientPanel = new JPanel(new BorderLayout());
      recipientPanel.add(recipientComboBox, BorderLayout.CENTER);
      recipientPanel.add(new JLabel("Recipient:"), BorderLayout.WEST);

      add(recipientPanel, BorderLayout.NORTH);

  }

    /**
     * This the getter of all names in Recipient to chat to and Public option.
     * @param nicknames nicknames of all players
     * @return
     */
    private String[] getRecipientOptions(String[] nicknames) {
        String[] options = new String[nicknames.length + 1];
        options[0] = "Public";
        System.arraycopy(nicknames, 0, options, 1, nicknames.length);
        return options;
    }

    /**
     * Send message option,is the command that actually sends the message through send button.
     */
    private void sendChatMessage() {
        String message = inputArea.getText().trim();
        String recipient = "Public";
        if (recipientComboBox!=null) {
             recipient = (String) recipientComboBox.getSelectedItem();
        }
        if (!message.isEmpty()) {
            if ("Public".equals(recipient)) {
                chatArea.append(" Me: " + message + "\n");
                f.sendChatMessage(message);
            } else {
                chatArea.append(" Me to " + recipient + ": " + message + "\n");
                f.sendPrivateChatMessage(message, recipient);
            }
            inputArea.setText("");
            recipientComboBox.setSelectedIndex(0);
        }
    }

    /**
     * This is the line that add the message to the chat area where who received the message can read it.
     * @param message the message received
     * @param nickname
     */
    public void addChatMessage(String message, String nickname) {
        chatArea.append(" " + nickname.toUpperCase() + ": " + message + "\n");
    }


}

