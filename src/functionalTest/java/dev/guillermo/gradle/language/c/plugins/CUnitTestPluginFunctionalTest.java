
package dev.guillermo.gradle.language.c.plugins;

import static dev.guillermo.gradle.language.c.plugins.TestHelper.writeFile;
import static org.gradle.testkit.runner.TaskOutcome.FAILED;
import static org.gradle.testkit.runner.TaskOutcome.SUCCESS;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;

import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.GradleRunner;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class CUnitTestPluginFunctionalTest {

    @TempDir
    File projectDir;

    @Test
    void buildProjectWithPassingTest() throws IOException {
        // Given
        writeFile(projectDir, "settings.gradle", "rootProject.name = 'foo'");
        writeFile(
                projectDir,
                "build.gradle",
                "plugins {",
                "  id('dev.guillermo.gradle.c-application')",
                "  id('dev.guillermo.gradle.c-unit-test')",
                "}");
        writeFile(projectDir, "src/main/c/foo.c", "int main(void){", "  return 0;", "}");
        writeFile(projectDir, "src/test/c/foo.c", "int main(void){", "  return 0;", "}");
        // When
        final BuildResult result = GradleRunner.create()
                .forwardOutput()
                .withPluginClasspath()
                .withArguments("check")
                .withProjectDir(projectDir)
                .build();
        // Then
        assertEquals(SUCCESS, result.task(":check").getOutcome());
    }

    @Test
    void buildProjectWithFailingTest() throws IOException {
        writeFile(projectDir, "settings.gradle", "rootProject.name = 'bar'");
        writeFile(
                projectDir,
                "build.gradle",
                "plugins {",
                "  id('dev.guillermo.gradle.c-application')",
                "  id('dev.guillermo.gradle.c-unit-test')",
                "}");
        writeFile(projectDir, "src/main/c/foo.c", "int main(void){", "  return 0;", "}");
        writeFile(projectDir, "src/test/c/foo.c", "int main(void){", "  return -1;", "}");
        // Run the build
        final BuildResult result = GradleRunner.create()
                .forwardOutput()
                .withPluginClasspath()
                .withArguments("check")
                .withProjectDir(projectDir)
                .buildAndFail();
        // Verify the result
        assertEquals(SUCCESS, result.task(":installTest").getOutcome());
        assertEquals(FAILED, result.task(":runTest").getOutcome());
    }
}
