package ca.cgjennings.apps.arkham.plugins;

import ca.cgjennings.apps.arkham.ToolWindow;
import ca.cgjennings.apps.arkham.project.ProjectUtilities;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.text.NumberFormat;
import javax.swing.DefaultListModel;
import javax.swing.Timer;
import resources.CacheMetrics;
import resources.ResourceKit;

/**
 * Panel for the tool window that displays cache metrics info.
 */
final class CacheManagerPanel extends javax.swing.JPanel {

    /**
     * Creates new form CacheManagerPanel
     */
    public CacheManagerPanel(ToolWindow tw) {
        initComponents();
        install(tw);
        reload();
    }

    private ToolWindow owner;

    void install(ToolWindow tw) {
        owner = tw;
        tw.setBody(this);
        tw.pack();
        tw.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                windowVisible();
            }

            @Override
            public void componentHidden(ComponentEvent e) {
                windowHidden();
            }
        });
    }

    private void windowVisible() {
        reload();
        updateCachePanel();
        updateTimer.start();
    }

    private void windowHidden() {
        updateTimer.stop();
    }

    private void reload() {
        CacheMetrics[] metrics = ResourceKit.getRegisteredCacheMetrics();
        if (model.getSize() == metrics.length) {
            int i;
            for (i = 0; i < metrics.length; ++i) {
                if (metrics[i] != (CacheMetrics) model.get(i)) {
                    break;
                }
            }
            // the list of metrics hasn't changed; exit
            if (i == metrics.length) {
                return;
            }
        }

        // if we reach here, then the current list of metrics instances is
        // different and we need to update the list
        Object selection = cacheList.getSelectedValue();
        model.removeAllElements();
        for (int i = 0; i < metrics.length; ++i) {
            model.addElement(metrics[i]);
        }
        cacheList.setSelectedValue(selection, true);
        if (cacheList.getSelectedIndex() < 0) {
            cacheList.setSelectedIndex(0);
        }
    }
    private DefaultListModel model = new DefaultListModel();

    private void updateCachePanel() {
        CacheMetrics cm = (CacheMetrics) cacheList.getSelectedValue();
        if (cm == null) {
            return;
        }

        cacheNameLabel.setText(cm.toString());
        typeLabel.setText(cm.getContentType().getSimpleName());
        objLabel.setText(formatter.format(cm.getItemCount()));
        String size;
        long bytes = cm.getByteSize();
        if (bytes < 0) {
            size = "Unknown";
        } else {
            size = ProjectUtilities.formatByteSize(bytes);
        }
        sizeLabel.setText(size);
        clearBtn.setEnabled(cm.isClearSupported());
    }

    private NumberFormat formatter = NumberFormat.getIntegerInstance();

    private static final int UPDATE_DELAY = 3000;

    private Timer updateTimer = new Timer(UPDATE_DELAY, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (isVisible()) {
                updateCachePanel();
                if (++callNum == 10) {
                    callNum = 0;
                    reload();
                }
            }
        }
        private int callNum = 0;
    });

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        splitter = new javax.swing.JSplitPane();
        listScroll = new javax.swing.JScrollPane();
        cacheList = new javax.swing.JList();
        cachePanel = new javax.swing.JPanel();
        cacheNameLabel = new javax.swing.JLabel();
        infoPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        typeLabel = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        objLabel = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        sizeLabel = new javax.swing.JLabel();
        clearBtn = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout());

        splitter.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        splitter.setDividerLocation(150);
        splitter.setDividerSize(2);
        splitter.setResizeWeight(0.5);

        listScroll.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        listScroll.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        cacheList.setBackground(java.awt.Color.darkGray);
        cacheList.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        cacheList.setFont(cacheList.getFont().deriveFont(cacheList.getFont().getSize()-1f));
        cacheList.setForeground(java.awt.Color.white);
        cacheList.setModel( model );
        cacheList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        cacheList.setVisibleRowCount(4);
        cacheList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                cacheListValueChanged(evt);
            }
        });
        listScroll.setViewportView(cacheList);

        splitter.setLeftComponent(listScroll);

        cachePanel.setLayout(new java.awt.BorderLayout());

        cacheNameLabel.setBackground(new java.awt.Color(32, 32, 32));
        cacheNameLabel.setFont(cacheNameLabel.getFont().deriveFont(cacheNameLabel.getFont().getStyle() | java.awt.Font.BOLD));
        cacheNameLabel.setForeground(java.awt.Color.orange);
        cacheNameLabel.setText(" ");
        cacheNameLabel.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, java.awt.Color.gray), javax.swing.BorderFactory.createEmptyBorder(8, 8, 8, 8)));
        cacheNameLabel.setOpaque(true);
        cachePanel.add(cacheNameLabel, java.awt.BorderLayout.PAGE_START);

        infoPanel.setBackground(java.awt.Color.darkGray);
        infoPanel.setForeground(java.awt.Color.white);

        jLabel1.setFont(jLabel1.getFont().deriveFont(jLabel1.getFont().getSize()-1f));
        jLabel1.setForeground(java.awt.Color.white);
        jLabel1.setText("Content Type");

        typeLabel.setFont(typeLabel.getFont().deriveFont(typeLabel.getFont().getSize()-1f));
        typeLabel.setForeground(java.awt.Color.white);
        typeLabel.setText(" ");

        jLabel2.setFont(jLabel2.getFont().deriveFont(jLabel2.getFont().getSize()-1f));
        jLabel2.setForeground(java.awt.Color.white);
        jLabel2.setText("Entries");

        objLabel.setFont(objLabel.getFont().deriveFont(objLabel.getFont().getSize()-1f));
        objLabel.setForeground(java.awt.Color.white);
        objLabel.setText(" ");

        jLabel3.setFont(jLabel3.getFont().deriveFont(jLabel3.getFont().getSize()-1f));
        jLabel3.setForeground(java.awt.Color.white);
        jLabel3.setText("Size");

        sizeLabel.setFont(sizeLabel.getFont().deriveFont(sizeLabel.getFont().getSize()-1f));
        sizeLabel.setForeground(java.awt.Color.white);
        sizeLabel.setText(" ");

        clearBtn.setFont(clearBtn.getFont().deriveFont(clearBtn.getFont().getSize()-1f));
        clearBtn.setText("Clear");
        clearBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout infoPanelLayout = new javax.swing.GroupLayout(infoPanel);
        infoPanel.setLayout(infoPanelLayout);
        infoPanelLayout.setHorizontalGroup(
            infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(infoPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(infoPanelLayout.createSequentialGroup()
                        .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(objLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 102, Short.MAX_VALUE)
                            .addComponent(typeLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 102, Short.MAX_VALUE)
                            .addComponent(sizeLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 102, Short.MAX_VALUE)))
                    .addComponent(clearBtn))
                .addContainerGap())
        );
        infoPanelLayout.setVerticalGroup(
            infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(infoPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(typeLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(objLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(sizeLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, 35, Short.MAX_VALUE)
                .addComponent(clearBtn)
                .addContainerGap())
        );

        cachePanel.add(infoPanel, java.awt.BorderLayout.CENTER);

        splitter.setRightComponent(cachePanel);

        add(splitter, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

	private void cacheListValueChanged( javax.swing.event.ListSelectionEvent evt ) {//GEN-FIRST:event_cacheListValueChanged
            updateCachePanel();
	}//GEN-LAST:event_cacheListValueChanged

	private void clearBtnActionPerformed( java.awt.event.ActionEvent evt ) {//GEN-FIRST:event_clearBtnActionPerformed
            CacheMetrics cm = (CacheMetrics) cacheList.getSelectedValue();
            if (cm != null && cm.isClearSupported()) {
                cm.clear();
            }
	}//GEN-LAST:event_clearBtnActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList cacheList;
    private javax.swing.JLabel cacheNameLabel;
    private javax.swing.JPanel cachePanel;
    private javax.swing.JButton clearBtn;
    private javax.swing.JPanel infoPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane listScroll;
    private javax.swing.JLabel objLabel;
    private javax.swing.JLabel sizeLabel;
    private javax.swing.JSplitPane splitter;
    private javax.swing.JLabel typeLabel;
    // End of variables declaration//GEN-END:variables
}
