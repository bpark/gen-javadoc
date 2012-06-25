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

import com.intellij.codeInsight.generation.PsiElementClassMember;
import com.intellij.codeInsight.generation.PsiMethodMember;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Util class for handling all the PSI stuff.
 *
 * @author Burt Parkers
 */
public final class GenDocPsiUtil {

    /**  Prevents util class instantiation. */
    private GenDocPsiUtil() {
    }

    /**
     * Filters a list of methods. The filter criteria is defined inside {@linkplain MethodType}.
     *
     * @param psiClass the class containing the methods.
     * @param methodType the filter criteria.
     * @return the filtered method list.
     */
    @NotNull
    public static List<PsiMethod> retrieveMethods(@NotNull PsiClass psiClass, @NotNull MethodType methodType) {

        List<PsiMethod> filteredMethodList = new ArrayList<PsiMethod>();

        PsiElement[] psiClassChildren = psiClass.getChildren();
        List<PsiMethod> methodList = extractPsiMethods(psiClassChildren);
        for (final PsiMethod psiMethod : methodList) {
            boolean isMethodType = methodType.isType(psiMethod);
            if (isMethodType && !containsOverride(psiMethod)) {
                filteredMethodList.add(psiMethod);
            }
        }
        return filteredMethodList;
    }

    /**
     * Converts a list of {@linkplain PsiMethod} to {@linkplain PsiMethodMember}.
     *
     * @param methodList the list to convert.
     * @return the converted list.
     */
    @NotNull
    public static PsiMethodMember[] convertPsiMethodsToMembers(@NotNull List<PsiMethod> methodList) {
        final PsiMethodMember[] dialogMembers = new PsiMethodMember[methodList.size()];
        for (int i = 0; i < dialogMembers.length; i++) {
            dialogMembers[i] = new PsiMethodMember(methodList.get(i));
        }
        return dialogMembers;
    }

    /**
     * Converts a list of {@linkplain PsiMethodMember} to {@linkplain PsiMethod}.
     *
     * @param classMemberList the list to convert.
     * @return the converted list.
     */
    @NotNull
    public static List<PsiMethod> convertMembersToPsiMethods(@NotNull List<PsiElementClassMember> classMemberList) {
        List<PsiMethod> psiMethodList = new ArrayList<PsiMethod>();
        for (PsiElementClassMember psiElementClassMember : classMemberList) {
            PsiElement psiElement = psiElementClassMember.getPsiElement();
            psiMethodList.add((PsiMethod) psiElement);
        }
        return psiMethodList;
    }

    /**
     * Filters a list of methods, independent from the class.
     *
     * @param methodList the list to filter.
     * @param methodType the filter criteria.
     * @return the filtered list.
     */
    @NotNull
    public static List<PsiMethod> filter(@NotNull List<PsiMethod> methodList, @NotNull MethodType methodType) {
        List<PsiMethod> filteredList = new ArrayList<PsiMethod>();
        for (PsiMethod psiMethod : methodList) {
            if (methodType.isType(psiMethod) && !containsOverride(psiMethod)) {
                filteredList.add(psiMethod);
            }
        }
        return filteredList;
    }

    /**
     * Returns the current class.
     *
     * @param editor the editor.
     * @param dataContext the datacontext.
     * @return the current class or null.
     */
    @Nullable
    public static PsiClass getSubjectClass(Editor editor, DataContext dataContext) {
        PsiFile file = LangDataKeys.PSI_FILE.getData(dataContext);
        if (file == null) {
            return null;
        }

        int offset = editor.getCaretModel().getOffset();
        PsiElement context = file.findElementAt(offset);

        if (context == null) {
            return null;
        }

        PsiClass clazz = PsiTreeUtil.getParentOfType(context, PsiClass.class, false);
        if (clazz == null) {
            return null;
        }

        return clazz.isInterface() ? null : clazz;
    }

    /**
     * Checks if a method has a override annotation.
     *
     * @param psiMethod the method to check.
     * @return true if the method has a override annotation, otherwise false.
     */
    public static boolean containsOverride(PsiMethod psiMethod) {
        boolean override = false;
        PsiElement firstChild = psiMethod.getFirstChild();
        if (firstChild != null) {
            PsiElement[] psiElements = firstChild.getChildren();
            for (PsiElement psiElement : psiElements) {
                if (psiElement instanceof PsiAnnotation) {
                    PsiAnnotation psiAnnotation = (PsiAnnotation) psiElement;
                    override = Override.class.getName().equals(psiAnnotation.getQualifiedName());
                }
            }
        }
        return override;
    }

    /**
     * Extracts all psi methods from a list of psi elements.
     *
     * @param psiElements the psi element list.
     * @return all methods.
     */
    @NotNull
    private static List<PsiMethod> extractPsiMethods(@NotNull PsiElement[] psiElements) {
        List<PsiMethod> methodList = new ArrayList<PsiMethod>();
        for (PsiElement psiElement : psiElements) {
            if (psiElement instanceof PsiMethod) {
                methodList.add((PsiMethod) psiElement);
            }
        }
        return methodList;
    }

}
