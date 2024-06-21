package it.polimi.ingsw.Graphical;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChatPanel extends JPanel {
    private JTextArea chatArea;
    private JTextArea inputArea;
    private JButton sendButton;
    public ChatPanel() {
        super(new BorderLayout());
        setMinimumSize(new Dimension(300, 500));

        chatArea = new JTextArea(20, 30);
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);

        inputArea = new JTextArea(5, 30);
        inputArea.setLineWrap(true);
        inputArea.setWrapStyleWord(true);

        sendButton = new JButton("Send");
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
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

    private void sendMessage() {
        String message = inputArea.getText().trim();
        if (!message.isEmpty()) {
            chatArea.append("Me: " + message + "\n");
            inputArea.setText("");

        }
    }

    public void receiveMessage(String message,String nickname) {
        chatArea.append(nickname +":"+ message + "\n");
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Chat Panel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 600);
        frame.add(new ChatPanel());
        frame.setVisible(true);
    }
}

