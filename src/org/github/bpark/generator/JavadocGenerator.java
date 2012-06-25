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

package org.github.bpark.generator;

import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.util.PropertyUtil;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.github.bpark.config.AbstractDocConfig;
import org.jetbrains.annotations.NotNull;

import java.io.StringWriter;
import java.util.List;

/**
 * Generator for processing the templates and generating the javadoc comments.
 *
 * @author Burt Parkers
 */
public class JavadocGenerator {

    /** The project. */
    private Project project;

    /** True for overwriting existing comments, false otherwise. */
    private boolean overwrite;


    /**
     * Constructor.
     *
     * @param project the project.
     * @param overwrite overwrite flag, true if existing comments should be overwritten, otherwise false.
     */
    public JavadocGenerator(@NotNull Project project, boolean overwrite) {
        this.project = project;
        this.overwrite = overwrite;
    }

    /**
     * Generates the javadoc comment for a given method list. Depending of the overwrite configuration the javadoc
     * comments are generated or not. Overridden annotated methods are ignored.
     *
     * @param psiClass the class.
     * @param psiMethodLists the methods for the javadoc generation.
     * @param abstractDocConfig the configuration.
     */
    public void generate(@NotNull PsiClass psiClass, @NotNull List<PsiMethod> psiMethodLists, @NotNull AbstractDocConfig abstractDocConfig) {
        for (final PsiMethod psiMethod : psiMethodLists) {

            final PsiElement firstChild = psiMethod.getFirstChild();

            final boolean hasComment = firstChild instanceof PsiDocComment;

            boolean enterIfHasComment = !hasComment || overwrite;

            if (project != null && firstChild != null && enterIfHasComment) {

                PsiElementFactory psiElementFactory = JavaPsiFacade.getInstance(project).getElementFactory();

                String template = abstractDocConfig.getTemplate();

                String commentText = processTemplate(psiClass, psiMethod, template);

                final PsiDocComment docCommentFromText = psiElementFactory.createDocCommentFromText(commentText);
                Application application = ApplicationManager.getApplication();
                application.runWriteAction(new Runnable() {
                    public void run() {
                        if (hasComment) {
                            firstChild.replace(docCommentFromText);
                        } else {
                            psiMethod.addBefore(docCommentFromText, psiMethod.getFirstChild());
                        }
                        CodeStyleManager.getInstance(project).reformat(psiMethod);
                    }
                });
            }
        }
    }

    /**
     * Method for processing the template with velocity.
     *
     * @param psiClass the class.
     * @param psiMethod the method.
     * @param template the template.
     * @return the final javadoc string.
     */
    @NotNull
    private String processTemplate(@NotNull PsiClass psiClass, @NotNull PsiMethod psiMethod, @NotNull String template) {
        VelocityContext velocityContext = new VelocityContext();

        String propertyName = PropertyUtil.getPropertyName(psiMethod);
        velocityContext.put("field", propertyName);
        velocityContext.put("field_type", PropertyUtil.getPropertyType(psiMethod));

        velocityContext.put("class_name", psiClass.getName());
        velocityContext.put("full_class_name", psiClass.getQualifiedName());
        velocityContext.put("project_name", project.getName());

        VelocityEngine velocity = VelocityEngineFactory.getVelocityEngine();

        StringWriter stringWriter = new StringWriter();

        velocity.evaluate(velocityContext, stringWriter, getClass().getName(), template);

        return stringWriter.getBuffer().toString();
    }

    /**
     * Validates a given template.
     *
     * @param template the template.
     * @return true if the template is valid, otherwise false.
     */
    public boolean validateTemplate(@NotNull String template) {
        try {
            VelocityContext velocityContext = new VelocityContext();

            String propertyName = "testName";
            velocityContext.put("field", propertyName);
            velocityContext.put("field_type", "int");

            velocityContext.put("class_name", "MyClass");
            velocityContext.put("full_class_name", "com.test.MyClass");
            velocityContext.put("project_name", "test-project");

            VelocityEngine velocity = VelocityEngineFactory.getVelocityEngine();

            StringWriter stringWriter = new StringWriter();

            velocity.evaluate(velocityContext, stringWriter, getClass().getName(), template);

            PsiElementFactory psiElementFactory = JavaPsiFacade.getInstance(project).getElementFactory();

            String commentText = stringWriter.getBuffer().toString();

            psiElementFactory.createDocCommentFromText(commentText);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
