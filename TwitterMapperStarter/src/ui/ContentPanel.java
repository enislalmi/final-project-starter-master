package ui;

import org.openstreetmap.gui.jmapviewer.JMapViewer;
import query.Query;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ContentPanel extends JPanel {
    private JSplitPane topLevelSplitPane;
    private JSplitPane querySplitPane;
    private JPanel newQueryPanel;
    private JPanel existingQueryList;
    private JMapViewer map;

    private Application app;

    public ContentPanel(Application app) {
        this.app = app;

        map = new JMapViewer();
        map.setMinimumSize(new Dimension(100, 50));
        setLayout(new BorderLayout());
        newQueryPanel = new NewQueryPanel(app);


        JPanel layerPanelContainer = new JPanel();
        existingQueryList = new JPanel();
        existingQueryList.setLayout(new javax.swing.BoxLayout(existingQueryList, javax.swing.BoxLayout.Y_AXIS));
        layerPanelContainer.setLayout(new BorderLayout());
        layerPanelContainer.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createTitledBorder("Current Queries"),
                        BorderFactory.createEmptyBorder(5,5,5,5)));
        layerPanelContainer.add(existingQueryList, BorderLayout.NORTH);

        querySplitPane = new JSplitPane(0);
        querySplitPane.setDividerLocation(150);
        querySplitPane.setTopComponent(newQueryPanel);
        querySplitPane.setBottomComponent(layerPanelContainer);

        topLevelSplitPane = new JSplitPane(1);
        topLevelSplitPane.setDividerLocation(150);
        topLevelSplitPane.setLeftComponent(querySplitPane);
        topLevelSplitPane.setRightComponent(map);

        add(topLevelSplitPane, "Center");
        revalidate();

        repaint();
    }


    public void addQuery(Query query) {
        JPanel newQueryPanel = new JPanel();
        newQueryPanel.setLayout(new GridBagLayout());
        JPanel colorPanel = new JPanel();
        colorPanel.setBackground(query.getColor());
        colorPanel.setPreferredSize(new Dimension(30, 30));
        JButton removeButton = new JButton("X");
        removeButton.setPreferredSize(new Dimension(30, 20));
        //action listener replaced with lambda
        removeButton.addActionListener(e -> {
            app.terminateQuery(query);
            query.terminate();
            existingQueryList.remove(newQueryPanel);
            revalidate();
        });

        GridBagConstraints constraints = new GridBagConstraints();
        newQueryPanel.add(colorPanel, constraints);

        constraints = new GridBagConstraints();
        JCheckBox checkbox = new JCheckBox(query.getQueryString());
        checkbox.setSelected(true);
        //action listener replaced with lambda
        checkbox.addActionListener(e -> app.updateVisibility());
        query.setCheckBox(checkbox);
        constraints.weightx = 1.0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        newQueryPanel.add(checkbox, constraints);
        newQueryPanel.add(removeButton);

        existingQueryList.add(newQueryPanel);
        validate();
    }

    public JMapViewer getViewer() {
        return map;
    }



    public void addQueryToContentPanel(Query query) {
        JPanel newQueryPanel = new JPanel();
        newQueryPanel.setLayout(new GridBagLayout());
        JPanel colorPanel = createColorPanel(query);
        JButton removeButton = createRemoveButton(query, newQueryPanel);
        JCheckBox checkbox = createCheckBox(query);
        GridBagConstraints c = createGridBagConstraints();

        newQueryPanel.add(colorPanel, c);
        newQueryPanel.add(checkbox, c);
        newQueryPanel.add(removeButton);

        existingQueryList.add(newQueryPanel);
        validate();
    }


    private GridBagConstraints createGridBagConstraints() {
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 1.0;
        c.fill = GridBagConstraints.HORIZONTAL;
        return c;
    }


    private JCheckBox createCheckBox(Query query) {
        JCheckBox checkbox = new JCheckBox(query.getQueryString());
        checkbox.setSelected(true);
        //action listener replaced with lambda
        checkbox.addActionListener(e -> app.updateVisibility());
        query.setCheckBox(checkbox);
        return checkbox;
    }


    private JPanel createColorPanel(Query query) {
        JPanel colorPanel = new JPanel();
        colorPanel.setBackground(query.getColor());
        colorPanel.setPreferredSize(new Dimension(30, 30));
        return colorPanel;
    }


    private JButton createRemoveButton(Query query, JPanel newQueryPanel) {
        JButton removeButton = new JButton("X");
        removeButton.setPreferredSize(new Dimension(30, 20));
        removeButton.addActionListener(e -> {
            app.terminateQuery(query);
            query.terminateQuery();
            existingQueryList.remove(newQueryPanel);
            revalidate();
        });
        return removeButton;
    }
}
