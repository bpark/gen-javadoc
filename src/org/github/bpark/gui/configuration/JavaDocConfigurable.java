/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * This file is part of gen-javadoc.
 *
 * Copyright (c) 2011 by individual contributors as indicated by the
 * @author tags. See the copyright.txt file in the distribution
 * for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.github.bpark.gui.configuration;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import org.github.bpark.config.GetterDocConfig;
import org.github.bpark.config.SetterDocConfig;
import org.jetbrains.annotations.Nls;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.util.HashMap;
import java.util.Map;

/**
 * Configuration dialog for editing templates.
 *
 * @author Burt Parkers
 */
public class JavaDocConfigurable implements Configurable {

    /** The main panel. */
    private JPanel mainPanel;
    /** JList with the specific configuration names. */
    private JList configJList;
    /** Area for template manipulation. */
    private JTextArea templateArea;
    /** Area for descriptions. */
    private JTextArea descriptionArea;
    /** Template map, holds an index as key and a template instance as value. */
    private Map<Integer, Template> templateMap = new HashMap<Integer, Template>() {{
        put(0, new Template(new GetterDocConfig()));
        put(1, new Template(new SetterDocConfig()));
    }};

    /** Returns the display name. */
    @Nls
    public String getDisplayName() {
        return "JavaDoc Generator";
    }

    /**
     * Returns the icon, currently null.
     *
     * @return null.
     */
    public Icon getIcon() {
        return null;
    }

    /**
     * Returns the help topic.
     *
     * @return null.
     */
    public String getHelpTopic() {
        return null;
    }

    /**
     * Returns the configuration panel. Handles the selection of each template configuration.
     *
     * @return the configuration panel.
     */
    public JComponent createComponent() {
        //initGui();
        configJList.setListData(new String[] {
            templateMap.get(0).getAbstractDocConfig().getConfigName(),
            templateMap.get(1).getAbstractDocConfig().getConfigName()
        });
        descriptionArea.setText("Chult is located on an island in the southern part of the Trackless Sea, off the coast of Calimshan.[1] Formerly, the land was located at the westernmost end of the Chultan peninsula. It is a mountainous jungle of savage beasts, hulking dinosaurs, and disease-ridden swamps. Savage human tribes, goblins, and even stranger monstrous folk haunt the thick jungles. Nevertheless, Chult draws adventurers who search for its legendary riches. The primordial Ubtao is almost exclusively revered in the land, for the divine powers of Faerûn awarded Ubtao the dominion over the land of Chult in exchange for the deity's vigilance over the threat from under the Peaks of Flame. ");
        configJList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    int selectedIndex = configJList.getSelectedIndex();
                    if (selectedIndex >= 0 && selectedIndex < templateMap.size()) {
                        templateMap.get(1 - selectedIndex).setContent(templateArea.getText());
                        String content = templateMap.get(selectedIndex).getContent();
                        templateArea.setText(content);
                    }
                }
            }
        });
        select(0);
        return mainPanel;
    }

    /**
     * Returns the modified status.
     *
     * @return true.
     */
    public boolean isModified() {
        return true;
    }

    /**
     * Applies the configuration changes.
     *
     * @throws ConfigurationException if one of the templates is invalid.
     */
    public void apply() throws ConfigurationException {
        int selectedIndex = configJList.getSelectedIndex();
        templateMap.get(selectedIndex).setContent(templateArea.getText());
        for (Template template : templateMap.values()) {
            if (template.validate()) {
                template.save();
            } else {
                String configName = template.getAbstractDocConfig().getConfigName();
                throw new ConfigurationException("Template for " + configName + " is invalid!", "Template Error");
            }
        }
    }

    /**
     * Resets the configuration.
     */
    public void reset() {
        for (Template template : templateMap.values()) {
            template.reset();
        }
        select(0);
    }

    /**
     * Selects a specific list item.
     *
     * @param index index of the list item.
     */
    private void select(int index) {
        configJList.setSelectedIndex(index);
        templateArea.setText(templateMap.get(index).getAbstractDocConfig().getTemplate());
    }

    /** Not implemented. */
    public void disposeUIResources() {
    }


}
