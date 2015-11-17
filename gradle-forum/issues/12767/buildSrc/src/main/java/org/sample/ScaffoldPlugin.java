package org.sample;

import org.gradle.api.Action;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.logging.Logger;
import org.gradle.api.tasks.Copy;

import java.net.URL;

public class ScaffoldPlugin implements Plugin<Project> {

    private final URL pluginUrl = getClass().getProtectionDomain().getCodeSource().getLocation();

    private final PluginResource res = new PluginResource();

    public static final String PLUGIN_TASK_NAME = "scaffold";

    @Override
    public void apply(final Project project) {
        final Logger logger = project.getLogger();
        project.getTasks().create(PLUGIN_TASK_NAME, Copy.class, new Action<Copy>() {
            @Override
            public void execute(Copy copy) {
                // in the case of buildSrc dir
                copy.from(project.fileTree(res.getResource("scaffold")));
                copy.into(String.format("%s/scaffold", project.getBuildDir()));
                // TODO: in the case of plugin is provided as jar.
            }
        });
    }

    boolean isPluginGivenAsFile() {
        return pluginUrl.toExternalForm().startsWith("file");
    }
}
