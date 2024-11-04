
package dev.guillermo.gradle.language.c;

import java.util.List;

import org.gradle.api.component.SoftwareComponent;

/** C Linker extension */
public interface CLinker extends SoftwareComponent {

    /**
     * Flag to avoid using the standard system libraries when linking.
     *
     * @return Flag to avoid using the standard system libraries when linking.
     */
    boolean noDefaultLibraries();

    /**
     * User-defined link options for GCC-compatible toolchains.
     *
     * @return User-defined link options for GCC-compatible toolchains.
     */
    List<String> getGccOptions();

    /**
     * User-defined link options for Visual Studio or Visual C++ toolchains.
     *
     * @return User-defined link options for Visual Studio or Visual C++ toolchains.
     */
    List<String> getVisualCppOptions();
}
