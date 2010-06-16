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
package com.ashigeru.slim3.eclipse.ui.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import com.ashigeru.slim3.eclipse.core.project.Slim3ClasspathContainer;

/**
 * Slim3のクラスライブラリを選択しているプロジェクトに追加する。
 */
public class AddClassLibraryHandler extends AbstractHandler {

    public Object execute(ExecutionEvent event) throws ExecutionException {
        final IProject project = ProjectHandlerUtil.getTargetProject(event);
        if (project == null) {
            return null;
        }
        WorkspaceJob job = new WorkspaceJob("Adding Slim3 Class Library") {
            @Override
            public IStatus runInWorkspace(IProgressMonitor monitor)
                    throws CoreException {
                Slim3ClasspathContainer.add(monitor, project);
                return Status.OK_STATUS;
            }
        };
        job.schedule();
        return null;
    }
}
