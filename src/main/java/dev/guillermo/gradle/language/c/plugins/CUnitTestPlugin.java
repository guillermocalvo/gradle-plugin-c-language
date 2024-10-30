
package dev.guillermo.gradle.language.c.plugins;

import javax.inject.Inject;

import org.gradle.api.Project;
import org.gradle.api.internal.attributes.ImmutableAttributesFactory;
import org.gradle.api.model.ObjectFactory;
import org.gradle.language.internal.NativeComponentFactory;
import org.gradle.language.nativeplatform.internal.toolchains.ToolChainSelector;
import org.gradle.nativeplatform.TargetMachineFactory;
import org.gradle.nativeplatform.test.cpp.CppTestSuite;
import org.gradle.nativeplatform.test.cpp.plugins.CppUnitTestPlugin;

/**
 * A plugin that sets up the infrastructure for testing C binaries using a simple test executable.
 */
public class CUnitTestPlugin extends CppUnitTestPlugin {

    /**
     * Creates a new instance.
     *
     * @param componentFactory The component factory.
     * @param toolChainSelector The toolchain selector.
     * @param objectFactory The object factory.
     * @param attributesFactory The attributes factory.
     * @param targetMachineFactory The target machine factory.
     */
    @Inject
    public CUnitTestPlugin(
            NativeComponentFactory componentFactory,
            ToolChainSelector toolChainSelector,
            ObjectFactory objectFactory,
            ImmutableAttributesFactory attributesFactory,
            TargetMachineFactory targetMachineFactory) {
        super(
                componentFactory,
                toolChainSelector,
                objectFactory,
                attributesFactory,
                targetMachineFactory);
    }

    @Override
    public void apply(Project project) {
        project.getLogger().info("Applying c-unit-test plugin");
        super.apply(project);
        CBasePlugin.configure(project, CppTestSuite.class);
    }
}
