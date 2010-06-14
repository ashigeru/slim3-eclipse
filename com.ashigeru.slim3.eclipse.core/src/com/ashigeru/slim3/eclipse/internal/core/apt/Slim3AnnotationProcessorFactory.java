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
package com.ashigeru.slim3.eclipse.internal.core.apt;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

import com.ashigeru.slim3.eclipse.core.project.Slim3Library;
import com.ashigeru.slim3.eclipse.core.project.Slim3Nature;
import com.sun.mirror.apt.AnnotationProcessor;
import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.apt.AnnotationProcessorFactory;
import com.sun.mirror.apt.AnnotationProcessors;
import com.sun.mirror.declaration.AnnotationTypeDeclaration;

/**
 * Slim3に関連する自動生成の注釈プロセッサを作成する。
 * <p>
 * 現在のプロジェクトに対し、指定されたバージョンのものを利用する。
 * </p>
 * @author ashigeru
 */
public class Slim3AnnotationProcessorFactory implements
        AnnotationProcessorFactory {

    public Collection<String> supportedAnnotationTypes() {
        return Arrays.asList("org.slim3.datastore.Model");
    }

    public Collection<String> supportedOptions() {
        return Arrays.asList("debug");
    }

    public AnnotationProcessor getProcessorFor(
            Set<AnnotationTypeDeclaration> decls,
            AnnotationProcessorEnvironment ape) {

        String output = ape.getOptions().get("-s");
        if (output == null) {
            return AnnotationProcessors.NO_OP;
        }

        IPath path = Path.fromOSString(output);
        IProject project = getProjectFromPath(path);
        if (project == null) {
            return AnnotationProcessors.NO_OP;
        }

        if (Slim3Nature.isAdded(project) == false) {
            return AnnotationProcessors.NO_OP;
        }
        Slim3Nature slim3 = Slim3Nature.create(project);
        Slim3Library library = slim3.getLibrary();
        AnnotationProcessorFactory factory = library.createFactory();
        if (factory == null) {
            return AnnotationProcessors.NO_OP;
        }
        return factory.getProcessorFor(decls, ape);
    }

    private IProject getProjectFromPath(IPath path) {
        IWorkspaceRoot ws = ResourcesPlugin.getWorkspace().getRoot();
        for (IProject p : ws.getProjects()) {
            if (p.getLocation().isPrefixOf(path)) {
                return p;
            }
        }
        return null;
    }
}
