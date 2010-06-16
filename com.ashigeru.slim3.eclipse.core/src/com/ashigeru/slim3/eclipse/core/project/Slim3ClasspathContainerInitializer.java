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
package com.ashigeru.slim3.eclipse.core.project;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ClasspathContainerInitializer;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;

import com.ashigeru.slim3.eclipse.internal.core.Activator;

/**
 * {@link Slim3ClasspathContainer}を初期化する。
 */
public class Slim3ClasspathContainerInitializer extends
        ClasspathContainerInitializer {

    /**
     * この初期化が対象とするパスの最初のセグメント名。
     */
    public static final String FIRST_SEGMENT =
        Activator.PLUGIN_ID + ".coreLibraries"; //$NON-NLS-1$


    @Override
    public void initialize(IPath containerPath, IJavaProject project)
            throws CoreException {
        if (containerPath.segment(0).equals(FIRST_SEGMENT) == false) {
            return;
        }
        if (Slim3Nature.isAdded(project.getProject()) == false) {
            return;
        }
        Slim3Nature slim3 = Slim3Nature.create(project.getProject());
        Slim3Library library = slim3.getLibrary();
        if (library == null) {
            library = new UnknownSlim3Library("Not Specified");
        }
        JavaCore.setClasspathContainer(
            Slim3ClasspathContainer.PATH,
            new IJavaProject[] { project },
            new IClasspathContainer[] {
                    new Slim3ClasspathContainer(library),
            },
            new NullProgressMonitor());
    }
}
