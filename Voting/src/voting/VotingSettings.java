package voting;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

public class VotingSettings {
    
    private VotingResults votingResults = null;
    private JFrame frmVotingParticipants;
    private JMenuBar menuBar;
    private JMenu mnMenu;
    private ArrayList<Team> teams = new ArrayList<Team>();
    private ArrayList<Participant> participants = new ArrayList<Participant>();
    private DefaultTableModel model = null;
    private JToolBar toolBar;
    private JScrollPane scrollPane;
    private JTable table;
    private final Action addParticipantAction = new AddParticipantAction();
    private JButton btnAddParticipant;
    private final Action removeSelectedAction = new RemoveSelectedAction();
    private JButton btnRemoveParticipants;
    private final Action displayResultsAction = new DisplayResultsAction();
    private JButton btnDisplayResults;
    private JSeparator separator;
    private JMenuItem mntmManageTeams;
    private final Action manageTeamsAction = new ManageTeamsAction();

    /**
     * Create the application.
     */
    public VotingSettings() {
        this.model = new DefaultTableModel(
                null,
                new String[] {
                    "Name", "Team", "Points", "Total"
                }
            ) {
                //private static final long serialVersionUID = 8156207888181129045L;
                Class[] columnTypes = new Class[] {
                    String.class, String.class, Integer.class, Integer.class
                };
                public Class getColumnClass(int columnIndex) {
                    return columnTypes[columnIndex];
                }
            };
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
        
        toolBar = new JToolBar();
        toolBar.setFocusable(false);
        toolBar.setAlignmentX(Component.LEFT_ALIGNMENT);
        toolBar.setAlignmentY(Component.CENTER_ALIGNMENT);
        toolBar.setFloatable(false);
        toolBar.setRollover(true);
        frmVotingParticipants.getContentPane().add(toolBar, BorderLayout.NORTH);
        
        btnDisplayResults = toolBar.add(displayResultsAction);
        btnDisplayResults.setHorizontalTextPosition(SwingConstants.TRAILING);
        btnDisplayResults.setHorizontalAlignment(SwingConstants.LEFT);
        btnDisplayResults.setIconTextGap(5);
        btnDisplayResults.setIcon(new ImageIcon(VotingSettings.class.getResource("/icons/Refresh.png")));
        btnDisplayResults.setBorder(UIManager.getBorder("Button.border"));
        btnDisplayResults.setFocusable(false);
        btnDisplayResults.setMargin(new Insets(0, 5, 0, 10));
        btnDisplayResults.setToolTipText("Display results in a new window");
        btnDisplayResults.setText("Display Results");
        btnDisplayResults.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                displayResults();
            }
        });
        
        separator = new JSeparator();
        separator.setMaximumSize(new Dimension(2, 32767));
        separator.setOrientation(SwingConstants.VERTICAL);
        toolBar.add(separator);
        
        btnAddParticipant = toolBar.add(addParticipantAction);
        btnAddParticipant.setIconTextGap(5);
        btnAddParticipant.setHorizontalTextPosition(SwingConstants.TRAILING);
        btnAddParticipant.setHorizontalAlignment(SwingConstants.LEFT);
        btnAddParticipant.setIcon(new ImageIcon(VotingSettings.class.getResource("/icons/Add.png")));
        btnAddParticipant.setFocusable(false);
        btnAddParticipant.setToolTipText("Add new participant");
        btnAddParticipant.setMargin(new Insets(0, 10, 0, 5));
        btnAddParticipant.setFocusPainted(false);
        btnAddParticipant.setText("Add Participant");
        
        btnRemoveParticipants = toolBar.add(removeSelectedAction);
        btnRemoveParticipants.setIcon(new ImageIcon(VotingSettings.class.getResource("/icons/Delete.png")));
        btnRemoveParticipants.setHorizontalAlignment(SwingConstants.LEFT);
        btnRemoveParticipants.setIconTextGap(5);
        btnRemoveParticipants.setHorizontalTextPosition(SwingConstants.TRAILING);
        btnRemoveParticipants.setFocusable(false);
        btnRemoveParticipants.setToolTipText("Remove selected participants");
        btnRemoveParticipants.setMargin(new Insets(0, 5, 0, 5));
        btnRemoveParticipants.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                removeSelected();
            }
        });
        //btnRemoveParticipants.setActionCommand("Remove Selected");
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
        
        //bottomToolBar.add(Box.createHorizontalGlue());
        
        
        // Table column setup
        table.getColumnModel().getColumn(3).setMaxWidth(75);
        table.getColumnModel().getColumn(2).setMaxWidth(75);
        refreshTeams();
        
        mnMenu = new JMenu("Menu");
        mnMenu.setActionCommand("Menu");
        menuBar.add(mnMenu);
        
        mntmManageTeams = new JMenuItem("Manage Teams");
        mntmManageTeams.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ManageTeams manageTeams = new ManageTeams(null, teams);
                ArrayList<Team> tempTeams = manageTeams.showDialog();

                if (tempTeams != null)
                    teams = cloneTeams(tempTeams);
                
                refreshTeams();
            }
        });
        mntmManageTeams.setAction(manageTeamsAction);
        mnMenu.add(mntmManageTeams);
        final AddParticipant participantDialog = new AddParticipant(frmVotingParticipants, true);
        participantDialog.pack();
    }
    
    public void addParticipant() {
        participants.add(new Participant("Vardas", "Komanda", 0));
        model.addRow(new Object[] {
                participants.get(participants.size()-1).getParticipantName(), 
                participants.get(participants.size()-1).getTeamName(), 
                null, 
                participants.get(participants.size()-1).getPoints()
                });
        table.validate();
        table.repaint();
    }
    
    public void removeSelected() {
        int numRows = table.getSelectedRows().length;
        for (int i = 0; i < numRows; i++) {
            participants.remove(table.getSelectedRow());
            model.removeRow(table.getSelectedRow());
        }
        table.validate();
        table.repaint();
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
    
    // Use this method in refresh button
    private void displayResults() {
        if (votingResults == null)
            votingResults = new VotingResults(participants);
        else
            votingResults.refreshResults();
        votingResults.setVisible(true);
    }
    
    private class DisplayResultsAction extends AbstractAction {
        public DisplayResultsAction() {
            putValue(NAME, "DisplayResultsAction");
            putValue(SHORT_DESCRIPTION, "Display/refresh results");
        }
        public void actionPerformed(ActionEvent e) {
        }
    }

    public ArrayList<Team> getTeams() {
        return teams;
    }

    public void setTeams(ArrayList<Team> teams) {
        this.teams = teams;
    }
    private class ManageTeamsAction extends AbstractAction {
        public ManageTeamsAction() {
            putValue(NAME, "Manage Teams");
            putValue(SHORT_DESCRIPTION, "Manage teams");
        }
        public void actionPerformed(ActionEvent e) {
        }
    }
    
    public static ArrayList<Team> cloneTeams (ArrayList<Team> teams) {
        ArrayList<Team> clonedList = new ArrayList<Team>();
        
        for (Team currentTeam : teams) {
            clonedList.add(new Team(currentTeam));
        }
        return clonedList;
    }
    
    public void refreshTeams() {
        TableColumn col = table.getColumnModel().getColumn(1);
        Vector<String> values = new Vector<String>();
        values.add(null);
        for (int i = 0; i < teams.size(); i++)
            values.add(teams.get(i).getName());
        
        col.setCellEditor(new CellComboBoxEditor(values));
        CellComboBoxRenderer comboBox = new CellComboBoxRenderer(values);
        comboBox.setForeground(Color.WHITE);
        col.setCellRenderer(new CellComboBoxRenderer(values));
        
        table.validate();
        table.repaint();
    }
}
