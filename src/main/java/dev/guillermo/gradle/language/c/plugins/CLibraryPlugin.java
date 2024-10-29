
package dev.guillermo.gradle.language.c.plugins;

import javax.inject.Inject;

import org.gradle.api.Project;
import org.gradle.api.internal.attributes.ImmutableAttributesFactory;
import org.gradle.language.cpp.CppLibrary;
import org.gradle.language.cpp.plugins.CppLibraryPlugin;
import org.gradle.language.internal.NativeComponentFactory;
import org.gradle.language.nativeplatform.internal.toolchains.ToolChainSelector;
import org.gradle.nativeplatform.TargetMachineFactory;

import dev.guillermo.gradle.language.c.CCompiler;
import dev.guillermo.gradle.language.c.internal.DefaultCCompiler;

/** A plugin that produces a native library from C source. */
public class CLibraryPlugin extends CppLibraryPlugin {

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
    public CLibraryPlugin(
            NativeComponentFactory componentFactory,
            ToolChainSelector toolChainSelector,
            ImmutableAttributesFactory attributesFactory,
            TargetMachineFactory targetMachineFactory) {
        super(componentFactory, toolChainSelector, attributesFactory, targetMachineFactory);
        this.componentFactory = componentFactory;
    }

    @Override
    public void apply(Project project) {
        project.getLogger().info("Applying c-library plugin");
        super.apply(project);
        // Add the top-level compiler and extension
        final CCompiler compiler = componentFactory.newInstance(CCompiler.class, DefaultCCompiler.class, "compiler");
        project.getExtensions().add(CCompiler.class, "compiler", compiler);
        CBasePlugin.configure(project, CppLibrary.class);
    }
}
