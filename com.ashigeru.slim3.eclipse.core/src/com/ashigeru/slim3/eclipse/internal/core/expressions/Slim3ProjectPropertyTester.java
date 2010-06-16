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
package com.ashigeru.slim3.eclipse.internal.core.expressions;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;

import com.ashigeru.slim3.eclipse.core.project.Slim3ClasspathContainer;
import com.ashigeru.slim3.eclipse.core.project.Slim3Nature;
import com.ashigeru.slim3.eclipse.internal.core.LogUtil;

/**
 * Slim3プロジェクトに関するテスター。
 */
public class Slim3ProjectPropertyTester extends PropertyTester {

    @Override
    public boolean test(
            Object receiver,
            String property,
            Object[] args,
            Object expectedValue) {
        boolean expected = toBoolean(expectedValue);
        if ((receiver instanceof IProject) == false) {
            return !expected;
        }
        if (property == null) {
            return !expected;
        }
        IProject project = (IProject) receiver;
        return expected == test(project, property);
    }

    private boolean test(IProject project, String property) {
        assert project != null;
        assert property != null;
        if ("hasNature".equals(property)) {
            return Slim3Nature.isAdded(project);
        }
        if ("hasClassLibrary".equals(property)) {
            try {
                return Slim3ClasspathContainer.existsIn(project);
            }
            catch (CoreException e) {
                LogUtil.log(e);
                return false;
            }
        }
        return false;
    }

    private boolean toBoolean(Object value) {
        // "expected null" means default expectation
        if (value == null) {
            return true;
        }
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        throw new IllegalArgumentException(String.valueOf(value));
    }
}
