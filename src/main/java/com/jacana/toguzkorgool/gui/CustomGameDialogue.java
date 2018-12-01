package com.jacana.toguzkorgool.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;



/**
 * The CustomGameDialogue GUI allows the player to "set up" the board state
 * to their liking during any point of the game, using the modal this class
 * creates.
 *
 * Upon clicking the "Apply" button, this class send the input data to the
 * back-end Board.
 */
public class CustomGameDialogue extends JDialog {
    private JPanel contentPane;
    private List<JSpinner> lightHoleSpinners = new ArrayList<>();
    private List<JSpinner> darkHoleSpinners = new ArrayList<>();
    
    private CustomGameDialogue() {
        setResizable(false);
        setModal(true);
        setTitle("Custom Game");
        
        contentPane = new JPanel();
        setContentPane(contentPane);
        
        setUpComponents();
        setUpComponentsInitialData();
        
        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });
        
        // call onCancel() on ESCAPE
        contentPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(
                "released ESCAPE"),"closeWindow" );
        contentPane.getActionMap().put("closeWindow", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });
    }
    
    static void showCustomGameDialogue() {
        CustomGameDialogue dialog = new CustomGameDialogue();
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }
    
    public static void main(String[] args) {
        showCustomGameDialogue();
        System.exit(0);
    }
    
    private void setUpComponents() {
        setJMenuBar(constructAndGetMenuBar());
        
        contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contentPane.setLayout(new BorderLayout());
        
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(0, 2, 5, 0));
        
        JPanel lightPanel = constructAndGetSidePanel("Light Player", lightHoleSpinners);
        JPanel darkPanel = constructAndGetSidePanel("Dark Player", darkHoleSpinners);
        
        inputPanel.add(lightPanel);
        inputPanel.add(darkPanel);
        
        JPanel controlPanel = constructAndGetControlPanel();
        
        contentPane.add(inputPanel, BorderLayout.CENTER);
        contentPane.add(controlPanel, BorderLayout.PAGE_END);
    }
    
    private void setUpComponentsInitialData() {
    }
    
    private JPanel constructAndGetSidePanel(String panelLabel,
                                            List<JSpinner> panelHoleSpinners) {
        // should be "dark" or "light"
        final String namePrefix = panelLabel.split(" ")[0].toLowerCase();
        final String namePrefixCapitalized = namePrefix.substring(0, 1).toUpperCase()
                + namePrefix.substring(1);
        
        //make the panel a vertical box layout
        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
        
        // add name label to the top
        JPanel sideLabelPanel = new JPanel();
        addAndHorizontalCenterInPanel(new JLabel(panelLabel), sideLabelPanel);
        
        sidePanel.add(sideLabelPanel);
        addHorizontalSeparator(sidePanel);
        
        // HOLE SPINNERS--------------------------
        // create 9 numerical spinners with names and labels, each within a
        // flow layout
        for (int i = 1; i <= 9; i++) {
            JPanel holeSpinnerPanel = new JPanel();

            JSpinner holeSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 161, 1));
            holeSpinner.setName(namePrefix + "HoleSpinner" + i);
            //TODO set the spinners value with Board.getLightHoleKorgoolCount(index).

            JLabel newSpinnerLabel = new JLabel("Hole " + (i) + ":");
            
            //add the label and the spinner to the panel
            holeSpinnerPanel.add(newSpinnerLabel);
            holeSpinnerPanel.add(Box.createRigidArea(new Dimension(10, 0)));
            holeSpinnerPanel.add(holeSpinner);

            sidePanel.add(holeSpinnerPanel);
            
            //TODO Make a component map (component.getName() -> Component Object) instead of arrays
            panelHoleSpinners.add(holeSpinner);
        }
        addHorizontalSeparator(sidePanel);
        
        //KAZAN SPINNER--------------------------
        
        JPanel kazanPanel = new JPanel();
        
        //TODO get the kazan count via Board.get Light/Dark KazanCount(); add it to the model.
        SpinnerNumberModel spinnerKazanModel = new SpinnerNumberModel(0, 0, 82, 1);
        JSpinner kazanSpinner = new JSpinner(spinnerKazanModel);
        kazanSpinner.setName(namePrefix + "KazanSpinner");
        JLabel kazanLabel = new JLabel(namePrefixCapitalized + " Kazan:");
        
        kazanPanel.add(kazanLabel);
        kazanPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        kazanPanel.add(kazanSpinner);
        
        sidePanel.add(kazanPanel);
        addHorizontalSeparator(sidePanel);
        
        //TUZ COMBO BOX--------------------------
        
        JPanel tuzPanel = new JPanel();
        
        JLabel tuzLabel = new JLabel(namePrefixCapitalized + " Tuz:");
        String[] tuzOptions = {"None", "1st Hole", "2nd Hole", "3rd Hole",
                "4th Hole", "5th Hole", "6th Hole", "7th Hole", "8th Hole"};
        JComboBox<String> tuzComboBox = new JComboBox<>(tuzOptions);
        tuzComboBox.setName(namePrefix + "TuzComboBox");
        //TODO Set combobox selection to current tuz value via Board.getTuz()
        tuzComboBox.setSelectedIndex(0);
        
        //add to the side panel.
        tuzPanel.add(tuzLabel);
        tuzPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        tuzPanel.add(tuzComboBox);
        
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
        
        //TODO Add event listeners for Export and Import menu items.
        
        exportMenuItem.addActionListener(e -> placeholderActionMethod());
        importMenuItem.addActionListener(e -> placeholderActionMethod());
        
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
    
    //TODO Move helper methods into a static helper methods class
    /*Helper methods*/
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
    
    private void placeholderActionMethod() {
        System.out.println("Action was performed.");
    }
    
    /*Action Methods*/
    private void onApply() {
        //TODO check data with validator
        //TODO set up board
        dispose();
    }
    
    private void onCancel() {
        dispose();
    }
}
