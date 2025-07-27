import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Hem yuvarlak köşeli bir arka plan çizen hem de içindeki metni
 * HTML formatında gösteren özel bir JPanel sınıfı.
 */
public class RoundedBubblePanel extends JPanel {

    private final int cornerRadius;
    private final Color bubbleColor;

    public RoundedBubblePanel(String htmlText, Color bubbleColor, int cornerRadius) {
        super(new BorderLayout());
        this.bubbleColor = bubbleColor;
        this.cornerRadius = cornerRadius;
        setOpaque(false); // Arka planı kendimiz çizeceğimiz için panelin kendi boyamasını kapatıyoruz.

        JLabel textLabel = new JLabel(htmlText);
        textLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        textLabel.setBorder(new EmptyBorder(8, 12, 8, 12)); // Metin için iç boşluk
        add(textLabel, BorderLayout.CENTER);
    }

    @Override
    protected void paintComponent(Graphics g) {
        // Daha pürüzsüz çizimler için Antialiasing'i aktif ediyoruz.
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Arka planı belirlediğimiz renkte ve yuvarlak köşeli olarak çiziyoruz.
        g2.setColor(bubbleColor);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);

        g2.dispose();
        // Bu, metnin ve diğer bileşenlerin çizilmesi için gereklidir.
        super.paintComponent(g);
    }

    @Override
    public Insets getInsets() {
        // Panelin etrafında bir miktar boşluk bırakarak gölgelendirme veya kenarlık efekti için yer açar.
        int padding = 2;
        return new Insets(padding, padding, padding, padding);
    }
}
