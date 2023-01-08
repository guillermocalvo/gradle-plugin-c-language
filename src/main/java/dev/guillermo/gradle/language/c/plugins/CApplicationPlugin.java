
package dev.guillermo.gradle.language.c.plugins;

import javax.inject.Inject;

import org.gradle.api.Project;
import org.gradle.api.internal.attributes.ImmutableAttributesFactory;
import org.gradle.language.cpp.CppApplication;
import org.gradle.language.cpp.plugins.CppApplicationPlugin;
import org.gradle.language.internal.NativeComponentFactory;
import org.gradle.language.nativeplatform.internal.toolchains.ToolChainSelector;
import org.gradle.nativeplatform.TargetMachineFactory;

/** A plugin that produces a native application from C source. */
public class CApplicationPlugin extends CppApplicationPlugin {

    @Inject
    public CApplicationPlugin(
            NativeComponentFactory componentFactory,
            ToolChainSelector toolChainSelector,
            ImmutableAttributesFactory attributesFactory,
            TargetMachineFactory targetMachineFactory) {
        super(componentFactory, toolChainSelector, attributesFactory, targetMachineFactory);
    }

    public void apply(Project project) {
        project.getLogger().info("Applying c-application plugin");
        super.apply(project);
        CBasePlugin.configure(project, CppApplication.class);
    }
}
