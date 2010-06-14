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
package com.ashigeru.slim3.eclipse.internal.core;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;

import com.ashigeru.slim3.eclipse.core.project.Slim3Library;

/**
 * このプラグインの拡張ポイントを保持するレジストリ。
 * @version $Date$
 * @author ashigeru
 */
public class ExtensionRegistry {

    /**
     * {@link Slim3Library}の拡張ポイント。
     */
    public static final String SLIM3_LIBRARY_ID = Activator.PLUGIN_ID + ".library"; //$NON-NLS-1$

    /**
     * {@link Slim3Library}の情報が格納された要素名。
     */
    public static final String K_LIBRARIES_ELEMENT = "libraries"; //$NON-NLS-1$

    /**
     * {@link #K_LIBRARIES_ELEMENT}に含まれる属性で、ファクトリの実装クラスを保持する。
     */
    public static final String K_LIBRARIES_CLASS = "class"; //$NON-NLS-1$

    private Map<String, Slim3Library> slim3Libraries;

    /**
     * 拡張ポイント{@link #SLIM3_LIBRARY_ID}に指定された{@link Slim3Library}の一覧を返す。
     * <p>
     * 返されるマップのキーは、値に取ったパーサが処理可能なファイルの拡張子が格納される。
     * この拡張子は、区切り文字の{@code '.'}を含まない。
     * </p>
     * <p>
     * 返されるマップは変更できない。
     * </p>
     * @return 拡張ポイントに登録された{@link Slim3Library}の一覧
     */
    public synchronized Map<String, Slim3Library> getSlim3Libraries() {
        if (slim3Libraries != null) {
            return slim3Libraries;
        }
        Map<String, Slim3Library> results = loadLibraries();
        this.slim3Libraries = Collections.unmodifiableMap(
            new HashMap<String, Slim3Library>(results));
        return slim3Libraries;
    }

    private Map<String, Slim3Library> loadLibraries() {
        IExtensionRegistry registory = Platform.getExtensionRegistry();
        IExtensionPoint point = registory.getExtensionPoint(SLIM3_LIBRARY_ID);

        Map<String, Slim3Library> results = new HashMap<String, Slim3Library>();
        for (IExtension extension : point.getExtensions()) {
            for (IConfigurationElement element : extension.getConfigurationElements()) {
                if (element.getName().equals(K_LIBRARIES_ELEMENT) == false) {
                    continue;
                }
                Slim3Library instance;
                try {
                    instance = getExecutable(
                        extension, element, Slim3Library.class, K_LIBRARIES_CLASS);
                }
                catch (CoreException e) {
                    Activator.getDefault().getLog().log(e.getStatus());
                    continue;
                }
                String key = instance.getVersion();
                if (results.containsKey(key)) {
                    Slim3Library already = results.get(key);
                    Status status = new Status(
                        IStatus.ERROR,
                        Activator.PLUGIN_ID,
                        MessageFormat.format(
                            "Conflicts file extension \"{0}\", between {1} and {2} ({3} ignored)",
                            key,
                            instance.getClass().getName(),
                            already.getClass().getName(),
                            extension.getContributor().getName()));
                    Activator.getDefault().getLog().log(status);
                }
                else {
                    results.put(key, instance);
                }
            }
        }

        return results;
    }

    private static <T> T getExecutable(
            IExtension extension,
            IConfigurationElement element,
            Class<T> klass,
            String attributeName) throws CoreException {
        assert extension != null;
        assert element != null;
        assert klass != null;
        assert attributeName != null;
        Object instance;
        try {
            instance = element.createExecutableExtension(attributeName);
        }
        catch (CoreException e) {
            MultiStatus status = new MultiStatus(
                Activator.PLUGIN_ID,
                IStatus.ERROR,
                new IStatus[] { e.getStatus() },
                MessageFormat.format(
                    "Cannot load extension {0}",
                    extension.getContributor().getName()),
                null);
            throw new CoreException(status);
        }
        if (klass.isInstance(instance) == false) {
            Status status = new Status(
                IStatus.ERROR,
                Activator.PLUGIN_ID,
                MessageFormat.format(
                    "{0} is not a {1}, {2} ({3})",
                    instance,
                    klass.getName(),
                    instance.getClass().getName(),
                    extension.getContributor().getName()));
            throw new CoreException(status);
        }

        return klass.cast(instance);
    }
}
