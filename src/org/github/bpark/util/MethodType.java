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

package org.github.bpark.util;

import com.intellij.psi.PsiMethod;
import com.intellij.psi.util.PropertyUtil;

/**
 * Enumeration for method types.
 *
 * @author Burt Parkers
 */
public enum MethodType {

    /** Constant for setter. */
    SETTER {
        boolean isType(PsiMethod psiMethod) {
            return PropertyUtil.isSimplePropertySetter(psiMethod);
        }
    },

    /** Constant for getter. */
    GETTER {
        boolean isType(PsiMethod psiMethod) {
            return PropertyUtil.isSimplePropertyGetter(psiMethod);
        }
    },

    /** Constant for setter and getter. */
    SETTER_AND_GETTER {
        boolean isType(PsiMethod psiMethod) {
            return PropertyUtil.isSimplePropertySetter(psiMethod) || PropertyUtil.isSimplePropertyGetter(psiMethod);
        }
    };

    /**
     * Method for checking a methods type.
     *
     * @param psiMethod the method to check.
     * @return true if the method is of the currents enum type.
     */
    abstract boolean isType(PsiMethod psiMethod);

}
