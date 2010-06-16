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

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.dialogs.PropertyPage;
import org.eclipse.ui.preferences.ScopedPreferenceStore;

import com.ashigeru.slim3.eclipse.internal.ui.Activator;

/**
 * Property page for Slim3.
 * @author ashigeru
 */
public class Slim3PropertyPage extends PropertyPage {

    @Override
    protected IPreferenceStore doGetPreferenceStore() {
        IProject project = (IProject) getElement().getAdapter(IProject.class);
        ProjectScope scope = new ProjectScope(project);
        ScopedPreferenceStore store = new ScopedPreferenceStore(
            scope,
            Activator.PLUGIN_ID);
        return store;
    }

	@Override
    protected Control createContents(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		composite.setLayout(layout);

		return composite;
	}

	@Override
    protected void performDefaults() {
	    // TODO
	}

	@Override
	protected void performApply() {
	    // TODO performApply 2010/06/17 0:32:42
	    super.performApply();
	}

	@Override
    public boolean performOk() {
		// TODO
		return true;
	}

}