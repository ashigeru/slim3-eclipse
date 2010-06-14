/*
S * Copyright 2010 @ashigeru.
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
package com.ashigeru.slim3.libraries;

import java.util.Collections;
import java.util.List;

import org.eclipse.jdt.core.IClasspathEntry;
import org.slim3.gen.processor.ModelProcessorFactory;

import com.ashigeru.slim3.eclipse.core.project.Slim3Library;

/**
 * Slim3ライブラリの説明。
 * @author ashigeru
 */
public class Description extends Slim3Library {

    private static final String VERSION = "1.0.4"; //$NON-NLS-1$

    @Override
    public String getVersion() {
        return VERSION;
    }

    @Override
    public List<IClasspathEntry> getClasspathEntries() {
        // TODO getClasspathEntries 2010/06/12 16:51:09
        return Collections.emptyList();
    }

    @Override
    public ModelProcessorFactory createFactory() {
        return new ModelProcessorFactory();
    }
}
