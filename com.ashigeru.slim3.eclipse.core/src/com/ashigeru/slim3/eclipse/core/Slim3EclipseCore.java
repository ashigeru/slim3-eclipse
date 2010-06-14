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
package com.ashigeru.slim3.eclipse.core;

import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import com.ashigeru.slim3.eclipse.core.project.Slim3Library;
import com.ashigeru.slim3.eclipse.internal.core.Activator;

/**
 * このプラグインが公開するプラグイン特有の情報。
 * @author ashigeru
 */
public class Slim3EclipseCore {

    /**
     * 現在登録されているライブラリの一覧をバージョンの若い順に返す。
     * @return 現在登録されているライブラリの一覧
     */
    public static SortedSet<Slim3Library> getLibraries() {
        Map<String, Slim3Library> libraries = Activator.getDefault().getLibraries();
        return new TreeSet<Slim3Library>(libraries.values());
    }
}
