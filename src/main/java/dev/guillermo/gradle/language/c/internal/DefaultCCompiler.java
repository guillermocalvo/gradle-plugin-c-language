
package dev.guillermo.gradle.language.c.internal;

import static dev.guillermo.gradle.language.c.CDialect.DEFAULT_DIALECT;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.inject.Inject;

import dev.guillermo.gradle.language.c.CCompiler;
import dev.guillermo.gradle.language.c.CDialect;

/** Default C compiler. */
public class DefaultCCompiler implements CCompiler {

    private final String name;
    private CDialect dialect;
    private final Map<String, String> macros;
    private boolean failOnWarning;
    private boolean suppressAllWarnings;
    private boolean enableOpenMp;

    /**
     * Creates a new instance.
     *
     * @param name The compiler name.
     */
    @Inject
    public DefaultCCompiler(String name) {
        this.name = name;
        this.dialect = DEFAULT_DIALECT;
        this.macros = new LinkedHashMap<>();
        this.failOnWarning = false;
        this.suppressAllWarnings = false;
        this.enableOpenMp = false;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public CDialect getDialect() {
        return this.dialect;
    }

    /**
     * Sets the dialect of C that should be used for all compilation tasks.
     *
     * @param dialect The dialect of C that should be used for all compilation tasks.
     */
    public void setDialect(String dialect) {
        this.dialect = CDialect.from(dialect).orElse(DEFAULT_DIALECT);
    }

    @Override
    public Map<String, String> getMacros() {
        return this.macros;
    }

    @Override
    public boolean failOnWarning() {
        return this.failOnWarning;
    }

    /**
     * Sets the flag to treat warnings as errors in all compilation tasks.
     *
     * @param failOnWarning The flag to treat warnings as errors in all compilation tasks.
     */
    public void setFailOnWarning(boolean failOnWarning) {
        this.failOnWarning = failOnWarning;
    }

    @Override
    public boolean suppressAllWarnings() {
        return this.suppressAllWarnings;
    }

    /**
     * Sets the flag to suppress all warnings in all compilation tasks.
     *
     * @param suppressAllWarnings The flag to suppress all warnings in all compilation tasks
     */
    public void setSuppressAllWarnings(boolean suppressAllWarnings) {
        this.suppressAllWarnings = suppressAllWarnings;
    }

    @Override
    public boolean enableOpenMp() {
        return this.enableOpenMp;
    }

    /**
     * Sets the flag to enable OpenMP support in all compilation tasks.
     *
     * @param enableOpenMp The flag to enable OpenMP support in all compilation tasks.
     */
    public void setEnableOpenMp(boolean enableOpenMp) {
        this.enableOpenMp = enableOpenMp;
    }
}
