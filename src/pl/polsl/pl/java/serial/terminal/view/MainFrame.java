package pl.polsl.pl.java.serial.terminal.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

import pl.polsl.pl.java.serial.terminal.main.Controler;
import pl.polsl.pl.java.serial.terminal.view.helpers.DisableableTextPane;

/**
 * Main GUI class. 
 * Has menu bar with about connection commands
 * and two text areas with buttons to send and receive text.
 *
 * @author Michał Lytek
 */
public class MainFrame extends javax.swing.JFrame {

    /** Instance of controler class */
    private Controler controler;
    
    /** Instance of configuration dialog */
    private ConfigurationDialog configurationDialog;

    /**
     * Creates new main GUI form.
     * Initialize GUi component and child configuration dialog.
     *
     * @param controler instance of controler which will be handling GUI request
     */
    public MainFrame(Controler controler) {
        this.controler = controler;
        this.configurationDialog = new ConfigurationDialog(this, true, controler);

        //setup system look & feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

            javax.swing.UIManager.getDefaults().entrySet().stream().map((entry) -> entry.getKey()).forEach((key) -> {
                Object value = javax.swing.UIManager.get(key);
                if (value != null && value instanceof javax.swing.plaf.FontUIResource) {
                    javax.swing.plaf.FontUIResource fr = (javax.swing.plaf.FontUIResource) value;
                    javax.swing.plaf.FontUIResource f = new javax.swing.plaf.FontUIResource("Tahoma", fr.getStyle(), 11);
                    javax.swing.UIManager.put(key, f);
                }
            });
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            System.err.println("Using the default look and feel.");
        }

        initComponents();
        postInitComponents();
        
