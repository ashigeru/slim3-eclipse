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
package com.ashigeru.slim3.eclipse.internal.ui;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;

/**
 * ログを出力するためのユーティリティ。
 */
public class LogUtil {

    private static final String ID = Activator.PLUGIN_ID;
    private static final ILog LOG;
    static {
        Plugin plugin = Activator.getDefault();
        LOG = plugin == null ? null : plugin.getLog();
    }

    /**
     * ログを出力する。
     * @param severity 重大性 ({@code org.eclipse.core.runtime.IStatus})
     * @param message メッセージ
     * @throws NullPointerException 引数に{@code null}が指定された場合
     */
    public static void log(int severity, String message) {
        log(severity, ID, message);
    }

    /**
     * ログを出力する。
     * @param severity 重大性 ({@code org.eclipse.core.runtime.IStatus})
     * @param pluginId プラグイン
     * @param message メッセージ
     * @throws NullPointerException 引数に{@code null}が指定された場合
     */
    public static void log(int severity, String pluginId, String message) {
        if (pluginId == null) {
            throw new NullPointerException("pluginId"); //$NON-NLS-1$
        }
        if (message == null) {
            throw new NullPointerException("message"); //$NON-NLS-1$
        }
        Status status = new Status(severity, pluginId, message);
        log0(status);
    }

    /**
     * ログを出力する。
     * @param severity 重大性 ({@code org.eclipse.core.runtime.IStatus})
     * @param message メッセージ (省略可)
     * @param exception 例外
     * @throws NullPointerException 引数に{@code null}が指定された場合
     */
    public static void log(int severity, String message, Throwable exception) {
        log(severity, ID, message, exception);
    }

    /**
     * ログを出力する。
     * @param severity 重大性 ({@code org.eclipse.core.runtime.IStatus})
     * @param pluginId プラグイン
     * @param message メッセージ (省略可)
     * @param exception 例外
     * @throws NullPointerException 引数に{@code null}が指定された場合
     */
    public static void log(int severity, String pluginId, String message, Throwable exception) {
        if (pluginId == null) {
            throw new NullPointerException("pluginId"); //$NON-NLS-1$
        }
        if (exception == null) {
            throw new NullPointerException("exception"); //$NON-NLS-1$
        }
        Status status = new Status(
            severity,
            pluginId,
            message == null ? "" : message, //$NON-NLS-1$
            exception);
        log0(status);
    }

    /**
     * ログを出力する。
     * @param status 状態
     * @throws NullPointerException 引数に{@code null}が指定された場合
     */
    public static void log(IStatus status) {
        if (status == null) {
            throw new NullPointerException("status"); //$NON-NLS-1$
        }
        log0(status);
    }

    /**
     * ログを出力する。
     * @param exception 例外
     * @throws NullPointerException 引数に{@code null}が指定された場合
     */
    public static void log(CoreException exception) {
        if (exception == null) {
            throw new NullPointerException("exception"); //$NON-NLS-1$
        }
        log0(exception.getStatus());
    }

    private static void log0(IStatus status) {
        if (LOG != null) {
            LOG.log(status);
        }
        else {
            System.err.println(status);
        }
    }

    /**
     * インスタンス化の禁止。
     */
    private LogUtil() {
        super();
    }
}
