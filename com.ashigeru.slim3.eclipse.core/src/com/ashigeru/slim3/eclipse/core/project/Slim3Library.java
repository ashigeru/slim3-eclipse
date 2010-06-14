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

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jdt.core.IClasspathEntry;

import com.sun.mirror.apt.AnnotationProcessorFactory;

/**
 * Slim3 のライブラリ情報に関する定義。
 * @author ashigeru
 */
public abstract class Slim3Library implements Comparable<Slim3Library> {

    /**
     * ライブラリバージョンを表す文字列を返す。
     * @return ライブラリバージョンを表す文字列
     */
    public abstract String getVersion();

    /**
     * クラスパスエントリの一覧を返す。
     * @return クラスパスエントリの一覧
     */
    public abstract List<IClasspathEntry> getClasspathEntries();

    /**
     * このライブラリに関連する{@link AnnotationProcessorFactory}を生成して返す。
     * @return 未対応の場合は{@code null}
     */
    public abstract AnnotationProcessorFactory createFactory();

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getVersion() == null) ? 0 : getVersion().hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Slim3Library other = (Slim3Library) obj;
        if (getVersion() == null) {
            if (other.getVersion() != null) {
                return false;
            }
        }
        else if (getVersion().equals(other.getVersion())) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(Slim3Library o) {
        Iterator<VersionSegment> a = VersionSegment.parse(getVersion()).iterator();
        Iterator<VersionSegment> b = VersionSegment.parse(o.getVersion()).iterator();
        while (true) {
            if (a.hasNext() == false) {
                return b.hasNext() ? -1 : 0;
            }
            if (b.hasNext() == false) {
                assert a.hasNext();
                return +1;
            }
            int cmp = a.next().compareTo(b.next());
            if (cmp != 0) {
                return cmp;
            }
        }
    }

    @Override
    public String toString() {
        return MessageFormat.format(
            "{0} [{1}]",
            getClass().getSimpleName(),
            getVersion());
    }

    private static class VersionSegment implements Comparable<VersionSegment> {

        private static Pattern NUMERIC = Pattern.compile("[0-9]+");

        /**
         * バージョン文字列をセグメントごとに分割して返す。
         * @param version バージョン文字列
         * @return バージョンをセグメントごとに区切った情報、文字列が空の場合は空
         */
        public static List<VersionSegment> parse(String version) {
            if (version == null || version.length() == 0) {
                return Collections.emptyList();
            }
            List<VersionSegment> results = new ArrayList<VersionSegment>();
            Matcher m = NUMERIC.matcher(version);
            int start = 0;
            while (m.find(start)) {
                if (start != m.start()) {
                    String segment = version.substring(start, m.start());
                    results.add(new VersionSegment(segment));
                }
                String segment = version.substring(m.start(), m.end());
                results.add(new VersionSegment(segment));
                start = m.end();
            }
            if (start != version.length()) {
                results.add(new VersionSegment(version.substring(start)));
            }
            return results;
        }

        private String string;

        private long number;

        private boolean numeric;

        /**
         * インスタンスを生成する。
         * @param string セグメントを構成する文字列
         */
        public VersionSegment(String string) {
            if (string == null) {
                throw new IllegalArgumentException("string is null"); //$NON-NLS-1$
            }
            this.string = string;
            try {
                number = Long.parseLong(string);
                numeric = true;
            }
            catch (NumberFormatException e) {
                number = -1;
                numeric = false;
            }
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + string.hashCode();
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            VersionSegment other = (VersionSegment) obj;
            if (string == null) {
                if (other.string != null) {
                    return false;
                }
            }
            else if (string.equals(other.string) == false) {
                return false;
            }
            return true;
        }

        @Override
        public int compareTo(VersionSegment o) {
            if (numeric) {
                if (o.numeric) {
                    if (number == o.number) {
                        return 0;
                    }
                    else if (number < o.number){
                        return -1;
                    }
                    else {
                        return +1;
                    }
                }
                return -1;
            }
            else {
                if (o.numeric == false) {
                    return string.compareTo(o.string);
                }
                return +1;
            }
        }

        @Override
        public String toString() {
            return string;
        }
    }
}
