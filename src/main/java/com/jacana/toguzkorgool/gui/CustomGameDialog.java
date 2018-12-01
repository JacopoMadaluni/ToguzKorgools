package com.jacana.toguzkorgool.gui;

import com.jacana.toguzkorgool.Board;
import com.jacana.toguzkorgool.GameController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Map;


/**
 * The CustomGameDialog GUI allows the player to "set up" the board state
 * to their liking during any point of the game, using the modal this class
 * creates.
 * <p>
 * Upon clicking the "Apply" button, this class send the input data to the
 * back-end Board.
 */
public class CustomGameDialog extends JDialog {
    private JPanel contentPane;
    private Map<String, Component> componentMap = new HashMap<>();
    
    private CustomGameDialog() {
        setResizable(false);
        setModal(true);
        setTitle("Custom Game");
        
        contentPane = new JPanel();
        setContentPane(contentPane);
        
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
    
    static void showCustomGameDialog() {
        CustomGameDialog dialog = new CustomGameDialog();
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }
    
    public static void main(String[] args) {
        showCustomGameDialog();
        System.exit(0);
    }
    
    private void setUpComponents() {
        setJMenuBar(constructAndGetMenuBar());
        
        contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contentPane.setLayout(new BorderLayout());
        
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(0, 2, 5, 0));
        
        JPanel lightPanel = constructAndGetSidePanel("Light Player");
        JPanel darkPanel = constructAndGetSidePanel("Dark Player");
        
        inputPanel.add(lightPanel);
        inputPanel.add(darkPanel);
        
        JPanel controlPanel = constructAndGetControlPanel();
        
        contentPane.add(inputPanel, BorderLayout.CENTER);
        contentPane.add(controlPanel, BorderLayout.PAGE_END);
    }
    
    private JPanel constructAndGetSidePanel(String panelLabel) {
        // should be "dark" or "light"
        final String namePrefix = panelLabel.split(" ")[0].toLowerCase();
        final String namePrefixCapitalized = namePrefix.substring(0, 1).toUpperCase()
                + namePrefix.substring(1);
        
        //Get Data from board--------------------------------------------
        boolean isLightPanel = namePrefix.equals("light");
        //TODO Refactor this with player IDs or some other way
        int kazanKorgoolCount;
        int tuzIndex;
        if (isLightPanel) {
            kazanKorgoolCount = GameController.getBoard().getLightKazanCount();
            tuzIndex = GameController.getBoard().getLightPlayerTuzIndex();
        } else {
            kazanKorgoolCount = GameController.getBoard().getDarkKazanCount();
            tuzIndex = GameController.getBoard().getDarkPlayerTuzIndex();
        }
        
        //make the panel a vertical box layout
        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
        
        // add name label to the top
        JPanel sideLabelPanel = new JPanel();
        addAndHorizontalCenterInPanel(new JLabel(panelLabel), sideLabelPanel);
        
        sidePanel.add(sideLabelPanel);
        addHorizontalSeparator(sidePanel);
        
        //HOLE SPINNERS--------------------------------------------------
        // create 9 numerical spinners with names and labels, each within a
        // flow layout
        for (int i = 1; i <= 9; i++) {
            JPanel holeSpinnerPanel = new JPanel();
            
            SpinnerNumberModel spinnerHoleModel = new SpinnerNumberModel(0, 0, 161, 1);
            JSpinner holeSpinner = new JSpinner(spinnerHoleModel);
            
            holeSpinner.setName(namePrefix + "HoleSpinner" + i);
            
            //TODO Refactor this with player IDs or some other way
            if (isLightPanel) {
                holeSpinner.setValue(GameController.getBoard().getLightHoleKorgoolCount(i - 1));
            } else {
                holeSpinner.setValue(GameController.getBoard().getDarkHoleKorgoolCount(i - 1));
            }
            
            JLabel newSpinnerLabel = new JLabel("Hole " + (i) + ":");
            
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
        kazanSpinner.setName(namePrefix + "KazanSpinner");
        JLabel kazanLabel = new JLabel(namePrefixCapitalized + " Kazan:");
        
        kazanPanel.add(kazanLabel);
        kazanPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        kazanPanel.add(kazanSpinner);
        addToComponentMap(kazanSpinner);
        
        sidePanel.add(kazanPanel);
        addHorizontalSeparator(sidePanel);
        
        //TUZ COMBO BOX--------------------------------------------------
        
        JPanel tuzPanel = new JPanel();
        
        JLabel tuzLabel = new JLabel(namePrefixCapitalized + "'s Tuz:");
        String[] tuzOptions = {"None", "1st Hole", "2nd Hole", "3rd Hole",
                "4th Hole", "5th Hole", "6th Hole", "7th Hole", "8th Hole"};
        JComboBox<String> tuzComboBox = new JComboBox<>(tuzOptions);
        tuzComboBox.setName(namePrefix + "TuzComboBox");
        
        //TODO Test this behaviour once Tuz is identifiable in the GUI
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
        
        //TODO Replace placeholders from import/export statements
        exportMenuItem.addActionListener(e -> System.out.println("I am a placeholder"));
        importMenuItem.addActionListener(e -> System.out.println("I am a placeholder"));
        
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
        buttonApply.setName("buttonApply");
        
        JButton buttonCancel = new JButton("Cancel");
        buttonCancel.setName("buttonCancel");
        
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
    private int validateInputData() { //Currently this method always returns that the data is valid.
        int retval = 0;
        //the Sum of korgools in holes of a side must not be greater than 161
        //the Sum of korgools on the board must be 162
        //tuz rules
        //game ending rules:
        //there must be at least one korgool on a side.
        //etc
        return retval;
    }
    
    //TODO Refactor code duplication
    private void sendInputDataToBackEnd() {
        //set holes for light
        Board board = GameController.getBoard();
        
        //TODO Refactor board API to mitigate code duplication
        
        //LIGHT---------------------------------------------------------
        //set holes for light
        for (int i = 1; i <= 9; i++) {
            int lightHoleCount = (int) ((JSpinner) getComponentByName("lightHoleSpinner" + i)).getValue();
            board.setLightHoleCount(i - 1, lightHoleCount);
        }
        
        //set kazan for light
        int lightKazanCount = (int) ((JSpinner) getComponentByName("lightKazanSpinner")).getValue();
        board.setLightKazanCount(lightKazanCount);
        
        //set tuz for light
        int lightTuzIndex = ((JComboBox) getComponentByName("lightTuzComboBox")).getSelectedIndex() - 1;
        if (lightTuzIndex >= 0) board.setLightPlayerTuz(lightTuzIndex);
    
        //DARK-----------------------------------------------------------
        //set holes for dark
        for (int i = 1; i <= 9; i++) {
            int darkHoleCount = (int) ((JSpinner) getComponentByName("darkHoleSpinner" + i)).getValue();
            board.setDarkHoleCount(i - 1, darkHoleCount);
        }
    
        //set kazan for dark
        int darkKazanValue = (int) ((JSpinner) getComponentByName("darkKazanSpinner")).getValue();
        board.setDarkKazanCount(darkKazanValue);
    
        //set tuz for dark
        int darkTuzIndex = ((JComboBox) getComponentByName("darkTuzComboBox")).getSelectedIndex() - 1;
        if (darkTuzIndex >= 0) board.setDarkPlayerTuz(darkTuzIndex);
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
        if (validateInputData() > 0) /*data invalid*/ {
            return; //TODO make more elegant
        } else {
            sendInputDataToBackEnd();
            GameController.updateGUI();
        }
        dispose();
    }
    
    private void onCancel() {
        dispose();
    }
}
