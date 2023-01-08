
package dev.guillermo.gradle.language.c.plugins;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;

import org.gradle.api.Project;
import org.gradle.language.cpp.CppLibrary;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class CLibraryPluginTest {

    @TempDir
    File tempDir;

    @Test
    void addsExtensionWithConventionForBaseName() throws IOException {

        // Given
        final Project project = ProjectBuilder.builder().withName("foobar").withProjectDir(tempDir).build();
        CBasePluginTest.touch(tempDir, "src/main/c/foobar.c");

        // When
        project.getPlugins().apply(CLibraryPlugin.class);

        // Then
        final Object library = project.getExtensions().getByName("library");
        assertThat(library).isInstanceOf(CppLibrary.class);
        assertThat(((CppLibrary) library).getBaseName().get()).isEqualTo("foobar");
    }
}
