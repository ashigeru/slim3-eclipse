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

import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;

import com.ashigeru.slim3.eclipse.internal.core.LogUtil;

/**
 * 問題作成時に利用するクラスライブラリのコンテナ。
 * @version 2010
 */
public class Slim3ClasspathContainer implements IClasspathContainer {

    /**
     * このコンテナの単純名。
     */
    public static final String NAME = "Slim3"; //$NON-NLS-1$

    /**
     * このコンテナへのパス。
     */
    public static final IPath PATH = Path
        .fromPortableString(Slim3ClasspathContainerInitializer.FIRST_SEGMENT)
        .append(NAME);

    /**
     * このコンテナの表示名。
     */
    private static final String DISPLAY_NAME = "Slim3";

    private List<IClasspathEntry> classpathEntries;

    private String displayName;

    /**
     * インスタンスを生成する。
     * @param library 対象のプロジェクト
     * @throws CoreException インスタンスの生成に失敗した場合
     */
    public Slim3ClasspathContainer(Slim3Library library) throws CoreException {
        if (library == null) {
            throw new IllegalArgumentException("library is null"); //$NON-NLS-1$
        }
        List<IClasspathEntry> entries = library.getClasspathEntries();
        this.classpathEntries = Collections.unmodifiableList(entries);
        this.displayName = buildDisplayName(library);
    }

    /**
     * 指定のプロジェクトにこのコンテナが追加されている場合のみ{@code true}を返す。
     * @param project 対象のプロジェクト
     * @return 指定のプロジェクトにこのコンテナが追加されている場合に{@code true}、
     *     そうでない場合は{@code false}
     * @throws IllegalArgumentException 引数に{@code null}が含まれる場合
     */
    public static boolean isAdded(IProject project) {
        if (project == null) {
            throw new IllegalArgumentException("project is null"); //$NON-NLS-1$
        }
        try {
            IJavaProject javaProject = toJavaProject(project);
            if (javaProject == null) {
                return false;
            }
            IClasspathEntry[] classpath = javaProject.getRawClasspath();
            return index(classpath) >= 0;
        }
        catch (JavaModelException e) {
            LogUtil.log(e);
            return false;
        }
    }

    private static IJavaProject toJavaProject(IProject project) {
        assert project != null;
        try {
            if (project.hasNature(JavaCore.NATURE_ID)) {
                return JavaCore.create(project);
            }
        }
        catch (CoreException e) {
            LogUtil.log(e);
            return null;
        }
        return null;
    }

    /**
     * 指定のプロジェクトにこのコンテナを追加する。
     * <p>
     * すでに指定のプロジェクトにこのコンテナが追加されている場合、この呼び出しはなにも行わない。
     * </p>
     * @param monitor 進捗モニタ
     * @param project コンテナを追加する対象のプロジェクト
     * @throws JavaModelException コンテナの追加に失敗した場合
     * @throws IllegalArgumentException 引数に{@code null}が含まれる場合
     */
    public static void add(
            IProgressMonitor monitor,
            IProject project) throws JavaModelException {
        if (monitor == null) {
            throw new IllegalArgumentException("monitor is null"); //$NON-NLS-1$
        }
        if (project == null) {
            throw new IllegalArgumentException("project is null"); //$NON-NLS-1$
        }
        monitor.beginTask("Adding Slim3 Classpath Container", 2);
        try {
            IJavaProject javaProject = toJavaProject(project);
            if (javaProject == null) {
                return;
            }
            IClasspathEntry[] classpath = javaProject.readRawClasspath();
            if (index(classpath) >= 0) {
                return;
            }
            IClasspathEntry[] newClasspath =
                    new IClasspathEntry[classpath.length + 1];
            System.arraycopy(classpath, 0, newClasspath, 0, classpath.length);
            newClasspath[classpath.length] =
                    JavaCore.newContainerEntry(Slim3ClasspathContainer.PATH);
            monitor.worked(1);

            javaProject.setRawClasspath(
                newClasspath,
                new SubProgressMonitor(monitor, 1));
        }
        finally {
            monitor.done();
        }
    }

    /**
     * 指定のプロジェクトからこのコンテナを削除する。
     * <p>
     * 指定のプロジェクトにこのコンテナが追加されていない場合、この呼び出しはなにも行わない。
     * </p>
     * @param monitor 進捗モニタ
     * @param project コンテナを削除する対象のプロジェクト
     * @throws JavaModelException コンテナの削除に失敗した場合
     * @throws IllegalArgumentException 引数に{@code null}が含まれる場合
     */
    public static void remove(
            IProgressMonitor monitor,
            IProject project) throws JavaModelException {
        if (monitor == null) {
            throw new IllegalArgumentException("monitor is null"); //$NON-NLS-1$
        }
        if (project == null) {
            throw new IllegalArgumentException("project is null"); //$NON-NLS-1$
        }
        monitor.beginTask("Removing Slim3 Classpath Container", 2);
        try {
            IJavaProject javaProject = toJavaProject(project);
            if (javaProject == null) {
                return;
            }
            IClasspathEntry[] classpath = javaProject.readRawClasspath();
            int index = index(classpath);
            if (index < 0) {
                return;
            }
            IClasspathEntry[] newClasspath = new IClasspathEntry[classpath.length - 1];
            if (index != 0) {
                System.arraycopy(
                    classpath, 0,
                    newClasspath, 0,
                    index);
            }
            if (index != classpath.length - 1) {
                System.arraycopy(
                    classpath, index + 1,
                    newClasspath, index,
                    classpath.length - 1 - index);
            }
            monitor.worked(1);

            javaProject.setRawClasspath(
                newClasspath,
                new SubProgressMonitor(monitor, 1));
        }
        finally {
            monitor.done();
        }
    }

    private static int index(IClasspathEntry[] classpath) {
        assert classpath != null;
        for (int i = 0; i < classpath.length; i++) {
            IClasspathEntry entry = classpath[i];
            if (entry.getEntryKind() != IClasspathEntry.CPE_CONTAINER) {
                continue;
            }
            if (entry.getPath().equals(Slim3ClasspathContainer.PATH) == false) {
                continue;
            }
            return i;
        }
        return -1;
    }

    @Override
    public int getKind() {
        return IClasspathContainer.K_APPLICATION;
    }

    @Override
    public IPath getPath() {
        return PATH;
    }

    @Override
    public String getDescription() {
        return displayName;
    }

    @Override
    public IClasspathEntry[] getClasspathEntries() {
        return classpathEntries.toArray(
            new IClasspathEntry[classpathEntries.size()]);
    }

    private String buildDisplayName(Slim3Library library) {
        assert library != null;
        return MessageFormat.format(
            "{0} [{1}]", //$NON-NLS-1$
            DISPLAY_NAME,
            library.getVersion());
    }
}
