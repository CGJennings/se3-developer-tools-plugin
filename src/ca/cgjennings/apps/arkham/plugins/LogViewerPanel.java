package ca.cgjennings.apps.arkham.plugins;

import ca.cgjennings.apps.arkham.StrangeEons;
import ca.cgjennings.apps.arkham.ToolWindow;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.lang.reflect.Field;
import java.util.Locale;
import java.util.logging.Level;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.Timer;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

/**
 * Displays the application log content.
 *
 * @author Christopher G. jennings (cjennings@acm.org)
 */
public class LogViewerPanel extends javax.swing.JPanel {

    private StringBuffer logBuffer;
    private int buffLen;
    private Timer pollTimer;

    public LogViewerPanel(ToolWindow tw) {
        initComponents();
        initLevelPopup();

        try {
            Field fLogBuffer = StrangeEons.class.getDeclaredField("logBuffer");
            fLogBuffer.setAccessible(true);
            logBuffer = (StringBuffer) fLogBuffer.get(null);
            fLogBuffer.setAccessible(false);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            StrangeEons.log.log(Level.SEVERE, null, e);
            throw new AssertionError();
        }
        pollTimer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateLog();
            }
        });
        tw.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                updateLog();
                pollTimer.start();
            }

            @Override
            public void componentHidden(ComponentEvent e) {
                pollTimer.stop();
            }
        });
    }

    private void updateLog() {
        String newText = null;
        synchronized (logBuffer) {
            int newLen = logBuffer.length();
            if (buffLen != newLen) {
                newText = logBuffer.substring(buffLen, newLen);
                buffLen = newLen;
            }
        }
        if (newText != null) {
            try {
                Document doc = logField.getDocument();
                doc.insertString(doc.getLength(), newText, null);
            } catch (BadLocationException ex) {
                // impossible
            }
        }
    }

    private void initLevelPopup() {
        ActionListener li = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String levelName = ((JRadioButtonMenuItem) e.getSource()).getText().toUpperCase(Locale.CANADA);
                Level newLevel = Level.parse(levelName);
                StrangeEons.log.setLevel(newLevel);
            }
        };
        createItem("Off", li);
        createItem("Severe", li);
        createItem("Warning", li);
        createItem("Info", li);
        createItem("Config", li);
        createItem("Fine", li);
        createItem("All", li);
    }

    private void createItem(String name, ActionListener li) {
        JRadioButtonMenuItem it = new JRadioButtonMenuItem(name);
        it.addActionListener(li);
        levelGroup.add(it);
        Level cur = StrangeEons.log.getLevel();
        boolean select = cur.toString().equalsIgnoreCase(name);
        if (!select && (cur == Level.FINER || cur == Level.FINEST) && name.equals("All")) {
            select = true;
        }
        it.setSelected(select);
        logLevelPopup.add(it);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        logLevelPopup = new javax.swing.JPopupMenu();
        levelGroup = new javax.swing.ButtonGroup();
        javax.swing.JScrollPane logScroll = new javax.swing.JScrollPane();
        logField = new javax.swing.JTextArea();

        setBackground(java.awt.Color.darkGray);
        setLayout(new java.awt.BorderLayout());

        logScroll.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));

        logField.setEditable(false);
        logField.setBackground(java.awt.Color.darkGray);
        logField.setColumns(20);
        logField.setFont(new java.awt.Font("Monospaced", 0, 11)); // NOI18N
        logField.setForeground(java.awt.Color.white);
        logField.setLineWrap(true);
        logField.setRows(5);
        logField.setTabSize(4);
        logField.setWrapStyleWord(true);
        logField.setCaretColor(java.awt.Color.white);
        logField.setComponentPopupMenu(logLevelPopup);
        logField.setSelectedTextColor(java.awt.Color.black);
        logField.setSelectionColor(new java.awt.Color(255, 200, 0));
        logScroll.setViewportView(logField);

        add(logScroll, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup levelGroup;
    private javax.swing.JTextArea logField;
    private javax.swing.JPopupMenu logLevelPopup;
    // End of variables declaration//GEN-END:variables
}
