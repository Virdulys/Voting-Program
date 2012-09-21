package voting;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

public class ManageTeams extends JDialog {
    //private static final long serialVersionUID = 4357686840954867862L;
    private ArrayList<Team> teams = null;
    private JFrame parent;
    private final Action addTeamAction = new AddTeamAction();
    private final Action removeSelectedAction = new RemoveSelected();
    private JTable table;
    private DefaultTableModel model = null;
    private boolean teamsChanged = false;

    public ManageTeams(JFrame parent, ArrayList<Team> teams) {
        super(parent, true); // modal = true
        setModal(true);
        this.parent = parent;
        
        if (teams != null)
            this.teams = cloneTeams(teams);
            else
                this.teams = new ArrayList<Team>();
        
        model = new DefaultTableModel (
                    null,
                    new String[] {
                        "Team Name"/*, "Points"*/
                    }
                ) {
                   // private static final long serialVersionUID = 4816355992389917338L;
                    Class[] columnTypes = new Class[] {
                        String.class/*, Integer.class*/
                    };
                    public Class getColumnClass(int columnIndex) {
                        return columnTypes[columnIndex];
                    }
                };
        
        initialize();
    }
        
    public void initialize() {
        
        setTitle("Manage Teams");
        setBounds(100, 100, 464, 362);
        getContentPane().setLayout(new BorderLayout());
        {
            JPanel buttonPane = new JPanel();
            buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
            getContentPane().add(buttonPane, BorderLayout.SOUTH);
            {
                JButton okButton = new JButton("OK");
                okButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        teamsChanged = true;
                        setVisible(false);
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
                        teamsChanged = false;
                        setVisible(false);
                        dispose();
                    }
                });
                cancelButton.setActionCommand("Cancel");
                buttonPane.add(cancelButton);
            }
        }
        {
            JToolBar toolBar = new JToolBar();
            toolBar.setFloatable(false);
            getContentPane().add(toolBar, BorderLayout.NORTH);
            {
                JButton btnAddTeam = toolBar.add(addTeamAction);
                btnAddTeam.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent arg0) {
                        teams.add(new Team("Pavadinimas"));
                        model.addRow(new Object[] {
                                teams.get(teams.size()-1).getName(), 
                                teams.get(teams.size()-1).getPoints()
                                });
                    }
                });
                btnAddTeam.setHorizontalAlignment(SwingConstants.LEFT);
                btnAddTeam.setHorizontalTextPosition(SwingConstants.TRAILING);
                btnAddTeam.setIconTextGap(5);
                btnAddTeam.setIcon(new ImageIcon(ManageTeams.class.getResource("/images/Add.png")));
                btnAddTeam.setFocusable(false);
                btnAddTeam.setMargin(new Insets(0, 10, 0, 5));
                btnAddTeam.setToolTipText("Add new team");
                btnAddTeam.setText("Add Team");
            }
            {
                JButton btnRemoveTeams = toolBar.add(removeSelectedAction);
                btnRemoveTeams.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent arg0) {
                        removeSelected();
                    }
                });
                btnRemoveTeams.setIcon(new ImageIcon(ManageTeams.class.getResource("/images/Delete.png")));
                btnRemoveTeams.setIconTextGap(5);
                btnRemoveTeams.setHorizontalTextPosition(SwingConstants.TRAILING);
                btnRemoveTeams.setHorizontalAlignment(SwingConstants.LEFT);
                btnRemoveTeams.setFocusable(false);
                btnRemoveTeams.setMargin(new Insets(0, 5, 0, 5));
                btnRemoveTeams.setToolTipText("Remove selected teams");
                btnRemoveTeams.setText("Remove Selected");
            }
        }
        {
            JScrollPane scrollPane = new JScrollPane();
            getContentPane().add(scrollPane, BorderLayout.CENTER);
            {
                if (teams.size() > 0) {
                    for (int i = 0; i < teams.size(); i++) {
                        model.addRow(new Object[] {
                                teams.get(i).getName(), 
                                teams.get(i).getPoints()
                                });
                    }
                }
                
                model.addTableModelListener(new TableModelListener() {
                    public void tableChanged(TableModelEvent e) {
                        int row = e.getFirstRow();
                        int column = e.getColumn();
                        
                        if (row >= 0 && column >= 0) {
                            if (model.getValueAt(row, column) != null) {
                                switch(column) {
                                    case 0: teams.get(row).setName((String)model.getValueAt(row, column));
                                            break;
                                    case 1: teams.get(row).setPoints((Integer)model.getValueAt(row, column));
                                            break;
                                    default: break;
                                }
                            }
                        }
                        
                    }
                });
                
                table = new JTable();
                table.setModel(model);
                //table.getColumnModel().getColumn(1).setMaxWidth(75);
                scrollPane.setViewportView(table);
            }
        }
    }

    private class AddTeamAction extends AbstractAction {
        //private static final long serialVersionUID = 2105237458499711389L;
        public AddTeamAction() {
            putValue(NAME, "addTeamAction");
            putValue(SHORT_DESCRIPTION, "Add new team");
        }
        public void actionPerformed(ActionEvent e) {
        }
    }
    private class RemoveSelected extends AbstractAction {
        //private static final long serialVersionUID = 264458781577902869L;
        public RemoveSelected() {
            putValue(NAME, "RemoveSelected");
            putValue(SHORT_DESCRIPTION, "Remove selected teams");
        }
        public void actionPerformed(ActionEvent e) {
        }
    }
    
    public ArrayList<Team> showDialog() {
        setVisible(true);
        
        if (teamsChanged)
            return teams;
        else
            return null;
    }
    
    public static ArrayList<Team> cloneTeams (ArrayList<Team> teams) {
        ArrayList<Team> clonedList = new ArrayList<Team>();
        
        for (Team currentTeam : teams) {
            clonedList.add(new Team(currentTeam));
        }
        return clonedList;
    }
    
    public void removeSelected() {
        int numRows = table.getSelectedRows().length;
        for (int i = 0; i < numRows; i++) {
            Team toRemove = teams.remove(table.getSelectedRow());
            
            for (int j = 0; j < toRemove.getMembers().size(); j++) {
                toRemove.getMembers().get(j).setTeam(null);
            }
            
            toRemove = null;
            model.removeRow(table.getSelectedRow());
        }
    }
}
