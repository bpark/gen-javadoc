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

package org.github.bpark.config;

import org.jetbrains.annotations.NotNull;

/**
 * Abstract superclass for configuration classes. Some properties are defined in both subclasses, because the
 * load and save setting mechanism ignores superclasses and the annotations are specific for subclasses.
 *
 * @author Burt Parkers
 */
public abstract class AbstractDocConfig {

    /**
     * Returns the template.
     *
     * @return the template.
     */
    @NotNull
    public abstract String getTemplate();

    /**
     * Returns the configuration name.
     *
     * @return the configuration name.
     */
    @NotNull
    public abstract String getConfigName();

    /**
     * Method for saving template changes. The config location is defined in the subclass.
     *
     * @param template the template content to save.
     */
    public abstract void save(@NotNull String template);

    /**
     * Returns the default template.
     *
     * @return the default template
     */
    @NotNull
    public abstract String getDefaultTemplate();

    /**
     * Sets the template. Does not store, only overwrites the current template.
     *
     * @param template the template content.
     */
    public abstract void setTemplate(@NotNull String template);

}
