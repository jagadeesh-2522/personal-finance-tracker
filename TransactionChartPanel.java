import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.sql.SQLException;

public class TransactionChartPanel extends JPanel {
    
    private ArrayList<TransactionData> transactionDataList;
    private static final Color NAVY = new Color(25, 40, 80);
    private static final Color PURPLE_PRIMARY = new Color(108, 68, 156);
    private static final Color BACKGROUND = new Color(248, 247, 255);
    private static final Color TEXT_DARK = new Color(40, 50, 80);
    private static final Color BORDER_SUBTLE = new Color(220, 215, 235);
    private static final Color BAR_COLOR = new Color(108, 68, 156);
    private static final Color BAR_HOVER_COLOR = new Color(142, 103, 197);
    
    public TransactionChartPanel() {
        this.transactionDataList = new ArrayList<>();
        setBackground(BACKGROUND);
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_SUBTLE, 1),
            BorderFactory.createEmptyBorder(20, 30, 20, 30)
        ));
    }
    
    public void loadChartData() {
        try {
            transactionDataList.clear();
            ArrayList<TransactionData> data = DatabaseExpenseManager.getTransactionChartData();
            transactionDataList.addAll(data);
            repaint();
        } catch (SQLException e) {
            System.err.println("Error loading chart data: " + e.getMessage());
            transactionDataList.clear();
            repaint();
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int panelWidth = getWidth();
        int panelHeight = getHeight();
        
        // Chart margins
        int topMargin = 60;
        int bottomMargin = 80;
        int leftMargin = 60;
        int rightMargin = 30;
        
        int chartAreaWidth = panelWidth - leftMargin - rightMargin;
        int chartAreaHeight = panelHeight - topMargin - bottomMargin;
        
        // Draw title
        g2d.setColor(NAVY);
        g2d.setFont(new Font("Segoe UI", Font.BOLD, 18));
        String title = "Transaction-wise Expense Analysis";
        FontMetrics titleMetrics = g2d.getFontMetrics();
        int titleX = (panelWidth - titleMetrics.stringWidth(title)) / 2;
        g2d.drawString(title, titleX, 30);
        
        // Draw axes
        g2d.setColor(TEXT_DARK);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawLine(leftMargin, panelHeight - bottomMargin, panelWidth - rightMargin, panelHeight - bottomMargin); // X-axis
        g2d.drawLine(leftMargin, topMargin, leftMargin, panelHeight - bottomMargin); // Y-axis
        
        // Draw axis labels
        g2d.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        g2d.drawString("Transaction ID", panelWidth / 2 - 40, panelHeight - 15);
        
        // Y-axis label (rotated)
        AffineTransform originalTransform = g2d.getTransform();
        g2d.rotate(-Math.PI / 2);
        g2d.drawString("Expense Amount (₹)", -panelHeight / 2 + 40, 15);
        g2d.setTransform(originalTransform);
        
        // Handle empty data
        if (transactionDataList.isEmpty()) {
            g2d.setColor(new Color(150, 150, 150));
            g2d.setFont(new Font("Segoe UI", Font.ITALIC, 13));
            String emptyMessage = "No transactions to display";
            FontMetrics metrics = g2d.getFontMetrics();
            int emptyX = (panelWidth - metrics.stringWidth(emptyMessage)) / 2;
            int emptyY = topMargin + (chartAreaHeight / 2) + (metrics.getAscent() / 2);
            g2d.drawString(emptyMessage, emptyX, emptyY);
            return;
        }
        
        // Calculate bar dimensions
        int numberOfBars = transactionDataList.size();
        int barWidth = Math.max(30, (chartAreaWidth - (numberOfBars * 5)) / numberOfBars);
        int spacingBetweenBars = 5;
        
        // Find max amount for scaling
        double maxAmount = 0;
        for (TransactionData data : transactionDataList) {
            if (data.getAmount() > maxAmount) {
                maxAmount = data.getAmount();
            }
        }
        
        // Add 20% padding to max for better visualization
        maxAmount = maxAmount * 1.2;
        
        // Draw grid lines and Y-axis labels
        g2d.setColor(new Color(240, 237, 250));
        g2d.setStroke(new BasicStroke(1));
        g2d.setFont(new Font("Segoe UI", Font.PLAIN, 9));
        
        int gridLines = 5;
        for (int i = 0; i <= gridLines; i++) {
            int yPos = panelHeight - bottomMargin - (int) (i * (chartAreaHeight / (double) gridLines));
            g2d.drawLine(leftMargin, yPos, panelWidth - rightMargin, yPos);
            
            double value = (i * maxAmount) / gridLines;
            String label = String.format("%.0f", value);
            FontMetrics metrics = g2d.getFontMetrics();
            g2d.setColor(TEXT_DARK);
            g2d.drawString(label, leftMargin - 45, yPos + 3);
            g2d.setColor(new Color(240, 237, 250));
        }
        
        // Draw bars
        int totalBarWidth = numberOfBars * barWidth + (numberOfBars - 1) * spacingBetweenBars;
        int startX = leftMargin + (chartAreaWidth - totalBarWidth) / 2;
        
        g2d.setFont(new Font("Segoe UI", Font.BOLD, 9));
        
        for (int i = 0; i < numberOfBars; i++) {
            TransactionData data = transactionDataList.get(i);
            
            int barX = startX + i * (barWidth + spacingBetweenBars);
            double percentage = data.getAmount() / maxAmount;
            int barHeight = (int) (chartAreaHeight * percentage);
            int barY = panelHeight - bottomMargin - barHeight;
            
            // Draw bar
            g2d.setColor(BAR_COLOR);
            g2d.fillRect(barX, barY, barWidth, barHeight);
            
            // Draw bar border
            g2d.setColor(NAVY);
            g2d.setStroke(new BasicStroke(1));
            g2d.drawRect(barX, barY, barWidth, barHeight);
            
            // Draw amount above bar
            String amountStr = String.format("%.0f", data.getAmount());
            FontMetrics amountMetrics = g2d.getFontMetrics();
            int amountX = barX + (barWidth - amountMetrics.stringWidth(amountStr)) / 2;
            int amountY = barY - 5;
            g2d.setColor(PURPLE_PRIMARY);
            g2d.drawString(amountStr, amountX, amountY);
            
            // Draw transaction ID below bar
            String transactionIdStr = String.valueOf(data.getTransactionId());
            FontMetrics idMetrics = g2d.getFontMetrics();
            int idX = barX + (barWidth - idMetrics.stringWidth(transactionIdStr)) / 2;
            int idY = panelHeight - bottomMargin + 25;
            g2d.setColor(TEXT_DARK);
            g2d.drawString(transactionIdStr, idX, idY);
        }
    }
}
