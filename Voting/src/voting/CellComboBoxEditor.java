package voting;

import java.util.Vector;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;

public class CellComboBoxEditor extends DefaultCellEditor {
    public CellComboBoxEditor(Vector<String> items) {
        super(new JComboBox(items));
    }
}
