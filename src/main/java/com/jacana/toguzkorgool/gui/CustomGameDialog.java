package com.jacana.toguzkorgool.gui;

import com.jacana.toguzkorgool.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;


/**
 * The CustomGameDialog GUI allows the player to "set up" the board state
 * to their liking during any point of the game, using the modal this class
 * creates.
 * <p>
 * Upon clicking the "Apply" button, this class send the input data to the
 * back-end Board.
 */
public class CustomGameDialog extends JDialog {
    private static CustomGameDialog instance;
    private JPanel contentPane;
    private Map<String, Component> componentMap = new HashMap<>();
    private static List<String> errors;
    
    private CustomGameDialog() {
        setResizable(false);
        setModal(true);
        setTitle("Custom Game");
        setName("CustomGameDialog");
        
        contentPane = new JPanel();
        setContentPane(contentPane);
        
        errors = new ArrayList<>();
        
        setUpComponents();
        
        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });
        
        // call onCancel() on ESCAPE
        contentPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(
                "released ESCAPE"), "closeWindow");
        contentPane.getActionMap().put("closeWindow", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });
    }
    
    static CustomGameDialog getCustomGameDialogInstance() {
        if (instance == null) {
            instance = new CustomGameDialog();
        }
        return instance;
    }
    
    static void showCustomGameDialog() {
        CustomGameDialog dialog = getCustomGameDialogInstance();
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }
    
    public static void main(String[] args) {
        showCustomGameDialog();
        System.exit(0);
    }
    
    private static String validateBoard(final Board board) {
        if (board == null) return "Board is null";
        String player1Validation = validateUser(board.getPlayer(0));
        if (player1Validation != null) return player1Validation;
        String player2Validation = validateUser(board.getPlayer(1));
        if (player2Validation != null) return player2Validation;
        return null;
    }
    
    private static String validateUser(final Player player) {
        String name = "player " + (player.getId() + 1);
        int tuzId = -1;
        for (int i = 0; i < player.getHoleCount(); i++) {
            Hole hole = player.getHole(i);
            if (hole.getKorgools() < 0) {
                return "Hole " + (i + 1) + " cannot have less than 0 korgools for " + name;
            } else if (hole.getKorgools() > 162) {
                return "Hole " + (i + 1) + " cannot have more than 162 korgools for " + name;
            }
            if (hole.isTuz()) {
                if (tuzId != -1)
                    return "More than one tuz found for " + name;
                if (i == 9 - 1) return "Hole 9 cannot be a tuz for " + name;
                tuzId = (i);
            }
        }
        if (player.getKazan().getKorgools() < 0) {
            return name + " cannot have a kazan with less than 0 korgools.";
        } else if (player.getKazan().getKorgools() > 162) {
            return name + " cannot have a kazan with more than 162 korgools.";
        }
        return null;
    }
    
    public static boolean areErrorsPresent() {
        return errors.size() > 0;
    }
    
    public static List<String> getErrors() {
        return errors;
    }
    
    private void setUpComponents() {
        setJMenuBar(constructAndGetMenuBar());
        
        contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contentPane.setLayout(new BorderLayout());
        
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(0, 2, 5, 0));
        
        JPanel player1Panel = constructAndGetSidePanel(0);
        JPanel player2Panel = constructAndGetSidePanel(1);
        
        inputPanel.add(player1Panel);
        inputPanel.add(player2Panel);
        
        JPanel controlPanel = constructAndGetControlPanel();
        
        contentPane.add(inputPanel, BorderLayout.CENTER);
        contentPane.add(controlPanel, BorderLayout.PAGE_END);
    }
    
    private JPanel constructAndGetSidePanel(int playerId) {
        final String playerName = "Player " + (playerId + 1);
        
        int kazanKorgoolCount = GameController.getBoard().getKazanCount(playerId);
        int tuzIndex = GameController.getBoard().getTuzIndex(playerId);
        
        //make the panel a vertical box layout
        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
        
        // add name label to the top
        JPanel sideLabelPanel = new JPanel();
        addAndHorizontalCenterInPanel(new JLabel(playerName), sideLabelPanel);
        
        sidePanel.add(sideLabelPanel);
        addHorizontalSeparator(sidePanel);
        
        //HOLE SPINNERS--------------------------------------------------
        // create 9 numerical spinners with names and labels, each within a
        // flow layout
        for (int i = 0; i < 9; i++) {
            JPanel holeSpinnerPanel = new JPanel();
            
            SpinnerNumberModel spinnerHoleModel = new SpinnerNumberModel(0, 0, 161, 1);
            JSpinner holeSpinner = new JSpinner(spinnerHoleModel);
            holeSpinner.setName("Player" + playerId + "Hole" + i);
            holeSpinner.setValue(GameController.getBoard().getHoleKorgoolCount(playerId, i));
            
            JLabel newSpinnerLabel = new JLabel("Hole " + (i + 1) + ":");
            
            //add the label and the spinner to the panel
            holeSpinnerPanel.add(newSpinnerLabel);
            holeSpinnerPanel.add(Box.createRigidArea(new Dimension(10, 0)));
            holeSpinnerPanel.add(holeSpinner);
            addToComponentMap(holeSpinner);
            
            sidePanel.add(holeSpinnerPanel);
        }
        addHorizontalSeparator(sidePanel);
        
        //KAZAN SPINNER--------------------------------------------------
        
        JPanel kazanPanel = new JPanel();
        
        SpinnerNumberModel spinnerKazanModel = new SpinnerNumberModel(kazanKorgoolCount, 0, 81, 1);
        JSpinner kazanSpinner = new JSpinner(spinnerKazanModel);
        kazanSpinner.setName("Player" + playerId + "Kazan");
        JLabel kazanLabel = new JLabel(playerName + " Kazan:");
        
        kazanPanel.add(kazanLabel);
        kazanPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        kazanPanel.add(kazanSpinner);
        addToComponentMap(kazanSpinner);
        
        sidePanel.add(kazanPanel);
        addHorizontalSeparator(sidePanel);
        
        //TUZ COMBO BOX--------------------------------------------------
        
        JPanel tuzPanel = new JPanel();
        
        JLabel tuzLabel = new JLabel(playerName + "'s Tuz:");
        String[] tuzOptions = {"None", "1st Hole", "2nd Hole", "3rd Hole",
                "4th Hole", "5th Hole", "6th Hole", "7th Hole", "8th Hole"};
        JComboBox<String> tuzComboBox = new JComboBox<>(tuzOptions);
        tuzComboBox.setName("Player" + playerId + "Tuz");
        
        tuzComboBox.setSelectedIndex(tuzIndex + 1);
        
        //add to the side panel.
        tuzPanel.add(tuzLabel);
        tuzPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        tuzPanel.add(tuzComboBox);
        addToComponentMap(tuzComboBox);
        
        sidePanel.add(tuzPanel);
        
        //done
        return sidePanel;
    }
    
    private JMenuBar constructAndGetMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        // File menu
        JMenu fileMenu = new JMenu("File");
        
        JMenuItem exportMenuItem = new JMenuItem("Export Board", KeyEvent.VK_E);
        exportMenuItem.getAccessibleContext().setAccessibleDescription("Export board state to a file");
        fileMenu.add(exportMenuItem);
        
        JMenuItem importMenuItem = new JMenuItem("Import Board", KeyEvent.VK_I);
        importMenuItem.getAccessibleContext().setAccessibleDescription("Import board state from a file");
        fileMenu.add(importMenuItem);
        
        exportMenuItem.addActionListener(e ->
                EventQueue.invokeLater(() -> {
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setDialogTitle("Choose a location to save to");
                    fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                    int returnVal = fileChooser.showSaveDialog(this);
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        try {
                            File selectedFile = fileChooser.getSelectedFile();
                            if (!selectedFile.getName().toLowerCase().endsWith(".json")) {
                                selectedFile = new File(selectedFile.getParentFile(), selectedFile.getName() + ".json");
                            }
                            if (selectedFile.exists()) {
                                int response = JOptionPane.showConfirmDialog(this, "A file with that name already exists. Do you want to replace the file?");
                                if (response != JOptionPane.YES_OPTION) {
                                    return;
                                } else {
                                    try {
                                        if (!selectedFile.delete()) {
                                            JOptionPane.showMessageDialog(this, "Failed to delete the previous file.");
                                            return;
                                        }
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                        JOptionPane.showMessageDialog(this, "Failed to delete the previous file.");
                                        return;
                                    }
                                }
                            }
                            
                            Board customBoard = new Board();
                            for (Player player : customBoard.getPlayers()) {
                                saveUser(player);
                            }
                            String serializedBoard = Utilities.getGson().toJson(customBoard);
                            try (FileWriter writer = new FileWriter(selectedFile)) {
                                writer.write(serializedBoard);
                            }
                            JOptionPane.showMessageDialog(this, "Successfully saved custom board to '" + selectedFile.getName() + "'.");
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(this, "An error occurred!");
                        }
                    }
                })
        );
        importMenuItem.addActionListener(e -> {
            EventQueue.invokeLater(() -> {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Choose a Toguz Korgool board JSON file.");
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                int returnVal = fileChooser.showOpenDialog(this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    try {
                        File selectedFile = fileChooser.getSelectedFile();
                        if (!selectedFile.getName().toLowerCase().endsWith(".json")) {
                            selectedFile = new File(selectedFile.getParentFile(), selectedFile.getName() + ".json");
                        }
                        StringBuilder sbFile = new StringBuilder();
                        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(selectedFile))) {
                            String line;
                            while ((line = bufferedReader.readLine()) != null) {
                                sbFile.append(line).append(System.lineSeparator());
                            }
                        }
                        try {
                            Board deserializedBoard = Utilities.getGson().fromJson(sbFile.toString(), Board.class);
                            String error = validateBoard(deserializedBoard);
                            if (error == null) {
                                loadUser(deserializedBoard.getPlayer(0));
                                loadUser(deserializedBoard.getPlayer(1));
                                JOptionPane.showMessageDialog(this, "Loaded in '" + selectedFile.getName() + "'!");
                            } else {
                                JOptionPane.showMessageDialog(this, "Cannot load in '" + selectedFile.getName() + "': " + error);
                            }
                        } catch (IllegalArgumentException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(this, "An error occurred: " + ex.getLocalizedMessage());
                        }
                    } catch (FileNotFoundException ex) {
                        JOptionPane.showMessageDialog(this, "Error: " + ex.getLocalizedMessage());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(this, "An error occurred!");
                    }
                }
            });
        });
        
        menuBar.add(fileMenu);
        
        return menuBar;
    }
    
    private JPanel constructAndGetControlPanel() {
        //make new panel
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.X_AXIS));
        
        //add top spacer
        controlPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        
        //make new buttons
        JButton buttonApply = new JButton("Apply");
        buttonApply.setName("ApplyButton");
        
        JButton buttonCancel = new JButton("Cancel");
        buttonCancel.setName("CancelButton");
        
        getRootPane().setDefaultButton(buttonCancel);
        
        // set button actions.
        buttonApply.addActionListener(e -> onApply());
        buttonCancel.addActionListener(e -> onCancel());
        
        //add the components
        controlPanel.add(Box.createHorizontalGlue());
        controlPanel.add(buttonApply);
        controlPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        controlPanel.add(buttonCancel);
        
        return controlPanel;
    }
    
    /*Data methods-----------------------------------------------------*/
    //TODO Validate data, else open an error message.
    private int checkInputForErrors() {
        //useful definitions
        int playerCount = GameController.getBoard().getPlayerCount();
        //IMPORTANT sometimes counts korgools on one side to be 82...
        //collect information
        int totalKorgoolCount = 0;
        int[] holeKorgoolPerSideCount = new int[playerCount];
        int[] tubIndexes = new int[playerCount];
        for (int playerId = 0; playerId < playerCount; playerId++) {
            totalKorgoolCount +=
                    (int) ((JSpinner) getComponentByName("Player" + playerId + "Kazan")).getValue();
            tubIndexes[playerId] = ((JComboBox) getComponentByName("Player" + playerId + "Tuz")).getSelectedIndex() - 1;
            for (int i = 0; i < Constants.CONSTRAINT_HOLES_PER_PLAYER; i++) {
                holeKorgoolPerSideCount[playerId] +=
                        (int) ((JSpinner) getComponentByName("Player" + playerId + "Hole" + i)).getValue();
            }
        }
        int holeKorgoolCount = IntStream.of(holeKorgoolPerSideCount).sum();
        totalKorgoolCount += holeKorgoolCount;
        
        
        //CHECK VIOLATIONS
        //Validation 1 & 2: the Sum of korgools in holes of a side must not be greater than 161 and not less than 1
        boolean sideHoleMaxViolation = false;
        boolean sideHoleMinViolation = false;
        for (int sideCount : holeKorgoolPerSideCount) {
            if (sideCount > Constants.CONSTRAINT_MAX_KORGOOLS_PER_HOLES)
                sideHoleMaxViolation = true;
            if (sideCount < Constants.CONSTRAINT_MIN_KORGOOLS_PER_HOLES)
                sideHoleMinViolation = true;
        }
        if (sideHoleMaxViolation) {
            errors.add(Constants.ERROR_CUSTOM_GUI_CONSTRAINT_MAX_TOTAL_KORGOOLS_PER_HOLES_VIOLATION);
        }
        if (sideHoleMinViolation) {
            errors.add(Constants.ERROR_CUSTOM_GUI_CONSTRAINT_MIN_TOTAL_KORGOOLS_PER_HOLES_VIOLATION);
        }
        
        //Validation 3: the Sum of korgools on the board must be 162
        if (totalKorgoolCount != Constants.CONSTRAINT_TOTAL_KORGOOLS) {
            System.out.println("Total Korgools is = " + totalKorgoolCount);
            errors.add(Constants.ERROR_CUSTOM_GUI_CONSTRAINT_TOTAL_KORGOOLS_VIOLATION);
        }
        
        //Validation 4:Tuz can be the same hole.
        boolean duplicate = false;
        for (int i = 0; i < tubIndexes.length && !duplicate; i++) {
            for (int j = i + 1; j < tubIndexes.length && !duplicate; j++) {
                if (tubIndexes[i] == tubIndexes[j] && tubIndexes[i] != -1) {
                    duplicate = true;
                }
            }
        }
        if (duplicate) {
            errors.add(Constants.ERROR_CUSTOM_GUI_CONSTRAINT_TUZ_IDENTITY_VIOLATION);
        }
        
        //see if any errors were found
        return errors.size();
    }
    
    private void clearErrorList() {
        errors.clear();
    }
    
    private String makeErrorString() {
        StringBuilder sb = new StringBuilder();
        String lineStart = "· ";
        String lineEnd = "\n";
        for (String errorMessage : errors) {
            sb.append(lineStart).append(errorMessage).append(lineEnd);
        }
        return sb.toString();
    }
    
    private void sendInputDataToBackEnd() {
        Board board = GameController.getBoard();
        
        // For each player
        
        // Reset the kazan
        for (int playerId = 0; playerId < board.getPlayerCount(); playerId++) {
            board.setKazanCount(playerId, 0);
        }
        
        // Update holes and kazan.
        for (int playerId = 0; playerId < board.getPlayerCount(); playerId++) {
            // Set number of korgools in hole
            for (int i = 0; i < 9; i++) {
                int holeCount = (int) ((JSpinner) getComponentByName("Player" + playerId + "Hole" + i)).getValue();
                board.setHoleCount(playerId, i, holeCount);
            }
            
            // Set number of korgools in kazan
            int kazanCount = board.getKazanCount(playerId);
            board.setKazanCount(playerId, kazanCount + (int) ((JSpinner) getComponentByName("Player" + playerId + "Kazan")).getValue());
            
            // Set tuz index
            int tuzIndex = ((JComboBox) getComponentByName("Player" + playerId + "Tuz")).getSelectedIndex() - 1;
            if (tuzIndex >= 0) {
                board.setTuz(playerId, tuzIndex);
                
                int holeCount = board.getHoleKorgoolCount(playerId, tuzIndex);
                board.setHoleCount(playerId, tuzIndex, 0);
                
                int opponentId = board.getOpponentOf(playerId).getId();
                int opponentKazanCount = board.getKazanCount(opponentId);
                board.setKazanCount(opponentId, opponentKazanCount + holeCount);
            }
        }
        
        for (int playerId = 0; playerId < board.getPlayerCount(); playerId++) {
            if (board.playerHasWon(playerId)) {
                GameController.getInstance().onWin(playerId);
                break;
            }
        }
    }
    
    //TODO Move helper methods into a static helper methods class
    /*Helper methods---------------------------------------------------*/
    private void addAndHorizontalCenterInPanel(JComponent component, JPanel panel) {
        panel.add(Box.createHorizontalGlue());
        panel.add(component);
        panel.add(Box.createHorizontalGlue());
    }
    
    private void addHorizontalSeparator(JPanel panel) {
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(new JSeparator(SwingConstants.HORIZONTAL));
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
    }
    
    /* methods adapted from
        https://stackoverflow.com/questions/4958600/get-a-swing-component-by-name */
    private void addToComponentMap(Component component) {
        componentMap.put(component.getName(), component);
    }
    
    private Component getComponentByName(String name) {
        return componentMap.getOrDefault(name, null);
    }
    
    /*Action Methods*/
    private void onApply() {
        if (checkInputForErrors() > 0) /*data invalid*/ {
            // make a JOptionPanel with errors in the errors list
            String errorString = makeErrorString();
            JOptionPane.showMessageDialog(this, errorString);
            clearErrorList();
        } else {
            sendInputDataToBackEnd();
            GameController.updateGUI();
            dispose();
        }
    }
    
    private void onCancel() {
        dispose();
    }
    
    /* Static helper methods */
    
    /* Loading and saving methods */
    private void loadUser(final Player player) {
        int tuzIndex = -1;
        for (int i = 0; i < player.getHoleCount(); i++) {
            Hole hole = player.getHole(i);
            JSpinner holeSpinner = (JSpinner) componentMap.get("Player" + player.getId() + "Hole" + i);
            holeSpinner.setValue(hole.getKorgools());
            if (hole.isTuz()) {
                tuzIndex = i;
            }
        }
        JComboBox<String> tuzComboBox = (JComboBox<String>) componentMap.get("Player" + player.getId() + "Tuz");
        tuzComboBox.setSelectedIndex(tuzIndex + 1);
        
        JSpinner kazanSpinner = (JSpinner) componentMap.get("Player" + player.getId() + "Kazan");
        kazanSpinner.setValue(player.getKazanCount());
    }
    
    private void saveUser(final Player player) {
        for (int i = 0; i < player.getHoleCount(); i++) {
            JSpinner holeSpinner = (JSpinner) componentMap.get("Player" + player.getId() + "Hole" + i);
            if (holeSpinner == null) return;
            player.clearHole(i);
            player.addToHole(i, (Integer) holeSpinner.getValue());
        }
        
        player.setTuz(-1);
        JComboBox<String> tuzComboBox = (JComboBox<String>) componentMap.get("Player" + player.getId() + "Tuz");
        if (tuzComboBox != null) {
            if (tuzComboBox.getSelectedIndex() > 0) {
                player.setTuz(Math.max(Math.min(tuzComboBox.getSelectedIndex() - 1, player.getHoleCount() - 1), 0));
            }
        }
        
        JSpinner kazanSpinner = (JSpinner) componentMap.get("Player" + player.getId() + "Kazan");
        if (kazanSpinner != null) {
            player.setKazanCount(Math.max((Integer) kazanSpinner.getValue(), 0));
        }
    }
}
