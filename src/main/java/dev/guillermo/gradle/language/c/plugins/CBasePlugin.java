
package dev.guillermo.gradle.language.c.plugins;

import java.util.HashMap;
import java.util.Map;

import org.gradle.api.Project;
import org.gradle.api.logging.Logger;
import org.gradle.api.provider.ListProperty;
import org.gradle.language.cpp.CppBinary;
import org.gradle.language.cpp.CppComponent;
import org.gradle.language.cpp.internal.DefaultCppExecutable;
import org.gradle.language.cpp.internal.DefaultCppLibrary;
import org.gradle.language.cpp.tasks.CppCompile;
import org.gradle.language.nativeplatform.ComponentWithExecutable;
import org.gradle.language.nativeplatform.ComponentWithSharedLibrary;
import org.gradle.nativeplatform.tasks.LinkExecutable;
import org.gradle.nativeplatform.tasks.LinkSharedLibrary;
import org.gradle.nativeplatform.test.cpp.internal.DefaultCppTestExecutable;
import org.gradle.nativeplatform.toolchain.GccCompatibleToolChain;
import org.gradle.nativeplatform.toolchain.NativeToolChain;
import org.gradle.nativeplatform.toolchain.VisualCpp;

class CBasePlugin {

    private final Project project;
    private final CppComponent component;
    private final Logger logger;

    CBasePlugin(Project project, CppComponent component) {
        this.project = project;
        this.component = component;
        this.logger = project.getLogger();
        this.logger.info("Configuring component: {}", component.getName());
    }

    private void configure(CppBinary binary) {

        final NativeToolChain toolchain = binary.getToolChain();
        final CppCompile compileTask = binary.getCompileTask().get();

        this.logger.info(
                "Configuring C binary: {}.{} - {}",
                this.component.getBaseName().get(),
                binary.getName(),
                binary.getBaseName().get());

        final Map<String, String> from = new HashMap<>();

        from.put("include", "**/*.c");

        if (binary instanceof DefaultCppTestExecutable) {
            from.put("dir", "src/test/c");
        } else if (binary instanceof DefaultCppLibrary) {
            from.put("dir", "src/main/c");
        } else if (binary instanceof DefaultCppExecutable) {
            from.put("dir", "src/main/c");
        }

        this.logger.info("Configuring compile task: {}", compileTask.getName());
        this.logger.info("  source.from: {}", from);
        compileTask.getSource().setFrom(this.project.fileTree(from));

        final ListProperty<String> currentCompilerArgs = compileTask.getCompilerArgs();

        if (toolchain instanceof VisualCpp) {
            currentCompilerArgs.addAll("/TC", "/std:c17");
        } else if (toolchain instanceof GccCompatibleToolChain) {
            currentCompilerArgs.addAll("-x", "c", "-std=c11");
        }

        if (binary instanceof ComponentWithExecutable) {
            final LinkExecutable linkTask = ((ComponentWithExecutable) binary).getLinkTask().get();
            this.logger.info("Configuring link task: {}", linkTask.getName());
            if (toolchain instanceof GccCompatibleToolChain) {
                linkTask.getLinkerArgs().addAll("-nodefaultlibs", "-lc");
            }
        } else if (binary instanceof ComponentWithSharedLibrary) {
            final LinkSharedLibrary linkTask = ((ComponentWithSharedLibrary) binary).getLinkTask().get();
            this.logger.info("Configuring link task: {}", linkTask.getName());
            if (toolchain instanceof GccCompatibleToolChain) {
                linkTask.getLinkerArgs().addAll("-nodefaultlibs", "-lc");
            }
        }
    }

    static void configure(Project project, Class<? extends CppComponent> clazz) {
        final CppComponent component = project.getExtensions().getByType(clazz);
        final CBasePlugin helper = new CBasePlugin(project, component);
        component.getBinaries().configureEach(helper::configure);
    }
}
