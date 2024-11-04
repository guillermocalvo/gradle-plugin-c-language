
package dev.guillermo.gradle.language.c.plugins;

import javax.inject.Inject;

import org.gradle.api.Project;
import org.gradle.api.internal.attributes.ImmutableAttributesFactory;
import org.gradle.language.cpp.CppApplication;
import org.gradle.language.cpp.plugins.CppApplicationPlugin;
import org.gradle.language.internal.NativeComponentFactory;
import org.gradle.language.nativeplatform.internal.toolchains.ToolChainSelector;
import org.gradle.nativeplatform.TargetMachineFactory;

import dev.guillermo.gradle.language.c.CCompiler;
import dev.guillermo.gradle.language.c.CLinker;
import dev.guillermo.gradle.language.c.internal.DefaultCCompiler;
import dev.guillermo.gradle.language.c.internal.DefaultCLinker;

/** A plugin that produces a native application from C source. */
public class CApplicationPlugin extends CppApplicationPlugin {

    private final NativeComponentFactory componentFactory;

    /**
     * Creates a new instance.
     *
     * @param componentFactory The component factory.
     * @param toolChainSelector The toolchain selector.
     * @param attributesFactory The attributes factory.
     * @param targetMachineFactory The target machine factory.
     */
    @Inject
    public CApplicationPlugin(
            NativeComponentFactory componentFactory,
            ToolChainSelector toolChainSelector,
            ImmutableAttributesFactory attributesFactory,
            TargetMachineFactory targetMachineFactory) {
        super(componentFactory, toolChainSelector, attributesFactory, targetMachineFactory);
        this.componentFactory = componentFactory;
    }

    @Override
    public void apply(Project project) {
        project.getLogger().info("Applying c-application plugin");
        super.apply(project);
        // Add the top-level compiler, linker and extensions
        final CCompiler compiler = componentFactory.newInstance(CCompiler.class, DefaultCCompiler.class, "compiler");
        final CLinker linker = componentFactory.newInstance(CLinker.class, DefaultCLinker.class, "linker");
        project.getExtensions().add(CCompiler.class, "compiler", compiler);
        project.getExtensions().add(CLinker.class, "linker", linker);
        CBasePlugin.configure(project, CppApplication.class);
    }
}
