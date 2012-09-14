package voting;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

public class VotingSettings {
    
    private VotingResults votingResults;
    
    private JFrame frmVotingParticipants;
    private JMenuBar menuBar;
    private JMenu mnMenu;
    private JMenuItem mntmAddParticipant;
    private Vector<Participant> participants = new Vector<Participant>();
    private JMenuItem mntmRemoveSelectedParticipants;
    private DefaultTableModel model = new DefaultTableModel(
                null,
                new String[] {
                    "Name", "Team", "Points", "Total"
                }
            ) {
                Class[] columnTypes = new Class[] {
                    String.class, String.class, Integer.class, Integer.class
                };
                public Class getColumnClass(int columnIndex) {
                    return columnTypes[columnIndex];
                }
            };
    private JToolBar toolBar;
    private JScrollPane scrollPane;
    private JTable table;
    private final Action addParticipantAction = new AddParticipantAction();
    private JButton btnAddParticipant;
    private final Action removeSelectedAction = new RemoveSelectedAction();
    private JButton btnRemoveParticipants;
    private JButton button;

    /**
     * Create the application.
     */
    public VotingSettings(VotingResults votingResults) {
        //this.votingResults = votingResults;
        //votingResults.getFrame().setVisible(true);
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frmVotingParticipants = new JFrame();
        frmVotingParticipants.setTitle("Voting Participants");
        frmVotingParticipants.setBounds(100, 100, 684, 528);
        frmVotingParticipants.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        model.addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                int row = e.getFirstRow();
                int column = e.getColumn();
                
                if (row >= 0 && column >= 0) {
                    if (model.getValueAt(row, column) != null) {
                        switch(column) {
                            case 0: participants.get(row).setParticipantName((String)model.getValueAt(row, column));
                                    break;
                            case 1: participants.get(row).setTeamName((String)model.getValueAt(row, column));
                                    break;
                            case 2: participants.get(row).setPoints(participants.get(row).getPoints() + (Integer)model.getValueAt(row, column));
                                    model.setValueAt(participants.get(row).getPoints(), row, 3);
                                    break;
                            case 3: participants.get(row).setPoints((Integer)model.getValueAt(row, column));
                                    break;
                            default: break;
                        }
                    }
                }
                
            }
        });
        
        menuBar = new JMenuBar();
        frmVotingParticipants.setJMenuBar(menuBar);
        
        votingResults = new VotingResults();
        votingResults.setParticipants(participants);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                votingResults.createAndShowGUI();
            }
        });
        
        toolBar = new JToolBar();
        toolBar.setAlignmentX(Component.LEFT_ALIGNMENT);
        toolBar.setAlignmentY(Component.CENTER_ALIGNMENT);
        toolBar.setFloatable(false);
        toolBar.setRollover(true);
        frmVotingParticipants.getContentPane().add(toolBar, BorderLayout.NORTH);
        
        btnAddParticipant = toolBar.add(addParticipantAction);
        btnAddParticipant.setMargin(new Insets(0, 5, 0, 5));
        btnAddParticipant.setFocusPainted(false);
        btnAddParticipant.setText("Add Participant");
        
        btnRemoveParticipants = toolBar.add(removeSelectedAction);
        btnRemoveParticipants.setMargin(new Insets(0, 5, 0, 5));
        btnRemoveParticipants.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                removeSelected();
            }
        });
        btnRemoveParticipants.setActionCommand("Remove Selected");
        btnRemoveParticipants.setFocusPainted(false);
        btnRemoveParticipants.setText("Remove Selected");
        
        btnAddParticipant.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                addParticipant();
            }
        });
        
        scrollPane = new JScrollPane();
        frmVotingParticipants.getContentPane().add(scrollPane, BorderLayout.CENTER);
        
        table = new JTable(model);
        scrollPane.setViewportView(table);
        
        table.getColumnModel().getColumn(3).setMaxWidth(75);
        table.getColumnModel().getColumn(2).setMaxWidth(75);
        
        mnMenu = new JMenu("Menu");
        mnMenu.setActionCommand("Menu");
        menuBar.add(mnMenu);
        
        mntmAddParticipant = new JMenuItem("Add participant");
        final AddParticipant participantDialog = new AddParticipant(frmVotingParticipants, true);
        participantDialog.pack();
        mntmAddParticipant.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                addParticipant();
            }
        });
        mnMenu.add(mntmAddParticipant);
        
        mntmRemoveSelectedParticipants = new JMenuItem("Remove Selected Participants");
        mntmRemoveSelectedParticipants.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                removeSelected();
            }
        });
        mnMenu.add(mntmRemoveSelectedParticipants);
    }
    
    public void addParticipant() {
        participants.add(new Participant("Vardas", "Komanda", 0));
        model.addRow(new Object[] {
                participants.lastElement().getParticipantName(), 
                participants.lastElement().getTeamName(), 
                null, 
                participants.lastElement().getPoints()
                });
    }
    
    public void removeSelected() {
        int numRows = table.getSelectedRows().length;
        for (int i = 0; i < numRows; i++) {
            participants.remove(table.getSelectedRow());
            model.removeRow(table.getSelectedRow());
        }
    }
    
    public void showParticipants() {
        if (!participants.isEmpty()) {
            int i = participants.size()-1;
            model.addRow(new Object[]{participants.get(i).getParticipantName(),
                    participants.get(i).getTeamName(),
                    null,
                    participants.get(i).getPoints()});
        }
    }

    public JFrame getFrame() {
        return frmVotingParticipants;
    }

    public void setFrame(JFrame frame) {
        this.frmVotingParticipants = frame;
    }
    
    public VotingResults getVotingResults() {
        return votingResults;
    }
    
    public void setVotingResults(VotingResults votingResults) {
        this.votingResults = votingResults;
    }
    private class AddParticipantAction extends AbstractAction {
        public AddParticipantAction() {
            putValue(NAME, "Add Participant");
            putValue(SHORT_DESCRIPTION, "Pridėti dalyvį");
        }
        public void actionPerformed(ActionEvent e) {
        }
    }
    private class RemoveSelectedAction extends AbstractAction {
        public RemoveSelectedAction() {
            putValue(NAME, "Remove Selected");
            putValue(SHORT_DESCRIPTION, "Ištrinti pasirinktus dalyvius");
        }
        public void actionPerformed(ActionEvent e) {
        }
    }
    
}
