import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class EditButtonRenderer extends JButton implements TableCellRenderer {
    
    private static final Color EDIT_BLUE = new Color(0, 123, 255);
    
    public EditButtonRenderer() {
        setOpaque(true);
        setFont(new Font("Segoe UI", Font.BOLD, 10));
        setForeground(Color.WHITE);
        setBackground(EDIT_BLUE);
        setBorder(BorderFactory.createEmptyBorder(3, 8, 3, 8));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        setText((value == null) ? "Edit" : value.toString());
        setForeground(Color.WHITE);
        setBackground(EDIT_BLUE);
        
        if (isSelected) {
            setBackground(EDIT_BLUE.darker());
        }
        
        return this;
    }
}
