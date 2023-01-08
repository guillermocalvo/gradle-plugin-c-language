
package dev.guillermo.gradle.language.c.plugins;

import javax.inject.Inject;

import org.gradle.api.Project;
import org.gradle.api.internal.attributes.ImmutableAttributesFactory;
import org.gradle.language.cpp.CppLibrary;
import org.gradle.language.cpp.plugins.CppLibraryPlugin;
import org.gradle.language.internal.NativeComponentFactory;
import org.gradle.language.nativeplatform.internal.toolchains.ToolChainSelector;
import org.gradle.nativeplatform.TargetMachineFactory;

/** A plugin that produces a native library from C source. */
public class CLibraryPlugin extends CppLibraryPlugin {

    @Inject
    public CLibraryPlugin(
            NativeComponentFactory componentFactory,
            ToolChainSelector toolChainSelector,
            ImmutableAttributesFactory attributesFactory,
            TargetMachineFactory targetMachineFactory) {
        super(componentFactory, toolChainSelector, attributesFactory, targetMachineFactory);
    }

    public void apply(Project project) {
        project.getLogger().info("Applying c-library plugin");
        super.apply(project);
        CBasePlugin.configure(project, CppLibrary.class);
    }
}
