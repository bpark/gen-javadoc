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

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.ide.util.PropertyName;
import org.jetbrains.annotations.NotNull;

/**
 * Specific configuration class for setter javadoc.
 *
 * @author Burt Parkers
 */
public class SetterDocConfig extends AbstractDocConfig {

    /** The default template. */
    private static final String DEFAULT_TEMPLATE =
            "/**\n" +
             " * Setter for field $field.\n" +
             " *\n" +
             " * @param $field the value to set for the field.\n" +
             " */";

    /** The template value and configuration location. */
    @PropertyName(value = "gendoc.setter.template")
    private String template;


    /** Constructor, loads the saved template or uses the default value. */
    public SetterDocConfig() {
        try {
            PropertiesComponent.getInstance().loadFields(this);
            if (template == null || template.isEmpty()) {
                template = DEFAULT_TEMPLATE;
            }
        } catch (IllegalAccessException e) {
            template = DEFAULT_TEMPLATE;
        }
    }

    @NotNull
    @Override
    public String getTemplate() {
        return template;
    }

    @NotNull
    @Override
    public String getConfigName() {
        return "Setter";
    }

    @Override
    public void save(@NotNull String template) {
        try {
            this.template = template;
            PropertiesComponent.getInstance().saveFields(this);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    @Override
    public String getDefaultTemplate() {
        return DEFAULT_TEMPLATE;
    }

    @Override
    public void setTemplate(@NotNull String template) {
        this.template = template;
    }
}
