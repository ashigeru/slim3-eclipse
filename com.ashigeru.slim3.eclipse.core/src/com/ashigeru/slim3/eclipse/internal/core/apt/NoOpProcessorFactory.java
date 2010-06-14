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

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import com.sun.mirror.apt.AnnotationProcessor;
import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.apt.AnnotationProcessorFactory;
import com.sun.mirror.apt.AnnotationProcessors;
import com.sun.mirror.declaration.AnnotationTypeDeclaration;

/**
 * 何も行わない {@link AnnotationProcessorFactory}.
 * @author ashigeru
 */
public class NoOpProcessorFactory implements AnnotationProcessorFactory {

    @Override
    public AnnotationProcessor getProcessorFor(
            Set<AnnotationTypeDeclaration> arg0,
            AnnotationProcessorEnvironment arg1) {
        return AnnotationProcessors.NO_OP;
    }

    @Override
    public Collection<String> supportedAnnotationTypes() {
        return Collections.emptySet();
    }

    @Override
    public Collection<String> supportedOptions() {
        return Collections.emptySet();
    }
}
