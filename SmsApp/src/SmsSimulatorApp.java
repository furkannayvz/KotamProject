import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SmsSimulatorApp {

    // --- AYARLAR ---
    private static final String API_URL = "http://localhost:8080/api/sms/incoming";
    private static final String PLACEHOLDER_TEXT = "5347101010 KALAN";

    // --- GÖRSEL TASARIM SABİTLERİ ---
    private static final Color THEME_COLOR = new Color(14, 57, 112);
    private static final Color BACKGROUND_COLOR = new Color(229, 234, 244);
    private static final Color BUBBLE_USER_COLOR = new Color(215, 228, 255);
    private static final Color BUBBLE_SYSTEM_COLOR = Color.WHITE;
    // ---------------

    private JFrame frame;
    private JTextField messageField;
    private JPanel conversationPanel;
    private JButton sendButton;
    private JScrollPane scrollPane;
    private final HttpClient httpClient;

    public SmsSimulatorApp() {
        this.httpClient = HttpClient.newHttpClient();
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> new SmsSimulatorApp().createAndShowGUI());
    }

    private void createAndShowGUI() {
        frame = new JFrame("KOTAM SMS Simulator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(420, 700);
        frame.setMinimumSize(new Dimension(380, 500));
        frame.setLayout(new BorderLayout());

        conversationPanel = new JPanel();
        conversationPanel.setLayout(new BoxLayout(conversationPanel, BoxLayout.Y_AXIS));
        conversationPanel.setBackground(BACKGROUND_COLOR);
        conversationPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        scrollPane = new JScrollPane(conversationPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);

        frame.add(createHeaderPanel(), BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(createBottomPanel(), BorderLayout.SOUTH);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new GridBagLayout());
        headerPanel.setBackground(THEME_COLOR);
        headerPanel.setPreferredSize(new Dimension(420, 90));

        try (InputStream is = SmsSimulatorApp.class.getResourceAsStream("/kotambeyaz.png")) {
            if (is == null) throw new IOException("Logo file not found in resources folder!");
            BufferedImage logoImage = ImageIO.read(is);
            Image scaledLogo = logoImage.getScaledInstance(-1, 50, Image.SCALE_SMOOTH);
            JLabel logoLabel = new JLabel(new ImageIcon(scaledLogo));
            headerPanel.add(logoLabel);
        } catch (Exception e) {
            JLabel titleLabel = new JLabel("KOTAM");
            titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
            titleLabel.setForeground(Color.WHITE);
            headerPanel.add(titleLabel);
            System.err.println("Error loading logo: " + e.getMessage());
        }
        return headerPanel;
    }

    private JPanel createBottomPanel() {
        // ... Bu metodun içeriği aynı kalabilir, değişiklik yok ...
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 0));
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        messageField = createStyledTextField();
        sendButton = createSendButton();

        bottomPanel.add(messageField, BorderLayout.CENTER);
        bottomPanel.add(sendButton, BorderLayout.EAST);
        return bottomPanel;
    }

    private JTextField createStyledTextField() {
        // ... Bu metodun içeriği aynı kalabilir, değişiklik yok ...
        JTextField textField = new JTextField();
        textField.setFont(new Font("Arial", Font.PLAIN, 14));
        textField.setBackground(new Color(240, 242, 245));
        textField.setForeground(Color.GRAY);
        textField.setText(PLACEHOLDER_TEXT);
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 223, 228), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        textField.addActionListener(this::performSendAction);
        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textField.getText().equals(PLACEHOLDER_TEXT)) {
                    textField.setText("");
                    textField.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (textField.getText().isEmpty()) {
                    textField.setForeground(Color.GRAY);
                    textField.setText(PLACEHOLDER_TEXT);
                }
            }
        });
        return textField;
    }
    private JButton createSendButton() {
        // ... Bu metodun içeriği aynı kalabilir, değişiklik yok ...
        JButton button = new JButton("Send");
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(THEME_COLOR);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(new EmptyBorder(12, 20, 12, 20));
        button.setFocusPainted(false);
        button.addActionListener(this::performSendAction);
        return button;
    }

    private void performSendAction(ActionEvent e) {
        // ... Bu metodun içeriği aynı kalabilir, değişiklik yok ...
        String fullInput = messageField.getText().trim();
        Pattern pattern = Pattern.compile("^(\\S+)\\s+(.*)$");
        Matcher matcher = pattern.matcher(fullInput);
        if (!matcher.matches()) {
            JOptionPane.showMessageDialog(frame,
                    "Invalid format. Please use: <PhoneNumber> <Message>\nExample: " + PLACEHOLDER_TEXT,
                    "Format Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String from = matcher.group(1);
        String text = matcher.group(2);
        addMessageBubble("YOU (" + from + "):\n" + text, true);
        messageField.setText("");
        sendButton.setEnabled(false);
        CompletableFuture.runAsync(() -> sendSmsToBackend(from, text));
    }

    private void sendSmsToBackend(String from, String text) {
        // ... Bu metodun içeriği aynı kalabilir, değişiklik yok ...
        try {
            String jsonBody = String.format("{\"from\":\"%s\", \"text\":\"%s\"}", from, text);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            SwingUtilities.invokeLater(() -> addMessageBubble("KOTAM:\n" + response.body(), false));
        } catch (Exception ex) {
            SwingUtilities.invokeLater(() -> addMessageBubble("SYSTEM ERROR:\nCould not connect to the server. Is it running?", false));
        } finally {
            SwingUtilities.invokeLater(() -> sendButton.setEnabled(true));
        }
    }

    // --- DEĞİŞİKLİKLERİN YAPILDIĞI ANA METOT ---
    private void addMessageBubble(String text, boolean isUser) {
        // Metni HTML formatına çeviriyoruz. Bu, metnin otomatik olarak satır atlamasını sağlar.
        // 'width: 250px' değeri, mesaj kutucuğunun maksimum genişliğini belirler.
        String htmlText = String.format("<html><body style='width: 250px;'>%s</body></html>",
                text.replace("\n", "<br>"));

        // Yeni RoundedBubblePanel'imizi oluşturuyoruz.
        Color bubbleColor = isUser ? BUBBLE_USER_COLOR : BUBBLE_SYSTEM_COLOR;
        RoundedBubblePanel bubble = new RoundedBubblePanel(htmlText, bubbleColor, 20); // 20: köşe yuvarlaklığı

        // Hizalamayı sağlamak için bir sarmalayıcı panel kullanıyoruz.
        // FlowLayout, içindeki bileşenin kendi tercih ettiği boyutu almasını sağlar (dinamik boyutlandırma).
        JPanel wrapperPanel = new JPanel(new FlowLayout(isUser ? FlowLayout.RIGHT : FlowLayout.LEFT));
        wrapperPanel.setOpaque(false); // Arka planı görünmez yap
        wrapperPanel.add(bubble);

        conversationPanel.add(wrapperPanel);
        conversationPanel.add(Box.createRigidArea(new Dimension(0, 5))); // Mesajlar arası boşluk

        frame.revalidate();
        frame.repaint();
        scrollToBottom();
    }

    private void scrollToBottom() {
        // ... Bu metodun içeriği aynı kalabilir, değişiklik yok ...
        SwingUtilities.invokeLater(() -> {
            JScrollBar vertical = scrollPane.getVerticalScrollBar();
            vertical.setValue(vertical.getMaximum());
        });
    }
}