        MainFrame.this.setVisible(true);
    }

    /**
     * Custom field setup after Designer generated code initalization.
     */
    private void postInitComponents() {
        upperPanel.setMinimumSize(new Dimension(500, 100));
        bottomPanel.setMinimumSize(new Dimension(500, 200));

        connectionDetailsPanel.setVisible(false);

        connectMenuItem.setEnabled(false);
        disconnectMenuItem.setEnabled(false);
        testConnectionMenuItem.setEnabled(false);

        portStatusLabel.setText("Nie połączono");
        portNameLabel.setVisible(false);

        receivingTextPane.setBackground(new Color(240, 240, 240));
        receivingTextPane.getCaret().setVisible(false);
        
        Font jLabel2Font = jLabel2.getFont();
        jLabel2.setFont(jLabel2Font.deriveFont((float) (jLabel2Font.getSize() * 1.2)));
        
        Font jLabel1Font = jLabel1.getFont();
        jLabel1.setFont(jLabel1Font.deriveFont((float) (jLabel1Font.getSize() * 1.2)));
        
        packWindow();
        MainFrame.this.setLocationRelativeTo(null);
    }

    /**
     * Handle demands of presenting received text on screen.
     * Checks if the text should be placed in new line or not.
     *
     * @param receivedLine the string to insert in text area
     * @param insertInNewLine true if the line should be placed in new line, false if appended to te current text
     */
    public void insertReceivedText(String receivedLine, boolean insertInNewLine) {
        ((DisableableTextPane) receivingTextPane).setWritable(true);
        StyledDocument receivingDocument = receivingTextPane.getStyledDocument();
        try {
            if (receivingTextPane.getText().length() == 0 || !insertInNewLine) {
                receivingDocument.insertString(receivingDocument.getLength(), receivedLine, null);
            } else {
                receivingDocument.insertString(receivingDocument.getLength(), "\n" + receivedLine, null);
            }

        } catch (BadLocationException ex) {
            System.err.println(ex);
        }
        ((DisableableTextPane) receivingTextPane).setWritable(false);
    }

    /**
     * Handle controler request after setting up connection parameters fields.
     * It unlock hidden panel and load label values with current connection settings.
     */
    public void showConnectionParameters() {
        connectMenuItem.setEnabled(true);

        portStatusLabel.setText("Nie połączono:");
        portNameLabel.setVisible(true);
        portNameLabel.setText(controler.getConnectedPortName());
        portSpeedLabel.setText(controler.getConnectedPortSpeed() + " bps");
        signFormatLabel.setText(controler.getConnectedPortSignFormat());
        flowControlLabel.setText(controler.getConnectedPortFlowControl());
        terminatorLabel.setText(controler.getConnectedPortTerminator());

        connectionDetailsPanel.setVisible(true);
        
        packWindow();
    }

    /**
     * Handle demand of showing connection test result.
     * It shows dialog with information about test.
     *
     * @param isSuccesful true if test was ok, false if there was an error or timeout
     * @param pingResult the round trip delay time in ms - ignored if isSuccesful is false
     */
    public void showConnectionTestResults(boolean isSuccesful, int pingResult) {
        if (isSuccesful) {
            JOptionPane.showOptionDialog(this,
                    "Test łącza zakończony sukcesem!\n"
                    + "Czas \"round trip delay\": " + pingResult + " ms.",
                    "Test łącza",
                    JOptionPane.PLAIN_MESSAGE,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    null,
                    null
            );
        } else {
            JOptionPane.showOptionDialog(this,
                    "Test łącza zakończony niepowodzeniem!\n"
                    + "Sprawdź czy urządzenie podpięte jest poprawnie.",
                    "Test łącza",
                    JOptionPane.PLAIN_MESSAGE,
                    JOptionPane.ERROR_MESSAGE,
                    null,
                    null,
                    null
            );
        }
    }
    
    private void packWindow() {
        setMinimumSize(new Dimension(800, 600));
        pack();
        setMinimumSize(MainFrame.this.getBounds().getSize());
//        pack();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        MainSplitPane = new javax.swing.JSplitPane();
        upperPanel = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        sendingScrollPane = new javax.swing.JScrollPane();
        sendingTextPane = new javax.swing.JTextPane();
        sendButton = new javax.swing.JButton();
        cleanSendedButton = new javax.swing.JButton();
        bottomPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        receivingScrollPane = new javax.swing.JScrollPane();
        receivingTextPane = new DisableableTextPane();
        cleanReceivedButton = new javax.swing.JButton();
        statusBarPanel = new javax.swing.JPanel();
        portStatusLabel = new javax.swing.JLabel();
        portNameLabel = new javax.swing.JLabel();
        connectionDetailsPanel = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        portSpeedLabel = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        signFormatLabel = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        flowControlLabel = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        terminatorLabel = new javax.swing.JLabel();
        connectionStatusIcon = new javax.swing.JLabel();
        menuBar = new javax.swing.JMenuBar();
        mainMenu = new javax.swing.JMenu();
        exitMenuItem = new javax.swing.JMenuItem();
        connectionMenu = new javax.swing.JMenu();
        connectMenuItem = new javax.swing.JMenuItem();
        disconnectMenuItem = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        portConfigurationMenuItem = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        testConnectionMenuItem = new javax.swing.JMenuItem();
        infoMenu = new javax.swing.JMenu();
        aboutMenuItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Java Serial Terminal");
        setMinimumSize(new java.awt.Dimension(800, 600));

        MainSplitPane.setBorder(null);
        MainSplitPane.setDividerLocation(200);
        MainSplitPane.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        upperPanel.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(204, 204, 204)));

        jLabel2.setText("Nadawanie:");

        sendingScrollPane.setViewportView(sendingTextPane);

        sendButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pl/polsl/pl/java/serial/terminal/view/images/send.png"))); // NOI18N
        sendButton.setText("Wyślij");
        sendButton.setEnabled(false);
        sendButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendButtonActionPerformed(evt);
            }
        });

        cleanSendedButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pl/polsl/pl/java/serial/terminal/view/images/eraser.png"))); // NOI18N
        cleanSendedButton.setText("Wyczyść");
        cleanSendedButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cleanSendedButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout upperPanelLayout = new javax.swing.GroupLayout(upperPanel);
        upperPanel.setLayout(upperPanelLayout);
        upperPanelLayout.setHorizontalGroup(
            upperPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(upperPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(upperPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(sendingScrollPane)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, upperPanelLayout.createSequentialGroup()
                        .addGap(0, 594, Short.MAX_VALUE)
                        .addComponent(cleanSendedButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(sendButton))
                    .addGroup(upperPanelLayout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        upperPanelLayout.setVerticalGroup(
            upperPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(upperPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sendingScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 126, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(upperPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(sendButton)
                    .addComponent(cleanSendedButton))
                .addGap(6, 6, 6))
        );

        MainSplitPane.setTopComponent(upperPanel);

        bottomPanel.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 0, new java.awt.Color(204, 204, 204)));

        jLabel1.setText("Odbieranie:");

        receivingTextPane.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        receivingTextPane.setDoubleBuffered(true);
        receivingScrollPane.setViewportView(receivingTextPane);

        cleanReceivedButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pl/polsl/pl/java/serial/terminal/view/images/eraser.png"))); // NOI18N
        cleanReceivedButton.setText("Wyczyść");
        cleanReceivedButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cleanReceivedButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout bottomPanelLayout = new javax.swing.GroupLayout(bottomPanel);
        bottomPanel.setLayout(bottomPanelLayout);
        bottomPanelLayout.setHorizontalGroup(
            bottomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bottomPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(bottomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(receivingScrollPane)
                    .addGroup(bottomPanelLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, bottomPanelLayout.createSequentialGroup()
                        .addGap(0, 685, Short.MAX_VALUE)
                        .addComponent(cleanReceivedButton)))
                .addContainerGap())
        );
        bottomPanelLayout.setVerticalGroup(
            bottomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bottomPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(receivingScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 260, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cleanReceivedButton)
                .addContainerGap())
        );

        MainSplitPane.setRightComponent(bottomPanel);

        portStatusLabel.setText("Połączono:");

        portNameLabel.setText("COM1");

        jLabel4.setText("Prędkość portu:");

        portSpeedLabel.setText("9600");

        jLabel6.setText("Format znaku:");

        signFormatLabel.setText("8N1");

        jLabel8.setText("Kontrola przepływu:");

        flowControlLabel.setText("n/a");

        jLabel10.setText("Terminator:");

        terminatorLabel.setText("LF");

        javax.swing.GroupLayout connectionDetailsPanelLayout = new javax.swing.GroupLayout(connectionDetailsPanel);
        connectionDetailsPanel.setLayout(connectionDetailsPanelLayout);
        connectionDetailsPanelLayout.setHorizontalGroup(
            connectionDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(connectionDetailsPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(portSpeedLabel)
                .addGap(18, 18, 18)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(signFormatLabel)
                .addGap(18, 18, 18)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(flowControlLabel)
                .addGap(18, 18, 18)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(terminatorLabel)
                .addContainerGap())
        );
        connectionDetailsPanelLayout.setVerticalGroup(
            connectionDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(connectionDetailsPanelLayout.createSequentialGroup()
                .addGroup(connectionDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(portSpeedLabel)
                    .addComponent(jLabel6)
                    .addComponent(signFormatLabel)
                    .addComponent(jLabel8)
                    .addComponent(flowControlLabel)
                    .addComponent(jLabel10)
                    .addComponent(terminatorLabel))
                .addGap(0, 0, 0))
        );

        connectionStatusIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pl/polsl/pl/java/serial/terminal/view/images/red_circle.png"))); // NOI18N
        connectionStatusIcon.setToolTipText("");

        javax.swing.GroupLayout statusBarPanelLayout = new javax.swing.GroupLayout(statusBarPanel);
        statusBarPanel.setLayout(statusBarPanelLayout);
        statusBarPanelLayout.setHorizontalGroup(
            statusBarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusBarPanelLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(connectionStatusIcon)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(portStatusLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(portNameLabel)
                .addGap(18, 18, 18)
                .addComponent(connectionDetailsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        statusBarPanelLayout.setVerticalGroup(
            statusBarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusBarPanelLayout.createSequentialGroup()
                .addGroup(statusBarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(connectionDetailsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(portNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(connectionStatusIcon, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(portStatusLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        mainMenu.setText("Menu");

        exitMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, java.awt.event.InputEvent.ALT_MASK));
        exitMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pl/polsl/pl/java/serial/terminal/view/images/exit.png"))); // NOI18N
        exitMenuItem.setText("Zamknij program");
        exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuItemActionPerformed(evt);
            }
        });
        mainMenu.add(exitMenuItem);

        menuBar.add(mainMenu);

        connectionMenu.setText("Połączenie");

        connectMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.ALT_MASK));
        connectMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pl/polsl/pl/java/serial/terminal/view/images/connect.png"))); // NOI18N
        connectMenuItem.setText("Połącz");
        connectMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                connectMenuItemActionPerformed(evt);
            }
        });
        connectionMenu.add(connectMenuItem);

        disconnectMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.ALT_MASK));
        disconnectMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pl/polsl/pl/java/serial/terminal/view/images/disconnect.png"))); // NOI18N
        disconnectMenuItem.setText("Rozłącz");
        disconnectMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                disconnectMenuItemActionPerformed(evt);
            }
        });
        connectionMenu.add(disconnectMenuItem);
        connectionMenu.add(jSeparator1);

        portConfigurationMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_K, java.awt.event.InputEvent.ALT_MASK));
        portConfigurationMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pl/polsl/pl/java/serial/terminal/view/images/configure.png"))); // NOI18N
        portConfigurationMenuItem.setText("Konfiguracja portu");
        portConfigurationMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                portConfigurationMenuItemActionPerformed(evt);
            }
        });
        connectionMenu.add(portConfigurationMenuItem);
        connectionMenu.add(jSeparator2);

        testConnectionMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_T, java.awt.event.InputEvent.ALT_MASK));
        testConnectionMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pl/polsl/pl/java/serial/terminal/view/images/test.png"))); // NOI18N
        testConnectionMenuItem.setText("Testuj łącze");
        testConnectionMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                testConnectionMenuItemActionPerformed(evt);
            }
        });
        connectionMenu.add(testConnectionMenuItem);

        menuBar.add(connectionMenu);

        infoMenu.setText("Info");

        aboutMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_I, java.awt.event.InputEvent.ALT_MASK));
        aboutMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pl/polsl/pl/java/serial/terminal/view/images/info.png"))); // NOI18N
        aboutMenuItem.setText("O programie");
        aboutMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutMenuItemActionPerformed(evt);
            }
        });
        infoMenu.add(aboutMenuItem);

        menuBar.add(infoMenu);

        setJMenuBar(menuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(MainSplitPane)
            .addComponent(statusBarPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(MainSplitPane)
                .addGap(0, 0, 0)
                .addComponent(statusBarPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Handle clean sended text area button pressing.
     * Cleans up a sending text area.
     *
     * @param evt is ignored
     */
    private void cleanSendedButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cleanSendedButtonActionPerformed
        sendingTextPane.setText(null);
    }//GEN-LAST:event_cleanSendedButtonActionPerformed

    /**
     * Handle sending button pressing.
     * Load text from sending text area and send it to controler.
     * If text wasn't sended ok, it shows error dialog message.
     *
     * @param evt is ignored
     */
    private void sendButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendButtonActionPerformed
        String text = sendingTextPane.getText();
        if ((text != null) && !text.equalsIgnoreCase("")) {
            if (controler.sendText(text)) {
                sendingTextPane.setText(null);
            } else {
                JOptionPane.showOptionDialog(this,
                        "Nie można wysłać wiadomości!\n"
                        + "Sprawdź czy połączenie nie uległo uszkodzeniu.",
                        "Błąd wysyłania",
                        JOptionPane.PLAIN_MESSAGE,
                        JOptionPane.ERROR_MESSAGE,
                        null,
                        null,
                        null
                );
            }
        }
    }//GEN-LAST:event_sendButtonActionPerformed

    /**
     * Handle cleaning received text area button pressing. It cleans received
     * text area.
     *
     * @param evt is ignored
     */
    private void cleanReceivedButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cleanReceivedButtonActionPerformed
        ((DisableableTextPane) receivingTextPane).setWritable(true);
        receivingTextPane.setText(null);
        ((DisableableTextPane) receivingTextPane).setWritable(false);
    }//GEN-LAST:event_cleanReceivedButtonActionPerformed

    /**
     * Handle exit demand from menu. It dispose all windows and close the
     * program.
     *
     * @param evt is ignored
     */
    private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitMenuItemActionPerformed
        configurationDialog.dispose();
        dispose();
    }//GEN-LAST:event_exitMenuItemActionPerformed

    /**
     * Handle port configuration demad from menu. Shows modal dialog to setup
     * connection parameters.
     *
     * @param evt is ignored
     */
    private void portConfigurationMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_portConfigurationMenuItemActionPerformed
        this.configurationDialog.setLocationRelativeTo(this);
        this.configurationDialog.setVisible(true);
    }//GEN-LAST:event_portConfigurationMenuItemActionPerformed

    /**
     * Handle show about infor from menu. It shows dialog with infos about
     * program author.
     *
     * @param evt is ignored
     */
    private void aboutMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aboutMenuItemActionPerformed
        JOptionPane.showOptionDialog(this,
                "Autor: Michał Lytek\n"
                + "Politechnika Śląska 2016",
                "O programie",
                JOptionPane.PLAIN_MESSAGE,
                JOptionPane.INFORMATION_MESSAGE,
                null, //do not use a custom icon
                null, //the titles of buttons
                null //default button title
        );
    }//GEN-LAST:event_aboutMenuItemActionPerformed

    /**
     * Handle disconenct request from menu. It send request to controler and if
     * was succesful, it disables and reenables menu items, but if not, it shows
     * error dialog message.
     *
     * @param evt is ignored
     */
    private void disconnectMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_disconnectMenuItemActionPerformed
        if (controler.disconnectFromPort()) {
            connectionStatusIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pl/polsl/pl/java/serial/terminal/view/images/red_circle.png")));

            disconnectMenuItem.setEnabled(false);
            connectMenuItem.setEnabled(true);
            portConfigurationMenuItem.setEnabled(true);
            testConnectionMenuItem.setEnabled(false);

            portStatusLabel.setText("Nie połączono:");

            sendButton.setEnabled(false);
        } else {
            JOptionPane.showOptionDialog(this,
                    "Nie można rozłączyć się z wybranym portem!\n"
                    + "Sprawdź czy fizyczne połączenie nie uległo uszkodzeniu.",
                    "Błąd rozłączenia",
                    JOptionPane.PLAIN_MESSAGE,
                    JOptionPane.ERROR_MESSAGE,
                    null,
                    null,
                    null
            );
        }

    }//GEN-LAST:event_disconnectMenuItemActionPerformed

    /**
     * Handle connect request from menu. It send demand to controler and if was
     * succesful, it disables and reenables menu items, but if not, it shows
     * error dialog message.
     *
     * @param evt is ignored
     */
    private void connectMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_connectMenuItemActionPerformed
        portStatusLabel.setText("Trwa łączenie...");
        portStatusLabel.validate();
        if (controler.connectToPort()) {
            connectionStatusIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pl/polsl/pl/java/serial/terminal/view/images/green_circle.png")));

            connectMenuItem.setEnabled(false);
            disconnectMenuItem.setEnabled(true);
            portConfigurationMenuItem.setEnabled(false);
            testConnectionMenuItem.setEnabled(true);

            portStatusLabel.setText("Połączono:");

            sendButton.setEnabled(true);
        } else {
            portStatusLabel.setText("Nie połączono");
            JOptionPane.showOptionDialog(this,
                    "Nie można połączyć się z wybranym portem!\n"
                    + "Prawdopodobnie port jest już w użyciu.",
                    "Błąd połączenia",
                    JOptionPane.PLAIN_MESSAGE,
                    JOptionPane.ERROR_MESSAGE,
                    null,
                    null,
                    null
            );
        }
    }//GEN-LAST:event_connectMenuItemActionPerformed

    /**
     * Handle connection test request from menu.
     * It send the demand to controler.
     *
     * @param evt is ignored
     */
    private void testConnectionMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_testConnectionMenuItemActionPerformed
        controler.testConnection();
    }//GEN-LAST:event_testConnectionMenuItemActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JSplitPane MainSplitPane;
    private javax.swing.JMenuItem aboutMenuItem;
    private javax.swing.JPanel bottomPanel;
    private javax.swing.JButton cleanReceivedButton;
    private javax.swing.JButton cleanSendedButton;
    private javax.swing.JMenuItem connectMenuItem;
    private javax.swing.JPanel connectionDetailsPanel;
    private javax.swing.JMenu connectionMenu;
    private javax.swing.JLabel connectionStatusIcon;
    private javax.swing.JMenuItem disconnectMenuItem;
    private javax.swing.JMenuItem exitMenuItem;
    private javax.swing.JLabel flowControlLabel;
    private javax.swing.JMenu infoMenu;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JMenu mainMenu;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenuItem portConfigurationMenuItem;
    private javax.swing.JLabel portNameLabel;
    private javax.swing.JLabel portSpeedLabel;
    private javax.swing.JLabel portStatusLabel;
    private javax.swing.JScrollPane receivingScrollPane;
    private javax.swing.JTextPane receivingTextPane;
    private javax.swing.JButton sendButton;
    private javax.swing.JScrollPane sendingScrollPane;
    private javax.swing.JTextPane sendingTextPane;
    private javax.swing.JLabel signFormatLabel;
    private javax.swing.JPanel statusBarPanel;
    private javax.swing.JLabel terminatorLabel;
    private javax.swing.JMenuItem testConnectionMenuItem;
    private javax.swing.JPanel upperPanel;
    // End of variables declaration//GEN-END:variables
}
