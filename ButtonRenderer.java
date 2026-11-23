import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class ButtonRenderer extends JButton implements TableCellRenderer {
    
    private static final Color NAVY = new Color(25, 40, 80);
    private static final Color DELETE_RED = new Color(220, 53, 69);
    
    public ButtonRenderer() {
        setOpaque(true);
        setFont(new Font("Segoe UI", Font.BOLD, 10));
        setForeground(Color.WHITE);
        setBackground(DELETE_RED);
        setBorder(BorderFactory.createEmptyBorder(3, 8, 3, 8));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        setText((value == null) ? "Delete" : value.toString());
        setForeground(Color.WHITE);
        setBackground(DELETE_RED);
        
        if (isSelected) {
            setBackground(DELETE_RED.darker());
        }
        
        return this;
    }
}
