package voting;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import voting.settings.ResultsNumber;

public class VotingSettings {
    /* FIXME Teams (develop): teams are still not assigned to participants via drop-down lists in the table view.
     * Idea would be to assign teams from the team arrayList to the participants via team name position in drop-down as
     * it basically represents them in the same order as in the arrayList. Also, display the participant team by the team
     * position in team arrayList. For instance, team.get(0) is item #1 (0+1, cause 0 is always null so we can also unassign
     * teams) in the team drop-down list.
     * */
    private VotingResults votingResults = null;
    private JFrame frmVotingParticipants;
    private JMenuBar menuBar;
    private JMenu mnFileMenu;
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
    private boolean resultsFullscreen = false;
    private JMenuItem mntmSave;
    private JMenuItem mntmLoad;
    private JMenu mnSettings;
    private JMenu mnHelp;
    private JMenuItem mntmResultDisplay;
    private ResultsNumber resultsNumberDialog = null;
    private int resultsNumber = 10;
    // FIXME Teams (uncomment): base team management variables
    /*private JMenuItem mntmManageTeams;
    private final Action manageTeamsAction = new ManageTeamsAction();
    private ArrayList<Team> teams = new ArrayList<Team>();*/

    /**
     * Create the application.
     */
    public VotingSettings() {
        this.model = new DefaultTableModel(
                null,
                new String[] {
                    "Name", /*"Team",*/ "Points", "Total"
                }
            ) {
                private static final long serialVersionUID = 8156207888181129045L;
                Class[] columnTypes = new Class[] {
                    String.class, /*String.class,*/ Integer.class, Integer.class
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
                            /*case 1: participants.get(row).getTeam().setName(((String)model.getValueAt(row, column)));
                                    break;*/ // FIXME Team (uncomment): change the team name with the table value
                            case 1: participants.get(row).setPoints(participants.get(row).getPoints() + (Integer)model.getValueAt(row, column));
                                    model.setValueAt(participants.get(row).getPoints(), row, 2);
                                    break;
                            case 2: participants.get(row).setPoints((Integer)model.getValueAt(row, column));
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
        
        // FIXME Teams (uncomment): refresh teams on startup
        /*table.getColumnModel().getColumn(3).setMaxWidth(75);
        table.getColumnModel().getColumn(2).setMaxWidth(75);
         * refreshTeams();*/
        
        table.getColumnModel().getColumn(2).setMaxWidth(75);
        table.getColumnModel().getColumn(1).setMaxWidth(75);
        
        mnFileMenu = new JMenu("File");
        mnFileMenu.setMnemonic('F');
        mnFileMenu.setActionCommand("Menu");
        menuBar.add(mnFileMenu);
        
        //Save to xml menu item
        EditListener mnListener = new EditListener();
        mntmSave = new JMenuItem("Save", KeyEvent.VK_S);
        mntmSave.setMnemonic('S');
        mntmSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Event.CTRL_MASK));
        mntmSave.addActionListener(mnListener);
        mnFileMenu.add(mntmSave);
        
        //Load to xml menu item
        mntmLoad = new JMenuItem("Load", KeyEvent.VK_L);
        mntmLoad.setMnemonic('L');
        mntmLoad.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, Event.CTRL_MASK));
        mntmLoad.addActionListener(mnListener);
        mnFileMenu.add(mntmLoad);
        
        mnSettings = new JMenu("Settings");
        mnSettings.setMnemonic('S');
        menuBar.add(mnSettings);
        
        mntmResultDisplay = new JMenuItem("Result Display", KeyEvent.VK_R);
        mntmResultDisplay.setMnemonic('R');
        mntmResultDisplay.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, Event.CTRL_MASK));
        mntmResultDisplay.addActionListener(mnListener);
        mnSettings.add(mntmResultDisplay);
        
        mnHelp = new JMenu("Help");
        mnHelp.setMnemonic('H');
        menuBar.add(mnHelp);
        
        //FIXME Teams (uncomment): enable team management menu item
        /*mntmManageTeams = new JMenuItem("Manage Teams");
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
        participantDialog.pack();*/
    }
    
    private class EditListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            //System.out.println(e.getActionCommand());
            if (e.getActionCommand().equals("Save"))
                saveParticipants();
            else if (e.getActionCommand().equals("Load")) {
                openParticipants();
            }
            else if (e.getActionCommand().equals("Result Display")) {
                if (resultsNumberDialog == null)
                    resultsNumberDialog = new ResultsNumber(resultsNumber);
                resultsNumber = resultsNumberDialog.showDialog();                
            }
        }
    }
    
    public void addParticipant() {
        addParticipant("Vardas", 0);
    }
    
    public void addParticipant(String name, int points) {
        participants.add(new Participant(name, points));
        model.addRow(new Object[] {
                participants.get(participants.size()-1).getParticipantName(),
                /*participants.get(participants.size()-1).getTeam().getName(), */ // FIXME Teams (uncomment): puts team name into the table
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
    //Cleans participants list and tables model
    public void removeParticipants() {
        for (int i = 0; i < participants.size(); i++) {
            model.removeRow(0);
        }
        participants.clear();
        table.validate();
        table.repaint();
    }
    
    public void showParticipants() {
        if (!participants.isEmpty()) {
            int i = participants.size()-1;
            model.addRow(new Object[]{participants.get(i).getParticipantName(),
                    /*participants.get(i).getTeam().getName(),*/ // FIXME Team (uncomment): get the team name
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
        private static final long serialVersionUID = 2936011880058626644L;
        public AddParticipantAction() {
            putValue(NAME, "Add Participant");
            putValue(SHORT_DESCRIPTION, "Pridėti dalyvį");
        }
        public void actionPerformed(ActionEvent e) {
        }
    }
    private class RemoveSelectedAction extends AbstractAction {
        private static final long serialVersionUID = -5840893490829328162L;
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
            votingResults = new VotingResults(this, participants);
        else
            votingResults.refreshResults();
        votingResults.setResultsNumber(resultsNumber);
        votingResults.setVisible(true);
        saveParticipants(true);
    }
    
    private class DisplayResultsAction extends AbstractAction {
        private static final long serialVersionUID = -4154716509114875596L;
        public DisplayResultsAction() {
            putValue(NAME, "DisplayResultsAction");
            putValue(SHORT_DESCRIPTION, "Display/refresh results");
        }
        public void actionPerformed(ActionEvent e) {
        }
    }

    //FIXME Teams (uncomment): setter & getter, menu item action
    /*public ArrayList<Team> getTeams() {
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
    }*/
    
    // FIXME Teams (uncomment): custom method for cloning team arrayList
    // Java clone api is somewhat buggy and wouldn't do any good here
    /*public static ArrayList<Team> cloneTeams (ArrayList<Team> teams) {
        ArrayList<Team> clonedList = new ArrayList<Team>();
        
        for (Team currentTeam : teams) {
            clonedList.add(new Team(currentTeam));
        }
        return clonedList;
    }*/
    
    // FIXME Teams (uncomment): refresh method, used after team management dialog returns something
    /*public void refreshTeams() {
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
    }*/
    
    public void toggleResultsFullscreen() {
        resultsFullscreen = !resultsFullscreen;
        VotingResults tempPanel = new VotingResults(this, votingResults.getResultsPanel(), resultsFullscreen);;
        votingResults.setVisible(false);
        votingResults.dispose();
        votingResults = tempPanel;
        votingResults.setVisible(true);
    }
    
    private void saveParticipants() {
        saveParticipants(false);
    }
    //TODO by Shabas fix the xml file
    private void saveParticipants(boolean backup) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory
                    .newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // root elements
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("tournament");
            doc.appendChild(rootElement);
            for (int i =0; i< participants.size(); i++) {   
                // participants elements
                Element staff = doc.createElement("participant");
                rootElement.appendChild(staff);
    
                // set attribute to staff element
                staff.setAttribute("id", Integer.toString(i));
    
                // participants name elements
                Element name = doc.createElement("name");
                name.appendChild(doc.createTextNode(participants.get(i).getParticipantName()));
                staff.appendChild(name);
    
                // points elements
                Element points = doc.createElement("points");
                points.appendChild(doc.createTextNode(Integer.toString(participants.get(i).getPoints())));
                staff.appendChild(points);
            }

            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory
                    .newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            
            StreamResult result; //Outputter
            
            if (!backup) {
                JFileChooser fc = new JFileChooser();
                //XML file filter
                FileNameExtensionFilter xmlfilter = new FileNameExtensionFilter(
                        "xml files (*.xml)", "xml");
                fc.addChoosableFileFilter(xmlfilter);
                //Setting dialogs parent and showing it up
                int returnVal = fc.showSaveDialog(frmVotingParticipants); 
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    //CHeking if file extension is xml
                    String fileName = file.getAbsoluteFile().toString();
                    if (!fileName.endsWith(".xml"))
                        file = new File(fileName + ".xml");   
                    
                    result = new StreamResult(file);
                    System.out.println("Opening: " + file.getName() + ".");
                } else 
                    //If file has not been chosen default backup will be saved
                    result = new StreamResult(new File(System.getProperty("user.dir")+"\\backup.xml"));
            }
            else
                result = new StreamResult(new File(System.getProperty("user.dir")+"\\backup.xml"));
            transformer.transform(source, result);
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (TransformerException tfe) {
            tfe.printStackTrace();
        }
    }
    
    private void openParticipants() {
        JFileChooser fc = new JFileChooser();
        // XML file filter
        FileNameExtensionFilter xmlfilter = new FileNameExtensionFilter(
                "xml files (*.xml)", "xml");
        fc.addChoosableFileFilter(xmlfilter);
        // Setting dialogs parent and showing it up
        int returnVal = fc.showOpenDialog(frmVotingParticipants);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            String fileName = file.getAbsoluteFile().toString();
            if (fileName.endsWith(".xml"))
                try {
                    DocumentBuilderFactory dbFactory = DocumentBuilderFactory
                            .newInstance();
                    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                    Document doc = dBuilder.parse(file);
                    doc.getDocumentElement().normalize();
                    if (doc.getDocumentElement().getNodeName() == "tournament");
                    
                    removeParticipants();                
                    
                    NodeList nList = doc.getElementsByTagName("participant");
                    for (int temp = 0; temp < nList.getLength(); temp++) {
                        Node nNode = nList.item(temp);
                        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                           Element eElement = (Element) nNode;
                           System.out.println("Name : " + getTagValue("name", eElement));                       
                           System.out.println("Points: " + getTagValue("points", eElement));
                           //TODO by Shabas implement team reading and creation
                           //For now, we write default "Komanda". Actually, we don't write anything :P
                           addParticipant(getTagValue("name", eElement), Integer.parseInt(getTagValue("points", eElement)));
                        }
                     }
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
    }
    //this method is used by xml reader
    private  String getTagValue(String sTag, Element eElement) {
        NodeList nlList = eElement.getElementsByTagName(sTag).item(0).getChildNodes();
        Node nValue = (Node) nlList.item(0);
        return nValue.getNodeValue();
      }
    
}
