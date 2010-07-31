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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;

import com.ashigeru.slim3.eclipse.internal.core.Activator;

/**
 * Slim3の状態を表すネイチャ。
 * @author ashigeru
 */
public class Slim3Nature implements IProjectNature {

    /**
     * このネイチャのID。
     */
    public static final String ID = "com.ashigeru.slim3.nature"; //$NON-NLS-1$

    private static final String K_SLIM3_VERSION = "slim3.version"; //$NON-NLS-1$

    private IProject project;

    private IEclipsePreferences preferences;

    @Override
    public void configure() throws CoreException {
        return;
    }

    @Override
    public void deconfigure() throws CoreException {
        return;
    }

    @Override
    public IProject getProject() {
        return project;
    }

    @Override
    public void setProject(IProject project) {
        this.project = project;
    }

    /**
     * このプロジェクトで利用するライブラリの情報を返す。
     * @return このプロジェクトで利用するライブラリの情報、不正な場合や使用しない場合は{@code null}
     */
    public Slim3Library getLibrary() {
        String version = getLibraryVersion();
        if (version == null) {
            return null;
        }
        Map<String, Slim3Library> all = Activator.getDefault().getLibraries();
        Slim3Library library = all.get(version);
        if (library == null) {
            return null;
        }
        return library;
    }

    /**
     * このプロジェクトに指定されているライブラリのバージョン番号を返す。
     * @return このプロジェクトに指定されているライブラリのバージョン番号、未指定の場合は{@code null}
     */
    public String getLibraryVersion() {
        IEclipsePreferences prefs = getPreferences();
        return prefs.get(K_SLIM3_VERSION, null);
    }

    /**
     * このプロジェクトで利用するライブラリのバージョン番号を設定する。
     * @param version 設定するバージョン、指定しない場合は{@code null}
     */
    public void setLibraryVersion(String version) {
        IEclipsePreferences prefs = getPreferences();
        prefs.put(K_SLIM3_VERSION, version);
    }

    private synchronized IEclipsePreferences getPreferences() {
        if (preferences == null) {
            preferences = Activator.getDefault().getPreferences(project);
        }
        return preferences;
    }

    /**
     * 指定のプロジェクトにこのネイチャが付与されている場合のみ{@code true}を返す。
     * @param project 対象のプロジェクト
     * @return 指定のプロジェクトにこのネイチャが付与されている場合に{@code true}、
     *     そうでない場合は{@code false}
     * @throws IllegalArgumentException 引数に{@code null}が含まれる場合
     */
    public static boolean isAdded(IProject project) {
        if (project == null) {
            throw new IllegalArgumentException("project is null"); //$NON-NLS-1$
        }
        try {
            return project.hasNature(ID);
        }
        catch (CoreException e) {
            return false;
        }
    }

    /**
     * 指定のプロジェクトに対するこのネイチャを返す。
     * <p>
     * この呼び出しでは、指定のプロジェクトにネイチャが存在するかどうかを判定しない。
     * つまり、指定のプロジェクトにネイチャが存在しなくても、この呼び出しは{@link Slim3Nature}オブジェクトを返す。
     * </p>
     * @param project 対象のプロジェクト
     * @return ネイチャオブジェクト
     * @throws IllegalArgumentException 引数に{@code null}が含まれる場合
     */
    public static Slim3Nature create(IProject project) {
        if (project == null) {
            throw new IllegalArgumentException("project is null"); //$NON-NLS-1$
        }
        Slim3Nature nature = new Slim3Nature();
        nature.setProject(project);
        return nature;
    }

    /**
     * 指定のプロジェクトにこのネイチャを付与する。
     * <p>
     * すでに指定のプロジェクトにこのネイチャが付与されている場合、この呼び出しはなにも行わない。
     * </p>
     * @param monitor 進捗モニタ
     * @param project 対象のプロジェクト
     * @return ネイチャオブジェクト
     * @throws CoreException ネイチャの付与に失敗した場合
     * @throws OperationCanceledException キャンセルが要求された場合
     * @throws IllegalArgumentException 引数に{@code null}が含まれる場合
     */
    public static Slim3Nature add(IProgressMonitor monitor, IProject project)
            throws CoreException {
        if (monitor == null) {
            throw new IllegalArgumentException("monitor is null"); //$NON-NLS-1$
        }
        if (project == null) {
            throw new IllegalArgumentException("project is null"); //$NON-NLS-1$
        }
        monitor.beginTask("Adding Slim3 Nature", 3);
        try {
            if (monitor.isCanceled()) {
                throw new OperationCanceledException();
            }
            IProjectDescription description = project.getDescription();
            List<String> natures = new ArrayList<String>(
                    Arrays.asList(description.getNatureIds()));
            if (natures.contains(ID)) {
                return create(project);
            }
            natures.add(ID);
            description.setNatureIds(natures.toArray(new String[natures.size()]));
            monitor.worked(1);

            project.setDescription(
                description,
                new SubProgressMonitor(monitor, 2));

            return create(project);
        }
        finally {
            monitor.done();
        }
    }

    /**
     * 指定のプロジェクトからこのネイチャを削除する。
     * <p>
     * 指定のプロジェクトにこのネイチャが付与されていない場合、この呼び出しはなにも行わない。
     * </p>
     * @param monitor 進捗モニタ
     * @param project 対象のプロジェクト
     * @throws CoreException ネイチャの削除に失敗した場合
     * @throws OperationCanceledException キャンセルが要求された場合
     * @throws IllegalArgumentException 引数に{@code null}が含まれる場合
     */
    public static void remove(IProgressMonitor monitor, IProject project)
            throws CoreException {
        if (monitor == null) {
            throw new IllegalArgumentException("monitor is null"); //$NON-NLS-1$
        }
        if (project == null) {
            throw new IllegalArgumentException("project is null"); //$NON-NLS-1$
        }
        monitor.beginTask("Removing Slim3 Nature", 5);
        try {
            if (monitor.isCanceled()) {
                throw new OperationCanceledException();
            }
            IProjectDescription description = project.getDescription();
            List<String> natures = new ArrayList<String>(
                    Arrays.asList(description.getNatureIds()));
            if (natures.remove(ID) == false) {
                return;
            }
            description.setNatureIds(natures.toArray(new String[natures.size()]));
            monitor.worked(1);

            Slim3ClasspathContainer.remove(
                new SubProgressMonitor(monitor, 2),
                project);

            project.setDescription(
                description,
                new SubProgressMonitor(monitor, 2));
        }
        finally {
            monitor.done();
        }
    }
}
