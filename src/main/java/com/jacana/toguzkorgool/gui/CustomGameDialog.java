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

    private Map<String, Component> componentMap = new HashMap<>();
    private JPanel contentPane;

    private JFileChooser importFileChooser = null;
    private JFileChooser exportFileChooser = null;

    private CustomGameDialog() {
        setResizable(false);
        setModal(true);
        setTitle("Custom Game");
        setName("CustomGameDialog");

        contentPane = new JPanel();
        setContentPane(contentPane);

        setUpComponents();

        // Call onCancel() when cross is clicked
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // Call onCancel() on ESCAPE
        contentPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("released ESCAPE"), "closeWindow");
        contentPane.getActionMap().put("closeWindow", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });
    }

    public static CustomGameDialog getInstance() {
        if (instance == null) {
            instance = new CustomGameDialog();
        }
        return instance;
    }

    public static void destroyInstance() {
        if (instance != null) {
            if (instance.isVisible()) instance.dispose();
            instance = null;
        }
    }

    public static void showCustomGameDialog() {
        CustomGameDialog dialog = getInstance();
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    public Map<String, Component> getComponentMap() {
        return componentMap;
    }

    @Override
    public void dispose() {
        instance = null;
        super.dispose();
    }

    private void setUpComponents() {
        setJMenuBar(constructAndGetMenuBar());

        contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contentPane.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(0, GameController.getBoard().getPlayerCount(), 5, 0));
        for (Player player : GameController.getBoard().getPlayers()) {
            inputPanel.add(constructAndGetSidePanel(player.getId()));
        }

        JPanel controlPanel = constructAndGetControlPanel();

        contentPane.add(inputPanel, BorderLayout.CENTER);
        contentPane.add(controlPanel, BorderLayout.PAGE_END);
    }

    public JPanel constructAndGetSidePanel(int playerId) {
        final String playerName = "Player " + (playerId + 1);

        int kazanKorgoolCount = GameController.getBoard().getKorgoolsInKazan(playerId);
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
        // Create numerical spinners with names and labels, each within a
        // Flow layout
        for (int i = 0; i < Constants.CONSTRAINT_HOLES_PER_PLAYER; i++) {
            JPanel holeSpinnerPanel = new JPanel();

            SpinnerNumberModel spinnerHoleModel = new SpinnerNumberModel(0, 0, 161, 1);
            JSpinner holeSpinner = new JSpinner(spinnerHoleModel);
            holeSpinner.setName("Player" + playerId + "Hole" + i);
            holeSpinner.setValue(GameController.getBoard().getKorgoolsInHole(playerId, i));

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

        String[] tuzOptions = new String[Constants.CONSTRAINT_HOLES_PER_PLAYER];
        tuzOptions[0] = "None";
        for (int i = 1; i < tuzOptions.length; i++)
            tuzOptions[i] = "Hole " + i;

        JComboBox<String> tuzComboBox = new JComboBox<>(tuzOptions);
        tuzComboBox.setName("Player" + playerId + "Tuz");

        tuzComboBox.setSelectedIndex(tuzIndex + 1);

        // Add to the side panel
        tuzPanel.add(tuzLabel);
        tuzPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        tuzPanel.add(tuzComboBox);
        addToComponentMap(tuzComboBox);

        sidePanel.add(tuzPanel);

        return sidePanel;
    }

    private JMenuBar constructAndGetMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // File menu
        JMenu fileMenu = new JMenu("File");
        fileMenu.setName("fileMenu");

        JMenuItem exportMenuItem = new JMenuItem("Export Board", KeyEvent.VK_E);
        exportMenuItem.setName("exportMenuItem");
        exportMenuItem.getAccessibleContext().setAccessibleDescription("Export board state to a file");
        fileMenu.add(exportMenuItem);

        JMenuItem importMenuItem = new JMenuItem("Import Board", KeyEvent.VK_I);
        importMenuItem.setName("importMenuItem");
        importMenuItem.getAccessibleContext().setAccessibleDescription("Import board state from a file");
        fileMenu.add(importMenuItem);

        exportMenuItem.addActionListener(e ->
                EventQueue.invokeLater(() -> {
                    exportFileChooser = new JFileChooser();
                    exportFileChooser.setDialogTitle("Choose a location to save to");
                    exportFileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
                    exportFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                    int returnVal = exportFileChooser.showSaveDialog(this);
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        try {
                            File selectedFile = exportFileChooser.getSelectedFile();
                            if (!selectedFile.getName().toLowerCase().endsWith(".json")) {
                                selectedFile = new File(selectedFile.getParentFile(), selectedFile.getName() + ".json");
                            }
                            if (selectedFile.exists()) {
                                int response = JOptionPane.showConfirmDialog(this, "A file with that name already exists. Do you want to replace the file?");
                                if (response != JOptionPane.YES_OPTION) {
                                    exportFileChooser = null;
                                    return;
                                } else {
                                    boolean deleted = true;
                                    try {
                                        deleted = selectedFile.delete();
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                        deleted = false;
                                    }
                                    if (!deleted) {
                                        exportFileChooser = null;
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
                            exportFileChooser = null;
                            JOptionPane.showMessageDialog(this, "Successfully saved custom board to '" + selectedFile.getName() + "'.");
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            exportFileChooser = null;
                            JOptionPane.showMessageDialog(this, "An error occurred!");
                        }
                    } else {
                        exportFileChooser = null;
                    }
                })
        );
        importMenuItem.addActionListener(e -> {
            EventQueue.invokeLater(() -> {
                importFileChooser = new JFileChooser();
                importFileChooser.setDialogTitle("Choose a Toguz Korgool board JSON file.");
                importFileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
                importFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                int returnVal = importFileChooser.showOpenDialog(this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    try {
                        File selectedFile = importFileChooser.getSelectedFile();
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
                                importFileChooser = null;
                                JOptionPane.showMessageDialog(this, "Loaded in '" + selectedFile.getName() + "'!");
                            } else {
                                importFileChooser = null;
                                JOptionPane.showMessageDialog(this, "Cannot load in '" + selectedFile.getName() + "': " + error);
                            }
                        } catch (IllegalArgumentException ex) {
                            ex.printStackTrace();
                            importFileChooser = null;
                            JOptionPane.showMessageDialog(this, "An error occurred: " + ex.getLocalizedMessage());
                        }
                    } catch (FileNotFoundException ex) {
                        importFileChooser = null;
                        JOptionPane.showMessageDialog(this, "Error: " + ex.getLocalizedMessage());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        importFileChooser = null;
                        JOptionPane.showMessageDialog(this, "An error occurred!");
                    }
                } else {
                    importFileChooser = null;
                }
            });
        });

        menuBar.add(fileMenu);

        return menuBar;
    }

    private JPanel constructAndGetControlPanel() {
        // Make new panel
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.X_AXIS));

        // Add top spacer
        controlPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        // Make new buttons
        JButton buttonApply = new JButton("Apply");
        buttonApply.setName("ApplyButton");

        JButton buttonCancel = new JButton("Cancel");
        buttonCancel.setName("CancelButton");

        getRootPane().setDefaultButton(buttonCancel);

        // Set button actions.
        buttonApply.addActionListener(e -> onApply());
        buttonCancel.addActionListener(e -> onCancel());

        // Add the components
        controlPanel.add(Box.createHorizontalGlue());
        controlPanel.add(buttonApply);
        controlPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        controlPanel.add(buttonCancel);

        return controlPanel;
    }

    /* Data methods */
    private List<String> checkInputForErrors() {
        List<String> errors = new ArrayList<>();

        // Useful definitions
        int playerCount = GameController.getBoard().getPlayerCount();

        // Collect information
        int totalKorgoolCount = 0;
        int[] holeKorgoolPerSideCount = new int[playerCount];
        int[] tubIndexes = new int[playerCount];
        for (int playerId = 0; playerId < playerCount; playerId++) {
            totalKorgoolCount += (int) ((JSpinner) getComponentByName("Player" + playerId + "Kazan")).getValue();
            tubIndexes[playerId] = ((JComboBox) getComponentByName("Player" + playerId + "Tuz")).getSelectedIndex() - 1;
            for (int i = 0; i < Constants.CONSTRAINT_HOLES_PER_PLAYER; i++) {
                holeKorgoolPerSideCount[playerId] += (int) ((JSpinner) getComponentByName("Player" + playerId + "Hole" + i)).getValue();
            }
        }
        int holeKorgoolCount = IntStream.of(holeKorgoolPerSideCount).sum();
        totalKorgoolCount += holeKorgoolCount;


        // Check violations
        // Validation 1 & 2: the sum of korgools in holes of a side must not be greater than 161 and not less than 1
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

        // Validation 3: the sum of korgools on the board must be 162
        if (totalKorgoolCount != Constants.CONSTRAINT_TOTAL_KORGOOLS) {
            errors.add(Constants.ERROR_CUSTOM_GUI_CONSTRAINT_TOTAL_KORGOOLS_VIOLATION);
        }

        // Validation 4: Tuz can be the same hole.
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

        return errors;
    }

    private void sendInputDataToBackEnd() {
        Board board = GameController.getBoard();

        // For each player

        // Reset the kazan
        for (int playerId = 0; playerId < board.getPlayerCount(); playerId++) {
            board.setKorgoolsInKazan(playerId, 0);
        }

        // Update holes and kazan.
        for (int playerId = 0; playerId < board.getPlayerCount(); playerId++) {
            // Set number of korgools in hole
            for (int i = 0; i < Constants.CONSTRAINT_HOLES_PER_PLAYER; i++) {
                int holeCount = (int) ((JSpinner) getComponentByName("Player" + playerId + "Hole" + i)).getValue();
                board.setKorgoolsInHole(playerId, i, holeCount);
            }

            // Set number of korgools in kazan
            int kazanCount = board.getKorgoolsInKazan(playerId);
            board.setKorgoolsInKazan(playerId, kazanCount + (int) ((JSpinner) getComponentByName("Player" + playerId + "Kazan")).getValue());

            // Set tuz index
            int tuzIndex = ((JComboBox) getComponentByName("Player" + playerId + "Tuz")).getSelectedIndex() - 1;
            if (tuzIndex >= 0) {
                board.setTuz(playerId, tuzIndex);

                int holeCount = board.getKorgoolsInHole(playerId, tuzIndex);
                board.setKorgoolsInHole(playerId, tuzIndex, 0);

                int opponentId = board.getOpponentOf(playerId).getId();
                int opponentKazanCount = board.getKorgoolsInKazan(opponentId);
                board.setKorgoolsInKazan(opponentId, opponentKazanCount + holeCount);
            }
        }

        for (int playerId = 0; playerId < board.getPlayerCount(); playerId++) {
            if (board.hasPlayerWon(playerId)) {
                GameController.getInstance().onWin(playerId);
                break;
            }
        }
    }

    /* Getters */

    public JFileChooser getExportFileChooser() {
        return exportFileChooser;
    }

    public JFileChooser getImportFileChooser() {
        return importFileChooser;
    }

    /* Action methods */

    private void onApply() {
        List<String> errors = checkInputForErrors();
        if (!errors.isEmpty()) { // The data is invalid
            // Make a JOptionPanel with a message displaying the errors in the errors list
            String errorString = makeErrorString(errors);
            JOptionPane.showMessageDialog(this, errorString);
        } else {
            sendInputDataToBackEnd();
            GameController.updateGUI();
            dispose();
        }
    }

    private void onCancel() {
        dispose();
    }

    /* Helper methods */

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

    /* Methods adapted from https://stackoverflow.com/questions/4958600/get-a-swing-component-by-name */

    private void addToComponentMap(Component component) {
        componentMap.put(component.getName(), component);
    }

    private Component getComponentByName(String name) {
        return componentMap.getOrDefault(name, null);
    }

    /* Loading and saving methods */

    void loadUser(final Player player) {
        int tuzIndex = -1;
        for (int i = 0; i < player.getNumberOfHoles(); i++) {
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
        kazanSpinner.setValue(player.getKorgoolsInKazan());
    }

    void saveUser(final Player player) {
        for (int i = 0; i < player.getNumberOfHoles(); i++) {
            JSpinner holeSpinner = (JSpinner) componentMap.get("Player" + player.getId() + "Hole" + i);
            if (holeSpinner == null) break;
            player.clearHole(i);
            player.addToHole(i, (Integer) holeSpinner.getValue());
        }

        player.setTuz(-1);
        JComboBox<String> tuzComboBox = (JComboBox<String>) componentMap.get("Player" + player.getId() + "Tuz");
        if (tuzComboBox != null) {
            if (tuzComboBox.getSelectedIndex() > 0) {
                player.setTuz(Math.max(Math.min(tuzComboBox.getSelectedIndex() - 1, player.getNumberOfHoles() - 1), 0));
            }
        }

        JSpinner kazanSpinner = (JSpinner) componentMap.get("Player" + player.getId() + "Kazan");
        if (kazanSpinner != null) {
            player.setKazanCount(Math.max((Integer) kazanSpinner.getValue(), 0));
        }
    }

    /* Static helper methods */

    private static String makeErrorString(List<String> errorList) {
        StringBuilder sb = new StringBuilder();
        String lineStart = "· ";
        for (String errorMessage : errorList) {
            sb.append(lineStart).append(errorMessage).append(System.lineSeparator());
        }
        return sb.toString();
    }

    public static String validateBoard(final Board board) {
        if (board == null) return "Board is null";
        int previousTuzIndex = -1;
        for (Player player : board.getPlayers()) {
            String playerValidation = validateUser(player);
            if (playerValidation != null) return playerValidation;
            int playerTuzIndex = player.getTuzIndex();
            if (playerTuzIndex != -1 && playerTuzIndex == previousTuzIndex) {
                return "There cannot be a player with the same hole as their opponent marked as a tuz";
            }
            previousTuzIndex = playerTuzIndex;
        }
        return null;
    }

    private static String validateUser(final Player player) {
        String name = "player " + (player.getId() + 1);
        int tuzId = -1;
        for (int i = 0; i < player.getNumberOfHoles(); i++) {
            Hole hole = player.getHole(i);
            if (hole.getKorgools() < 0) {
                return "Hole " + (i + 1) + " cannot have less than 0 korgools for " + name;
            } else if (hole.getKorgools() > Constants.CONSTRAINT_TOTAL_KORGOOLS) {
                return "Hole " + (i + 1) + " cannot have more than " + Constants.CONSTRAINT_TOTAL_KORGOOLS + " korgools for " + name;
            }
            if (hole.isTuz()) {
                if (tuzId != -1)
                    return "More than one tuz found for " + name;
                if (i == 9 - 1) return "Hole 9 cannot be a tuz for " + name;
                tuzId = i;
            }
        }
        if (player.getKazan().getKorgools() < 0) {
            return name + " cannot have a kazan with less than 0 korgools.";
        } else if (player.getKazan().getKorgools() > Constants.CONSTRAINT_TOTAL_KORGOOLS) {
            return name + " cannot have a kazan with more than " + Constants.CONSTRAINT_TOTAL_KORGOOLS + " korgools.";
        }
        return null;
    }

}
