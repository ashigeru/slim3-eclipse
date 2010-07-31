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

/**
 * Project properties for Slim3 Plug-in.
 * @author ashigeru
 */
public class Slim3ProjectProperties {

    /**
     * <p>
     * </p>
     * @param project
     * @return
     * @throws IllegalArgumentException 引数に{@code null}が含まれる場合
     */
    public static Slim3ProjectProperties load(IProject project) {
        // TODO load 2010/06/17 1:35:48
        return new Slim3ProjectProperties();
    }

    /**
     * <p>
     * </p>
     * @param project
     * @throws IllegalArgumentException 引数に{@code null}が含まれる場合
     */
    public void apply(IProject project) {
        // TODO apply 2010/06/17 21:42:16

    }

    /**
     * <p>
     * </p>
     * @return
     * @throws IllegalArgumentException 引数に{@code null}が含まれる場合
     */
    public static Slim3ProjectProperties createDefaults() {
        // TODO createDefaults 2010/06/17 21:44:33
        return new Slim3ProjectProperties();
    }

    /**
     * <p>
     * </p>
     * @return
     * @throws IllegalArgumentException 引数に{@code null}が含まれる場合
     */
    public boolean isEnabled() {
        // TODO isEnabled 2010/06/17 23:54:03
        return false;
    }

    /**
     * <p>
     * </p>
     * @return
     * @throws IllegalArgumentException 引数に{@code null}が含まれる場合
     */
    public String getVersion() {
        // TODO getVersion 2010/06/17 23:54:23
        return null;
    }

    /**
     * <p>
     * </p>
     * @return
     * @throws IllegalArgumentException 引数に{@code null}が含まれる場合
     */
    public boolean isClassLibraryEnabled() {
        // TODO isClassLibraryEnabled 2010/06/17 23:58:55
        return false;
    }

    /**
     * <p>
     * </p>
     * @param selection
     * @throws IllegalArgumentException 引数に{@code null}が含まれる場合
     */
    public void setEnabled(boolean selection) {
        // TODO setEnabled 2010/06/18 0:00:09

    }

    /**
     * <p>
     * </p>
     * @param slim3Library
     * @throws IllegalArgumentException 引数に{@code null}が含まれる場合
     */
    public void setVersion(Slim3Library slim3Library) {
        // TODO setVersion 2010/06/18 0:02:07

    }

    /**
     * <p>
     * </p>
     * @param selection
     * @throws IllegalArgumentException 引数に{@code null}が含まれる場合
     */
    public void setClassLibraryEnabled(boolean selection) {
        // TODO setClassLibraryEnabled 2010/06/18 0:02:52

    }
}
