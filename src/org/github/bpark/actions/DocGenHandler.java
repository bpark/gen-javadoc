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

package org.github.bpark.actions;

import com.intellij.codeInsight.generation.PsiMethodMember;
import com.intellij.ide.util.MemberChooser;
import com.intellij.ide.util.MemberChooserBuilder;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorWriteActionHandler;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import org.github.bpark.config.GetterDocConfig;
import org.github.bpark.config.SetterDocConfig;
import org.github.bpark.generator.JavadocGenerator;
import org.github.bpark.util.GenDocPsiUtil;
import org.github.bpark.util.MethodType;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Handler for all generating actions. Default method type is {@linkplain MethodType#SETTER_AND_GETTER}, and no
 * overwrite existing documentation.
 *
 * @author Burt Parkers
 */
public class DocGenHandler extends EditorWriteActionHandler {

    /** Default type. */
    private MethodType methodType = MethodType.SETTER_AND_GETTER;

    /** Flag for overwrite existing documentation configuration. */
    private boolean overwrite;

    /** Default constructor with default settings. */
    public DocGenHandler() {
    }

    /** Constructor with MethodType Parameter. */
    public DocGenHandler(MethodType methodType) {
        this.methodType = methodType;
    }

    @Override
    public void executeWriteAction(Editor editor, DataContext dataContext) {
        final Project project = LangDataKeys.PROJECT.getData(dataContext);
        assert project != null;

        PsiClass psiClass = GenDocPsiUtil.getSubjectClass(editor, dataContext);
        assert psiClass != null;

        showDialog(project, psiClass);
    }

    /**
     * This method creates and shows the method selection dialog.
     *
     * @param project the project.
     * @param psiClass the class.
     */
    private void showDialog(@NotNull final Project project, @NotNull final PsiClass psiClass) {
        final MemberChooserBuilder<PsiMethodMember> builder = new MemberChooserBuilder<PsiMethodMember>(project);
        builder.setTitle("Generate JavaDoc");
        builder.setHeaderPanel(createHeader());
        List<PsiMethod> methodList = GenDocPsiUtil.retrieveMethods(psiClass, methodType);
        final PsiMethodMember[] dialogMembers = GenDocPsiUtil.convertPsiMethodsToMembers(methodList);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                if (project.isDisposed()) return;
                final MemberChooser dialog = builder.createBuilder(dialogMembers);
                dialog.selectElements(dialogMembers);
                dialog.show();

                if (MemberChooser.OK_EXIT_CODE == dialog.getExitCode()) {

                    List<PsiMethod> selectedMethodList = GenDocPsiUtil.convertMembersToPsiMethods(dialog.getSelectedElements());
                    JavadocGenerator generator = new JavadocGenerator(project, overwrite);

                    List<PsiMethod> getterList = GenDocPsiUtil.filter(selectedMethodList, MethodType.GETTER);
                    generator.generate(psiClass, getterList, new GetterDocConfig());

                    List<PsiMethod> setterList = GenDocPsiUtil.filter(selectedMethodList, MethodType.SETTER);
                    generator.generate(psiClass, setterList, new SetterDocConfig());

                }
            }
        });
    }

    @Override
    public boolean isEnabled(Editor editor, DataContext dataContext) {
        return GenDocPsiUtil.getSubjectClass(editor, dataContext) != null;
    }

    /**
     * Creates the dialog header. Needed for overwrite settings.
     *
     * @return the panel containing the header.
     */
    private JPanel createHeader() {
        FlowLayout layout = new FlowLayout(FlowLayout.LEFT);
        JPanel mainPanel = new JPanel(layout);
        final JCheckBox checkBox = new JCheckBox();
        mainPanel.add(checkBox);
        mainPanel.add(new JLabel("Overwrite existing documentation"));
        checkBox.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                overwrite = checkBox.getModel().isSelected();
            }
        });
        return mainPanel;
    }

}
