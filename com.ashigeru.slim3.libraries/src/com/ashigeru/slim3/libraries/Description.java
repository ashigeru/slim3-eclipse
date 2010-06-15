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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.JavaCore;
import org.osgi.framework.Bundle;
import org.slim3.gen.processor.ModelProcessorFactory;

import com.ashigeru.slim3.eclipse.core.project.Slim3Library;
import com.ashigeru.slim3.libraries.internal.Activator;
import com.ashigeru.slim3.libraries.internal.LogUtil;

/**
 * Slim3ライブラリの説明。
 */
public class Description extends Slim3Library {

    private static final String VERSION = "1.0.4"; //$NON-NLS-1$

    private static final IPath LIBRARY_DIR = Path.fromPortableString("export"); //$NON-NLS-1$

    private static final String CLASSLIB_NAME_PATTERN = "slim3-{0}.jar"; //$NON-NLS-1$

    private static final String SOURCE_NAME_PATTERN = "slim3-{0}-sources.jar"; //$NON-NLS-1$

    @Override
    public String getVersion() {
        return VERSION;
    }

    @Override
    public List<IClasspathEntry> getClasspathEntries() {
        IPath jarPath = getJarPath();
        if (jarPath == null) {
            return Collections.emptyList();
        }

        IPath sourcePath = getSourcePath();
        return Arrays.asList(JavaCore.newLibraryEntry(
            jarPath,
            sourcePath,
            null,
            true));
    }

    @Override
    public ModelProcessorFactory createFactory() {
        return new ModelProcessorFactory();
    }

    private IPath getJarPath() {
        IPath path = LIBRARY_DIR.append(MessageFormat.format(
            CLASSLIB_NAME_PATTERN,
            VERSION));
        try {
            return toFullPath(path);
        }
        catch (IOException e) {
            LogUtil.log(IStatus.ERROR, MessageFormat.format(
                "Failed to resolve Slim3 library location: {0}",
                path),
                e);
            return null;
        }
    }

    private IPath getSourcePath() {
        IPath path = LIBRARY_DIR.append(MessageFormat.format(
            SOURCE_NAME_PATTERN,
            VERSION));
        try {
            return toFullPath(path);
        }
        catch (IOException e) {
            LogUtil.log(IStatus.ERROR, MessageFormat.format(
                "Failed to resolve Slim3 source archive location: {0}",
                path),
                e);
            return null;
        }
    }

    private static IPath toFullPath(IPath relativePath) throws IOException {
        assert relativePath != null;
        Bundle bundle = Activator.getDefault().getBundle();
        URL entry = bundle.getEntry(relativePath.toPortableString());
        if (entry == null) {
            throw new FileNotFoundException(relativePath.toPortableString());
        }
        URL fileUrl = FileLocator.toFileURL(entry);
        if (fileUrl.getProtocol().equals("file") == false) { //$NON-NLS-1$
            throw new FileNotFoundException(fileUrl.toExternalForm());
        }
        return toPath(fileUrl);
    }

    private static IPath toPath(URL url) throws IOException {
        assert url != null;
        assert url.getProtocol().equals("file"); //$NON-NLS-1$
        try {
            URI fileUri = url.toURI();
            String file = new File(fileUri).getAbsoluteFile().getCanonicalPath();
            IPath path = Path.fromOSString(file);
            return path;
        }
        catch (URISyntaxException ignore) {
            // continue...
        }

        String filePath = url.getFile();
        IPath path = Path.fromPortableString(filePath);
        if (path.toFile().exists()) {
            return path;
        }

        if (filePath.isEmpty() == false && filePath.startsWith("/")) { //$NON-NLS-1$
            path = Path.fromPortableString(filePath.substring(1));
            if (path.toFile().exists()) {
                return path;
            }
        }
        throw new FileNotFoundException(url.toExternalForm());
    }
}
