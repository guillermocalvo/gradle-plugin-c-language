
package com.leakyabstractions.gradle.language.c;

import org.gradle.api.Project;
import org.gradle.api.Plugin;

/**
 * A simple 'hello world' plugin.
 */
public class GradlePluginCLanguagePlugin implements Plugin<Project> {
    public void apply(Project project) {
        // Register a task
        project.getTasks().register("greeting", task -> {
            task.doLast(s -> System.out.println("Hello from plugin 'com.leakyabstractions.gradle.language.c.greeting'"));
        });
    }
}
