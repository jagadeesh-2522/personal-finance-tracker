import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;

public class AnalysisPanel extends JPanel {
    
    private CategoryChartPanel categoryChartPanel;
    private TransactionChartPanel transactionChartPanel;
    private ExpenseSummaryPanel summaryPanel;
    
    private static final Color BACKGROUND = new Color(248, 247, 255);
    
    public AnalysisPanel() {
        setLayout(new BorderLayout(15, 15));
        setBackground(BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Create the three chart panels
        categoryChartPanel = new CategoryChartPanel();
        transactionChartPanel = new TransactionChartPanel();
        summaryPanel = new ExpenseSummaryPanel();
        
        // Create layout: Summary on top, Category and Transaction charts below
        JPanel topSection = new JPanel(new BorderLayout());
        topSection.setBackground(BACKGROUND);
        topSection.add(summaryPanel, BorderLayout.CENTER);
        
        JPanel chartsSection = new JPanel(new GridLayout(1, 2, 15, 0));
        chartsSection.setBackground(BACKGROUND);
        chartsSection.add(categoryChartPanel);
        chartsSection.add(transactionChartPanel);
        
        add(topSection, BorderLayout.NORTH);
        add(chartsSection, BorderLayout.CENTER);
    }
    
    public void loadAllData() {
        categoryChartPanel.loadChartData();
        transactionChartPanel.loadChartData();
        summaryPanel.loadSummaryData();
    }
    
    public void refreshAllGraphs() {
        loadAllData();
    }
}
