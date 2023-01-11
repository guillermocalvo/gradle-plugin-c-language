
package dev.guillermo.gradle.language.c;

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
}
