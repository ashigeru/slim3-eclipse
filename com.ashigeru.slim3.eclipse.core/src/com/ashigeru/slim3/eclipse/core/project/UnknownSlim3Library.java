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

import java.util.Collections;
import java.util.List;

import org.eclipse.jdt.core.IClasspathEntry;

import com.sun.mirror.apt.AnnotationProcessorFactory;

/**
 * ダミー表示用の存在しないライブラリ。
 * @author ashigeru
 */
public class UnknownSlim3Library extends Slim3Library {

    private String version;

    /**
     * インスタンスを生成する。
     * @param version 表記するバージョン
     * @throws IllegalArgumentException 引数に{@code null}が含まれる場合
     */
    public UnknownSlim3Library(String version) {
        if (version == null) {
            throw new IllegalArgumentException("version is null"); //$NON-NLS-1$
        }
        this.version = version;
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public List<IClasspathEntry> getClasspathEntries() {
        return Collections.emptyList();
    }

    @Override
    public AnnotationProcessorFactory createFactory() {
        return null;
    }
}
