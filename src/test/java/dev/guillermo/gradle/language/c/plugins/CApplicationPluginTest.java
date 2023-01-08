
package dev.guillermo.gradle.language.c.plugins;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;

import org.gradle.api.Project;
import org.gradle.language.cpp.CppApplication;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class CApplicationPluginTest {

    @TempDir
    File tempDir;

    @Test
    void addsExtensionWithConventionForBaseName() throws IOException {

        // Given
        final File projectDir = new File(tempDir.getCanonicalPath());
        final Project project = ProjectBuilder.builder().withName("foo").withProjectDir(projectDir).build();
        CBasePluginTest.touch(projectDir, "src/main/c/main.c");

        // When
        project.getPlugins().apply(CApplicationPlugin.class);

        // Then
        final Object application = project.getExtensions().getByName("application");
        assertThat(application).isInstanceOf(CppApplication.class);
        assertThat(((CppApplication) application).getBaseName().get()).isEqualTo("foo");
    }
}
