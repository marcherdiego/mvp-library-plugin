package com.nerdscorner.mvp.ui;

import javax.swing.*;

public class ResultDialog extends JDialog {

    private static final String HTML_START = "<html>";
    private static final String HTML_END = "</html>";

    private JPanel contentPane;
    private JButton buttonOK;
    private JLabel resultMessage;

    public ResultDialog(String message) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(e -> onOK());

        resultMessage.setText(HTML_START + message + HTML_END);
    }

    private void onOK() {
        setVisible(false);
        dispose();
    }
}
