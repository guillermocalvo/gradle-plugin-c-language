
package com.leakyabstractions.gradle.language.c;

import static org.junit.jupiter.api.Assertions.*;

import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.Test;

/** A simple unit test for the 'com.leakyabstractions.gradle.language.c.greeting' plugin. */
class GradlePluginCLanguagePluginTest {
    @Test
    void pluginRegistersATask() {
        // Create a test project and apply the plugin
        Project project = ProjectBuilder.builder().build();
        project.getPlugins().apply("com.leakyabstractions.gradle.language.c.greeting");

        // Verify the result
        assertNotNull(project.getTasks().findByName("greeting"));
    }
}
