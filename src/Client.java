import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Client {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 9090);
        InputStream is = socket.getInputStream();
        OutputStream os = socket.getOutputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(os), true);

        // Set up the chat window (GUI)
        JFrame frame = new JFrame("Client Chat");
        frame.setLayout(new BorderLayout());
        frame.setSize(500, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        // Background image
        JLabel background = new JLabel(new ImageIcon("path_to_your_image.jpg"));  // Set the path to your background image
        frame.setContentPane(background);  // Set the background image to the frame content pane
        frame.setLayout(new BorderLayout());

        // Chat Area (custom scroll)
        JPanel chatPanel = new JPanel();
        chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));  // Vertical layout
        JScrollPane scrollPane = new JScrollPane(chatPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(400, 300));  // Set fixed size
        frame.add(scrollPane, BorderLayout.CENTER);

        // Bubble input field
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());
        JTextField textField = new JTextField();
        textField.setFont(new Font("Arial", Font.PLAIN, 14));
        textField.setBackground(new Color(240, 240, 240));  // Same as background color
        textField.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));  // Padding for bubble shape
        textField.setOpaque(true);
        textField.setForeground(Color.BLACK);
        textField.setBorder(BorderFactory.createLineBorder(new Color(0, 153, 0), 2, true));  // Rounded corners

        inputPanel.add(textField, BorderLayout.CENTER);

        // Send Button
        JPanel buttonPanel = new JPanel();
        JButton sendButton = new JButton("Send");
        sendButton.setFont(new Font("Arial", Font.BOLD, 14));
        sendButton.setBackground(new Color(34, 193, 195));  // Green color for button
        sendButton.setForeground(Color.WHITE);
        sendButton.setFocusPainted(false);
        sendButton.setPreferredSize(new Dimension(100, 40));
        buttonPanel.add(sendButton);

        inputPanel.add(buttonPanel, BorderLayout.EAST);  // Align the button on the right
        frame.add(inputPanel, BorderLayout.SOUTH);  // Position the input area at the bottom

        // Action listener for the send button
        sendButton.addActionListener(e -> {
            String msg = textField.getText();
            pw.println(msg);  // Send message to server
            addChatBubble(chatPanel, "You: " + msg, true);  // Add outgoing message to chat area
            textField.setText("");  // Clear the input field

            if (msg.equalsIgnoreCase("bye")) {
                try {
                    socket.close();  // Close the socket
                    System.exit(0);  // Exit the program
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        // Setup Frame
        frame.setVisible(true);

        // Listen for messages from the server
        String msg;
        do {
            msg = br.readLine();
            addChatBubble(chatPanel, "Server: " + msg, false);  // Add incoming message to chat area
        } while (!msg.equalsIgnoreCase("bye"));
    }

    // Method to add chat bubble
    private static void addChatBubble(JPanel chatPanel, String msg, boolean isClient) {
        JPanel bubblePanel = new JPanel();
        bubblePanel.setLayout(new FlowLayout(isClient ? FlowLayout.RIGHT : FlowLayout.LEFT));
        bubblePanel.setBackground(new Color(240, 240, 240));

        JLabel label = new JLabel(msg);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        label.setBackground(isClient ? new Color(34, 193, 195) : new Color(0, 153, 0));  // Green color for bubbles
        label.setOpaque(true);
        label.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        label.setForeground(Color.WHITE);
        label.setBorder(BorderFactory.createLineBorder(new Color(0, 153, 0), 1, true));  // Rounded border for the bubble

        // Rounded corners for bubble
        label.setBorder(BorderFactory.createLineBorder(new Color(0, 153, 0), 15));

        bubblePanel.add(label);
        chatPanel.add(bubblePanel);
        chatPanel.revalidate();  // Refresh the panel
        chatPanel.repaint();
    }
}



















