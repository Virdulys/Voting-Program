package voting;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;

public class AddParticipant extends JDialog {

    private final JPanel contentPanel = new JPanel();
    private Participant participant = null; // participant
    private JTextField txtParticipantName;
    private JTextField txtTeamName;
    private JTextField txtPoints;
    private JFrame parent;

    /**
     * Create the dialog.
     */
    public AddParticipant(JFrame parent, boolean modal) {
        super(parent, modal);
        setModal(true);
        this.parent = parent;
        initialize();
    }
        
    public void initialize() {
        
        setTitle("Add Participant");
        setBounds(100, 100, 450, 156);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(new MigLayout("", "[][grow]", "[][][]"));
        {
            JLabel lblParticipantName = new JLabel("Participant name");
            lblParticipantName.setAlignmentX(Component.RIGHT_ALIGNMENT);
            contentPanel.add(lblParticipantName, "cell 0 0,alignx trailing");
            //lblParticipantName.setFont(new Font("SansSerif", Font.PLAIN, 12));
        }
        {
            txtParticipantName = new JTextField();
            contentPanel.add(txtParticipantName, "cell 1 0,growx");
            txtParticipantName.setColumns(10);
        }
        {
            JLabel lblTeamName = new JLabel("Team name");
            lblTeamName.setHorizontalTextPosition(SwingConstants.RIGHT);
            lblTeamName.setAlignmentX(Component.RIGHT_ALIGNMENT);
            lblTeamName.setHorizontalAlignment(SwingConstants.RIGHT);
            //lblTeamName.setFont(new Font("SansSerif", Font.PLAIN, 12));
            contentPanel.add(lblTeamName, "cell 0 1,alignx trailing");
        }
        {
            txtTeamName = new JTextField();
            contentPanel.add(txtTeamName, "cell 1 1,growx");
            txtTeamName.setColumns(10);
        }
        {
            JLabel lblPoints = new JLabel("Points");
            contentPanel.add(lblPoints, "cell 0 2,alignx trailing,aligny center");
            //lblPoints.setFont(new Font("SansSerif", Font.PLAIN, 12));
        }
        {
            txtPoints = new JTextField();
            txtPoints.setColumns(5);
            txtPoints.setPreferredSize(new Dimension(10, 20));
            txtPoints.setMinimumSize(new Dimension(1, 20));
            contentPanel.add(txtPoints, "cell 1 2");
        }
        {
            JPanel buttonPane = new JPanel();
            buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
            getContentPane().add(buttonPane, BorderLayout.SOUTH);
            {
                JButton okButton = new JButton("OK");
                okButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        participant = new Participant();
                        participant.setParticipantName(txtParticipantName.getText());
                        participant.setTeamName(txtTeamName.getText());
                        participant.setPoints(Integer.parseInt(txtPoints.getText()));
                        dispose();
                        
                    }
                });
                okButton.setActionCommand("OK");
                buttonPane.add(okButton);
                getRootPane().setDefaultButton(okButton);
            }
            {
                JButton cancelButton = new JButton("Cancel");
                cancelButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        dispose();
                    }
                });
                cancelButton.setActionCommand("Cancel");
                buttonPane.add(cancelButton);
            }
        }
    }
    
    public Participant getParticipant() {
        return participant;
    }

}
