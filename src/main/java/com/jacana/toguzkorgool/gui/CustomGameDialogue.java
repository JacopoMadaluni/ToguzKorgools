package com.jacana.toguzkorgool.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CustomGameDialogue extends JDialog
{
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JSpinner lightKazan;
    private JSpinner lightHoleSpinner1;
    private JSpinner lightHoleSpinner2;
    private JSpinner lightHoleSpinner3;
    private JSpinner lightHoleSpinner4;
    private JSpinner lightHoleSpinner5;
    private JSpinner lightHoleSpinner6;
    private JSpinner lightHoleSpinner7;
    private JSpinner lightHoleSpinner8;
    private JPanel buttonPanel;
    private JPanel controlPanel;
    private JPanel lightSidePanel;
    private JPanel darkSidePanel;
    private JPanel sideSeparatorPanel;
    
    public CustomGameDialogue()
    {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        
        buttonOK.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                onOK();
            }
        });
        
        buttonCancel.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                onCancel();
            }
        });
        
        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                onCancel();
            }
        });
        
        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }
    
    private void onOK()
    {
        // add your code here
        dispose();
    }
    
    private void onCancel()
    {
        // add your code here if necessary
        dispose();
    }
    
    public static void main(String[] args)
    {
        CustomGameDialogue dialog = new CustomGameDialogue();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
