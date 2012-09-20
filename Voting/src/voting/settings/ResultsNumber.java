package voting.settings;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

public class ResultsNumber extends JDialog {

    private final JPanel contentPanel = new JPanel();
    private int number;
    private boolean numberChanged = false;
    private JSpinner spinner;

    public ResultsNumber(int number) {
        this.number = number;
        initialize();
    }
    private void initialize() {
        setResizable(false);
        setTitle("Result Number");
        setModal(true);
        setBounds(100, 100, 171, 123);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(new MigLayout("", "[][]", "[]"));
        {
            JLabel lblNumberOfResults = new JLabel("Number of results:");
            contentPanel.add(lblNumberOfResults, "cell 0 0,alignx right");
        }
        {
            spinner = new JSpinner();
            spinner.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent arg0) {
                    changeNumber();
                }
            });
            spinner.setValue(number);
            contentPanel.add(spinner, "cell 1 0");
        }
        {
            JPanel buttonPane = new JPanel();
            buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
            getContentPane().add(buttonPane, BorderLayout.SOUTH);
            {
                JButton okButton = new JButton("OK");
                okButton.setActionCommand("OK");
                buttonPane.add(okButton);
                okButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        setVisible(false);
                        dispose();
                    }
                });
                getRootPane().setDefaultButton(okButton);
            }
            {
                JButton cancelButton = new JButton("Cancel");
                cancelButton.setActionCommand("Cancel");
                cancelButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        number = -1; // -1 acts like a null
                        setVisible(false);
                        dispose();
                    }
                });
                buttonPane.add(cancelButton);
            }
        }
    }
        
    protected void changeNumber() {
        number = (Integer) spinner.getValue();
        
    }
    public int showDialog() {
        setVisible(true);
        return number;
    }

}
