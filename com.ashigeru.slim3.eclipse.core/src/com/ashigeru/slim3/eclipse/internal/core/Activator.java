package com.ashigeru.slim3.eclipse.internal.core;

import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.osgi.framework.BundleContext;

import com.ashigeru.slim3.eclipse.core.project.Slim3Library;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends Plugin {

	/**
	 *  The plug-in ID
	 */
	public static final String PLUGIN_ID = "com.ashigeru.slim3.eclipse.core";

	// The shared instance
	private static Activator plugin;

	private ExtensionRegistry registry;

	@Override
    public void start(BundleContext context) throws Exception {
		super.start(context);
		registry = new ExtensionRegistry();
		plugin = this;
	}

	@Override
    public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

    /**
     * Returns registered slim3 libraries.
     * @return registered slim3 libraries
     */
    public Map<String, Slim3Library> getLibraries() {
        return registry.getSlim3Libraries();
    }

    /**
     * 指定のプロジェクトで利用する設定のストアを返す。
     * @param project 対象のプロジェクト
     * @return 利用する設定のストア
     */
    public IEclipsePreferences getPreferences(IProject project) {
        ProjectScope scope = new ProjectScope(project);
        return scope.getNode(PLUGIN_ID);
    }
}
