package it.polimi.ingsw.Graphical;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChatPanel extends JPanel {
    private final JTextArea chatArea;
    private final JTextArea inputArea;
    private MainFrame f;
    public ChatPanel(MainFrame f) {
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
        chatArea.setFont(plainFont); // Imposta il font e la dimensione

        inputArea = new JTextArea(4, 30);
        inputArea.setLineWrap(true);
        inputArea.setWrapStyleWord(true);
        inputArea.setFont(plainFont); // Imposta il font e la dimensione

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

    private void sendChatMessage() {
        String message = inputArea.getText().trim();
        if (!message.isEmpty()) {
            chatArea.append(" Me: " + message + "\n");
            inputArea.setText("");
            f.sendChatMessage(message);
        }
    }

    public void addChatMessage(String message, String nickname) {
        //if(nickname.equals("game")) ;
        chatArea.append(" "+nickname.toUpperCase() +": "+ message + "\n");
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Chat Panel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 600);
        ChatPanel c=new ChatPanel(null);
        frame.add(c);
        frame.setVisible(true);
        c.addChatMessage("test", "test");
        c.addChatMessage("test", "game");

    }
}

