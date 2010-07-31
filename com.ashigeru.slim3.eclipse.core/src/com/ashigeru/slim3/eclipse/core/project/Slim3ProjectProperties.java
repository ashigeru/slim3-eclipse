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

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;

/**
 * Project properties for Slim3 Plug-in.
 * @author ashigeru
 */
public class Slim3ProjectProperties {

    private boolean enabled = false;

    private boolean libraryEnabled = false;

    private String libraryVersion = null;

    /**
     * 指定のプロジェクトに関連するSlim3の設定情報を返す。
     * @param project 対象のプロジェクト
     * @return 関連する設定情報
     * @throws IllegalArgumentException 引数に{@code null}が含まれる場合
     */
    public static Slim3ProjectProperties load(IProject project) {
        if (project == null) {
            throw new IllegalArgumentException("project must not be null"); //$NON-NLS-1$
        }
        Slim3Nature nature = Slim3Nature.create(project);
        Slim3ProjectProperties result = createDefaults();
        result.setEnabled(Slim3Nature.isAdded(project));
        result.setClassLibraryEnabled(Slim3ClasspathContainer.isAdded(project));
        result.setLibraryVersion(nature.getLibraryVersion());
        return result;
    }

    /**
     * 指定のSlim3の設定を指定のプロジェクトに設定する。
     * @param project 対象のプロジェクト
     * @throws CoreException 設定に失敗した場合
     * @throws IllegalArgumentException 引数に{@code null}が含まれる場合
     */
    public void apply(IProject project) throws CoreException {
        if (project == null) {
            throw new IllegalArgumentException("project must not be null"); //$NON-NLS-1$
        }
        Slim3Nature nature = Slim3Nature.create(project);
        nature.setLibraryVersion(getLibraryVersion());
        if (isEnabled()) {
            Slim3Nature.add(new NullProgressMonitor(), project);
            if (isClassLibraryEnabled()) {
                Slim3ClasspathContainer.add(new NullProgressMonitor(), project);
            }
            else {
                Slim3ClasspathContainer.remove(new NullProgressMonitor(), project);
            }
        }
        else {
            Slim3Nature.remove(new NullProgressMonitor(), project);
        }
    }

    /**
     * デフォルトの設定を新しく作成して返す。
     * @return 作成した設定
     */
    public static Slim3ProjectProperties createDefaults() {
        return new Slim3ProjectProperties();
    }

    /**
     * Slim3の機能が有効である場合のみ{@code true}を返す。
     * @return Slim3の機能が有効である場合に{@code true}
     */
    public boolean isEnabled() {
        return this.enabled;
    }

    /**
     * Slim3の機能を有効化、または無効化する。
     * @param enabled {@code true}の場合は有効化、{@code false}の場合は無効化する
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * Slim3のクラスライブラリが有効である場合のみ{@code true}を返す。
     * @return Slim3のクラスライブラリが有効である場合に{@code true}
     */
    public boolean isClassLibraryEnabled() {
        return libraryEnabled;
    }

    /**
     * Slim3のライブラリを有効化、または無効化する。
     * @param enabled {@code true}の場合は有効化、{@code false}の場合は無効化する
     */
    public void setClassLibraryEnabled(boolean enabled) {
        this.libraryEnabled = enabled;
    }

    /**
     * Slim3で利用するライブラリのバージョンを返す。
     * @return ライブラリのバージョン文字列、未指定の場合は{@code null}
     */
    public String getLibraryVersion() {
        return libraryVersion;
    }

    /**
     * Slim3で利用するライブラリのバージョンを設定する。
     * @param version ライブラリのバージョン文字列、利用しない場合は{@code null}
     */
    public void setLibraryVersion(String version) {
        this.libraryVersion = version;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (this.enabled ? 1231 : 1237);
        result = prime * result + (this.libraryEnabled ? 1231 : 1237);
        result = prime * result
                + (libraryVersion == null ? 0 : libraryVersion.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Slim3ProjectProperties other = (Slim3ProjectProperties) obj;
        if (this.enabled != other.enabled) {
            return false;
        }
        if (this.libraryEnabled != other.libraryEnabled) {
            return false;
        }
        if (this.libraryVersion == null) {
            if (other.libraryVersion != null) {
                return false;
            }
        }
        else if (!this.libraryVersion.equals(other.libraryVersion)) {
            return false;
        }
        return true;
    }
}
