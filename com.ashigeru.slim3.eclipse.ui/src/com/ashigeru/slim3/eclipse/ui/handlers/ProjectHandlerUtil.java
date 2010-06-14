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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * プロジェクト操作のハンドラに関するユーティリティ。
 */
public class ProjectHandlerUtil {

    private static final List<Class<? extends IResource>> RESOURCE_KIND;
    static {
        List<Class<? extends IResource>> classes =
            new ArrayList<Class<? extends IResource>>();
        classes.add(IResource.class);
        classes.add(IContainer.class);
        classes.add(IProject.class);
        classes.add(IFolder.class);
        classes.add(IFile.class);
        RESOURCE_KIND = Collections.unmodifiableList(classes);
    }

    /**
     * {@link ExecutionEvent}の情報をもとに、現在選択中のプロジェクトを取得する。
     * @param event 対象のイベントオブジェクト
     * @return 選択しているプロジェクト、存在しない場合は{@code false}
     * @throws ExecutionException イベントの状態が不正である場合
     * @throws IllegalArgumentException 引数に{@code null}が含まれる場合
     */
    public static IProject getTargetProject(ExecutionEvent event)
            throws ExecutionException {
        if (event == null) {
            throw new IllegalArgumentException("event is null"); //$NON-NLS-1$
        }
        ISelection s = HandlerUtil.getCurrentSelectionChecked(event);
        if ((s instanceof IStructuredSelection) == false) {
            return null;
        }
        IStructuredSelection sel = (IStructuredSelection) s;
        Object elem = sel.getFirstElement();
        if ((elem instanceof IAdaptable) == false) {
            return null;
        }

        IProject project = (IProject) ((IAdaptable) elem).getAdapter(IProject.class);
        return project;
    }

    /**
     * {@link ExecutionEvent}の情報をもとに、現在選択中のリソースを取得する。
     * @param event 対象のイベントオブジェクト
     * @return 選択しているリソース、存在しない場合は{@code false}
     * @throws ExecutionException イベントの状態が不正である場合
     * @throws IllegalArgumentException 引数に{@code null}が含まれる場合
     */
    public static IResource getTargetResource(ExecutionEvent event)
            throws ExecutionException {
        if (event == null) {
            throw new IllegalArgumentException("event is null"); //$NON-NLS-1$
        }
        ISelection s = HandlerUtil.getCurrentSelectionChecked(event);
        if ((s instanceof IStructuredSelection) == false) {
            return null;
        }
        IStructuredSelection sel = (IStructuredSelection) s;
        Object elem = sel.getFirstElement();
        if ((elem instanceof IAdaptable) == false) {
            return null;
        }

        return adapt((IAdaptable) elem, RESOURCE_KIND);
    }

    private static <T> T adapt(
            IAdaptable adaptable,
            List<Class<? extends T>> adapterClasses) {
        assert adaptable != null;
        assert adapterClasses != null;
        for (Class<? extends T> aClass : adapterClasses) {
            Object adapter = adaptable.getAdapter(aClass);
            if (adapter != null) {
                return aClass.cast(adapter);
            }
        }
        return null;
    }

    /**
     * 指定のオブジェクトをダイアログボックス上に表示する。
     * @param shell 表示に使用するシェル
     * @param toShow 表示するオブジェクト
     */
    public static void show(final Shell shell, final Object toShow) {
        if (toShow == null) {
            return;
        }
        shell.getDisplay().asyncExec(new Runnable() {
            @Override
            public void run() {
                MessageDialog.openInformation(
                    shell,
                    toShow.getClass().getName(),
                    String.valueOf(toShow));
            }
        });
    }

    /**
     * 指定のウィンドウのすべてのエディタを保存する。
     * @param window 対象のウィンドウ
     * @return キャンセルされなかった場合のみ{@code true}
     * @throws IllegalArgumentException 引数に{@code null}が含まれる場合
     */
    public static boolean saveDirtyEditors(IWorkbenchWindow window) {
        if (window == null) {
            throw new IllegalArgumentException("window is null"); //$NON-NLS-1$
        }
        IWorkbenchPage[] pages = window.getPages();
        for (IWorkbenchPage page : pages) {
            boolean saved = page.saveAllEditors(true);
            if (saved == false) {
                return false;
            }
        }
        return true;
    }

    /**
     * インスタンス生成の禁止。
     */
    private ProjectHandlerUtil() {
        throw new AssertionError();
    }
}
