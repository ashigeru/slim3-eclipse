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
 * 存在しないライブラリ。
 * @author ashigeru
 */
public class UnknownSlim3Library extends Slim3Library {

    /**
     * 不明なバージョン。
     */
    public static final String INVALID_VERSION = "INVALID";

    @Override
    public String getVersion() {
        return INVALID_VERSION;
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
