/*
 * Copyright 2010 @ashigeru.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package com.ashigeru.slim3.eclipse.ui.properties;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.PropertyPage;
import org.eclipse.ui.progress.IProgressService;

import com.ashigeru.slim3.eclipse.core.Slim3EclipseCore;
import com.ashigeru.slim3.eclipse.core.project.Slim3Library;
import com.ashigeru.slim3.eclipse.core.project.Slim3ProjectProperties;
import com.ashigeru.slim3.eclipse.internal.ui.LogUtil;

/**
 * Property page for Slim3.
 * @author ashigeru
 */
public class Slim3PropertyPage extends PropertyPage {

    private static final int INDENT_WIDTH = 10;

    List<Slim3Library> availableLibraries;

    Button enableSlim3Button;

    Label versionLabel;

    Combo versionList;

    Button useClassLibraryButton;

    IProject getTarget() {
        IProject project = (IProject) getElement().getAdapter(IProject.class);
        Assert.isNotNull(project);
        return project;
    }

    @Override
    protected Control createContents(Composite root) {
        availableLibraries =
                new ArrayList<Slim3Library>(Slim3EclipseCore.getLibraries());
        Collections.reverse(availableLibraries);

        Composite parent = new Composite(root, SWT.NONE);
        GridLayoutFactory.swtDefaults()
            .numColumns(2)
            .applyTo(parent);
        GridDataFactory.fillDefaults()
            .grab(true, true)
            .applyTo(parent);

        enableSlim3Button = new Button(parent, SWT.CHECK);
        enableSlim3Button.setText("Enable Slim3");
        GridDataFactory.swtDefaults()
            .align(SWT.LEFT, SWT.CENTER)
            .span(2, 1)
            .applyTo(enableSlim3Button);

        Label separator = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
        GridDataFactory.swtDefaults()
            .align(SWT.FILL, SWT.BEGINNING)
            .grab(true, false)
            .span(2, 1)
            .applyTo(separator);

        versionLabel = new Label(parent, SWT.NULL);
        versionLabel.setText("Slim3 Version:");
        GridDataFactory.swtDefaults()
            .align(SWT.LEFT, SWT.CENTER)
            .indent(INDENT_WIDTH, 0)
            .applyTo(versionLabel);

        versionList = new Combo(parent, SWT.DROP_DOWN | SWT.READ_ONLY);
        for (Slim3Library library : availableLibraries) {
            versionList.add(library.getVersion());
        }
        if (availableLibraries.isEmpty() == false) {
            versionList.select(0);
        }
        GridDataFactory.swtDefaults()
            .align(SWT.LEFT, SWT.CENTER)
            .applyTo(versionList);

        useClassLibraryButton = new Button(parent, SWT.CHECK);
        useClassLibraryButton.setText("Use Slim3 Class Libraries");
        GridDataFactory.swtDefaults()
            .align(SWT.LEFT, SWT.CENTER)
            .indent(INDENT_WIDTH, 0)
            .span(2, 1)
            .applyTo(useClassLibraryButton);

        applyToFields(Slim3ProjectProperties.load(getTarget()));
        enableSlim3Button.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                onEnableChanged();
            }
        });
        versionList.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                onVersionChanged();
            }
        });
        useClassLibraryButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                onUserClassLibraryChanged();
            }
        });
        onEnableChanged();
        applyDialogFont(parent);
        return parent;
    }

    void onEnableChanged() {
        boolean enable = enableSlim3Button.getSelection();
        if (enable && availableLibraries.isEmpty()) {
            setErrorMessage("利用可能なライブラリがありません");
            setDetailPartEnabled(false);
        }
        else if (enable) {
            setErrorMessage(null);
            setDetailPartEnabled(true);
        }
        else {
            setErrorMessage(null);
            setDetailPartEnabled(false);
        }
    }

    private void setDetailPartEnabled(boolean enable) {
        versionLabel.setEnabled(enable);
        versionList.setEnabled(enable);
        useClassLibraryButton.setEnabled(enable);
    }

    void onVersionChanged() {
        // do nothing
    }

    void onUserClassLibraryChanged() {
        // do nothing
    }

    @Override
    protected void performDefaults() {
        Slim3ProjectProperties defaults = Slim3ProjectProperties
            .createDefaults();
        applyToFields(defaults);
    }

    @Override
    public boolean performOk() {
        Slim3ProjectProperties properties = collectFromFields();
        if (isModified(properties)) {
            properties.apply(getTarget());
            rebuild();
        }
        else {
            properties.apply(getTarget());
        }
        return true;
    }

    private boolean isModified(Slim3ProjectProperties properties) {
        assert properties != null;
        Slim3ProjectProperties origin = Slim3ProjectProperties
            .load(getTarget());
        return properties.equals(origin);
    }

    private void applyToFields(Slim3ProjectProperties properties) {
        assert properties != null;
        enableSlim3Button.setSelection(properties.isEnabled());
        String version = properties.getVersion();
        if (availableLibraries.isEmpty()) {
            versionList.select(0);
        }
        else {
            int index = version == null ? -1 : versionList.indexOf(version);
            if (index >= 0) {
                versionList.select(index);
            }
            else {
                versionList.select(0);
            }
        }
        useClassLibraryButton.setSelection(properties.isClassLibraryEnabled());
    }

    private Slim3ProjectProperties collectFromFields() {
        Slim3ProjectProperties properties = new Slim3ProjectProperties();
        properties.setEnabled(enableSlim3Button.getSelection());
        if (availableLibraries.isEmpty()) {
            properties.setVersion(null);
        }
        else {
            int selection = versionList.getSelectionIndex();
            properties.setVersion(availableLibraries.get(selection));
        }
        properties.setClassLibraryEnabled(useClassLibraryButton.getSelection());
        return properties;
    }

    private void rebuild() {
        try {
            IProgressService progress = PlatformUI.getWorkbench()
                .getProgressService();
            progress.run(true, true, new IRunnableWithProgress() {
                @Override
                public void run(IProgressMonitor m1)
                        throws InterruptedException {
                    try {
                        JavaCore.run(new IWorkspaceRunnable() {
                            @Override
                            public void run(IProgressMonitor m2)
                                    throws CoreException {
                                getTarget().build(
                                    IncrementalProjectBuilder.CLEAN_BUILD,
                                    m2);
                            }
                        }, m1);
                    }
                    catch (OperationCanceledException e) {
                        throw new InterruptedException();
                    }
                    catch (CoreException e) {
                        LogUtil.log(e);
                    }
                }
            });
        }
        catch (Exception e) {
            // フォークしているので通常は発生しないはず
            LogUtil.log(IStatus.ERROR, "Cannot schedule clean building", e);
        }
    }
}
