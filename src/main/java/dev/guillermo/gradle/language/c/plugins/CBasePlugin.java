
package dev.guillermo.gradle.language.c.plugins;

import java.util.Map;
import java.util.Optional;

import org.gradle.api.Project;
import org.gradle.api.logging.Logger;
import org.gradle.language.cpp.CppBinary;
import org.gradle.language.cpp.CppComponent;
import org.gradle.language.cpp.tasks.CppCompile;
import org.gradle.language.nativeplatform.ComponentWithExecutable;
import org.gradle.language.nativeplatform.ComponentWithSharedLibrary;
import org.gradle.nativeplatform.tasks.AbstractLinkTask;
import org.gradle.nativeplatform.toolchain.GccCompatibleToolChain;
import org.gradle.nativeplatform.toolchain.NativeToolChain;
import org.gradle.nativeplatform.toolchain.VisualCpp;

import dev.guillermo.gradle.language.c.CCompiler;

class CBasePlugin {

    private final Project project;
    private final CppComponent component;
    private final Logger logger;
    private final CCompiler compiler;

    CBasePlugin(Project project, CppComponent component) {
        this.project = project;
        this.component = component;
        this.logger = project.getLogger();
        this.logger.info("Configuring component: {}", component.getName());
        this.compiler = project.getExtensions().getByType(CCompiler.class);
    }

    private void configure(CppBinary binary) {

        final NativeToolChain toolchain = binary.getToolChain();
        this.logger.info(
                "Configuring {}.{} for {}",
                this.component.getBaseName().get(),
                binary.getName(),
                toolchain.getDisplayName());
        this.configureCompileTask(toolchain, binary.getCompileTask().get());
        this.getLinkTask(binary).ifPresent(linkTask -> this.configureLinkTask(toolchain, linkTask));
    }

    private void configureCompileTask(NativeToolChain toolchain, CppCompile compileTask) {
        // Configure C sources
        final String fromDir = "src/" + component.getName() + "/c";
        compileTask
                .getSource()
                .setFrom(this.project.fileTree(Map.of("dir", fromDir, "include", "**/*.c")));
        this.logger.info(
                "  {}.{}.{}.source.from: {}",
                this.component.getBaseName().get(),
                this.component.getName(),
                compileTask.getName(),
                fromDir);
        // Configure compiler macros
        compileTask.getMacros().putAll(this.compiler.getMacros());
        this.logger.info(
                "  {}.{}.{}.macros: {})",
                this.component.getBaseName().get(),
                this.component.getName(),
                compileTask.getName(),
                compileTask.getMacros());
        // Configure compiler args
        if (toolchain instanceof VisualCpp) {
            compileTask.getCompilerArgs().add("/TC");
            switch (this.compiler.getDialect()) {
                case C90:
                    this.logger.warn(
                            "{}: Cannot specify strict C90 conformance. Some Microsoft extensions are included.",
                            compileTask.getName());
                    compileTask.getCompilerArgs().add("/Za");
                    break;
                case C99:
                    this.logger.warn(
                            "{}: Cannot specify strict C99 conformance. The compiler doesn't implement several required features.",
                            compileTask.getName());
                    break;
                case C11:
                    compileTask.getCompilerArgs().add("/std:c11");
                    break;
                case C17:
                    compileTask.getCompilerArgs().add("/std:c17");
                    break;
                case DEFAULT_DIALECT:
                    break;
            }
            // Treat all compiler warnings as errors
            if (this.compiler.failOnWarning()) {
                compileTask.getCompilerArgs().add("/WX");
            }
            // Suppress all compiler warnings
            if (this.compiler.suppressAllWarnings()) {
                compileTask.getCompilerArgs().add("/w");
            }
            // Enable OpenMP Support
            if (this.compiler.enableOpenMp()) {
                compileTask.getCompilerArgs().add("/openmp");
            }
        } else if (toolchain instanceof GccCompatibleToolChain) {
            compileTask.getCompilerArgs().addAll("-x", "c");
            switch (this.compiler.getDialect()) {
                case C90:
                    compileTask.getCompilerArgs().add("-std=c90");
                    break;
                case C99:
                    this.logger.warn(
                            "{}: C99 is substantially completely supported. See https://gcc.gnu.org/c99status.html for more information.",
                            compileTask.getName());
                    compileTask.getCompilerArgs().add("-std=c99");
                    break;
                case C11:
                    compileTask.getCompilerArgs().add("-std=c11");
                    break;
                case C17:
                    compileTask.getCompilerArgs().add("-std=c17");
                    break;
                case DEFAULT_DIALECT:
                    break;
            }
            // Make all warnings into errors
            if (this.compiler.failOnWarning()) {
                compileTask.getCompilerArgs().add("-Werror");
            }
            // Inhibit all warning messages
            if (this.compiler.suppressAllWarnings()) {
                compileTask.getCompilerArgs().add("-w");
            }
            // Enable handling of OpenMP directives (implies -fopenmp-simd and -pthread)
            if (this.compiler.enableOpenMp()) {
                compileTask.getCompilerArgs().add("-fopenmp");
            }
        }
        this.logger.info(
                "  {}.{}.{}.compilerArgs: {})",
                this.component.getBaseName().get(),
                this.component.getName(),
                compileTask.getName(),
                compileTask.getCompilerArgs().get());
    }

    private void configureLinkTask(NativeToolChain toolchain, AbstractLinkTask linkTask) {
        // Configure linker args
        if (toolchain instanceof GccCompatibleToolChain) {
            linkTask.getLinkerArgs().addAll("-nodefaultlibs", "-lc");
        }
        this.logger.info(
                "  {}.{}.{}.linkerArgs: {}",
                this.component.getBaseName().get(),
                this.component.getName(),
                linkTask.getName(),
                linkTask.getLinkerArgs().get());
    }

    private Optional<AbstractLinkTask> getLinkTask(CppBinary binary) {
        if (binary instanceof ComponentWithExecutable) {
            return Optional.of(((ComponentWithExecutable) binary).getLinkTask().get());
        }
        if (binary instanceof ComponentWithSharedLibrary) {
            return Optional.of(((ComponentWithSharedLibrary) binary).getLinkTask().get());
        }
        return Optional.empty();
    }

    static void configure(Project project, Class<? extends CppComponent> clazz) {
        final CppComponent component = project.getExtensions().getByType(clazz);
        final CBasePlugin helper = new CBasePlugin(project, component);
        component.getBinaries().configureEach(helper::configure);
    }
}
