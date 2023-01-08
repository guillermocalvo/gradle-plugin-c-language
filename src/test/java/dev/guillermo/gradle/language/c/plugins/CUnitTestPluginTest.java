
package dev.guillermo.gradle.language.c.plugins;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;

import org.gradle.api.Project;
import org.gradle.nativeplatform.test.cpp.CppTestSuite;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class CUnitTestPluginTest {

    @TempDir
    File tempDir;

    @Test
    void addsExtensionWithConventionForBaseName() throws IOException {

        // Given
        final Project project = ProjectBuilder.builder().withName("bar").withProjectDir(tempDir).build();
        CBasePluginTest.touch(tempDir, "src/test/c/bar.c");

        // When
        project.getPlugins().apply(CApplicationPlugin.class);
        project.getPlugins().apply(CUnitTestPlugin.class);

        // Then
        final Object unitTest = project.getExtensions().getByName("unitTest");
        assertThat(unitTest).isInstanceOf(CppTestSuite.class);
        assertThat(((CppTestSuite) unitTest).getBaseName().get()).isEqualTo("barTest");
    }
}
