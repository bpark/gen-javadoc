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

import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.AsyncResult;
import org.github.bpark.config.AbstractDocConfig;
import org.github.bpark.generator.JavadocGenerator;
import org.jetbrains.annotations.NotNull;

/**
 * Data class for holding template and configuration data during the execution of the configuration dialog.
 *
 * @author Burt Parkers
 */
public class Template {

    /** The current edited content. */
    private String content;

    /** The configuration. */
    private AbstractDocConfig abstractDocConfig;

    /**
     * Constructor.
     *
     * @param abstractDocConfig the configuration.
     */
    public Template(@NotNull AbstractDocConfig abstractDocConfig) {
        this.abstractDocConfig = abstractDocConfig;
    }

    /**
     * Returns the configuration.
     *
     * @return the configuration.
     */
    @NotNull
    public AbstractDocConfig getAbstractDocConfig() {
        return abstractDocConfig;
    }

    /**
     * Returns the content.
     *
     * @return the content.
     */
    @NotNull
    public String getContent() {
        return content;
    }

    /**
     * Setter for the content.
     *
     * @param content the content.
     */
    public void setContent(@NotNull String content) {
        this.content = content;
    }

    /** Calls the configs save method. */
    public void save() {
        abstractDocConfig.save(content);
    }

    /** Reinitialization of the content with the template. */
    public void reset() {
        content = abstractDocConfig.getTemplate();
    }

    /**
     * Validates the data.
     *
     * @return true if the content is valid, otherwise false.
     */
    public boolean validate() {
        AsyncResult<DataContext> dataContextFromFocus = DataManager.getInstance().getDataContextFromFocus();
        Project project = DataKeys.PROJECT.getData(dataContextFromFocus.getResult());
        boolean ret = false;
        if (project != null) {
            JavadocGenerator generator = new JavadocGenerator(project, false);
            ret = generator.validateTemplate(content);
        }
        return ret;
    }
}
