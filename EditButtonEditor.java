import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EditButtonEditor extends DefaultCellEditor {
    
    private JButton button;
    private String label;
    private FinanceTrackerApp app;
    private int row;
    private static final Color EDIT_BLUE = new Color(0, 123, 255);
    
    public EditButtonEditor(JCheckBox checkBox, FinanceTrackerApp app) {
        super(checkBox);
        this.app = app;
        
        button = new JButton();
        button.setOpaque(true);
        button.setFont(new Font("Segoe UI", Font.BOLD, 10));
        button.setForeground(Color.WHITE);
        button.setBackground(EDIT_BLUE);
        button.setBorder(BorderFactory.createEmptyBorder(3, 8, 3, 8));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Call editExpense directly when button is clicked
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                app.editExpense(row);
                fireEditingStopped();
            }
        });
    }
    
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        this.row = row;
        label = (value == null) ? "Edit" : value.toString();
        button.setText(label);
        return button;
    }
    
    public Object getCellEditorValue() {
        return label;
    }
}
