
package dev.guillermo.gradle.language.c;

import java.util.List;
import java.util.Map;

import org.gradle.api.component.SoftwareComponent;

/** C Compiler extension */
public interface CCompiler extends SoftwareComponent {

    /**
     * The Dialect of C that should be used for all compilation tasks.
     *
     * @return The Dialect of C that should be used for all compilation tasks.
     */
    CDialect getDialect();

    /**
     * Macros that should be defined for all compilation tasks.
     *
     * @return Macros that should be defined for all compilation tasks.
     */
    Map<String, String> getMacros();

    /**
     * Flag to treat warnings as errors in all compilation tasks.
     *
     * @return Flag to treat warnings as errors in all compilation tasks.
     */
    boolean failOnWarning();

    /**
     * Flag to suppress all warnings in all compilation tasks.
     *
     * @return Flag to suppress all warnings in all compilation tasks.
     */
    boolean suppressAllWarnings();

    /**
     * Flag to enable OpenMP support in all compilation tasks.
     *
     * @return Flag to enable OpenMP support in all compilation tasks.
     */
    boolean enableOpenMp();

    /**
     * User-defined compile options for GCC-compatible toolchains.
     *
     * @return User-defined compile options for GCC-compatible toolchains.
     */
    List<String> getGccOptions();

    /**
     * User-defined compile options for Visual Studio or Visual C++ toolchains.
     *
     * @return User-defined compile options for Visual Studio or Visual C++ toolchains.
     */
    List<String> getVisualCppOptions();
}
