
package dev.guillermo.gradle.language.c.plugins;

import static dev.guillermo.gradle.language.c.plugins.TestHelper.writeFile;
import static org.gradle.testkit.runner.TaskOutcome.SUCCESS;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;

import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.GradleRunner;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class CLibraryPluginFunctionalTest {

    @TempDir
    File projectDir;

    @Test
    void buildLibrary() throws IOException {
        // Given
        writeFile(projectDir, "settings.gradle", "rootProject.name = 'lib'");
        writeFile(
                projectDir, "build.gradle", "plugins {", "  id('dev.guillermo.gradle.c-library')", "}");
        writeFile(projectDir, "src/main/c/foo.c", "int main(void){", "  return 0;", "}");
        // When
        final BuildResult result = GradleRunner.create()
                .forwardOutput()
                .withPluginClasspath()
                .withArguments("build")
                .withProjectDir(projectDir)
                .build();
        // Then
        assertEquals(SUCCESS, result.task(":build").getOutcome());
    }
}
