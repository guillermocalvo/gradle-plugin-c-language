
package dev.guillermo.gradle.language.c.internal;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dev.guillermo.gradle.language.c.CLinker;

/** Default C linker. */
public class DefaultCLinker implements CLinker {

    private final String name;
    private boolean noDefaultLibraries;
    private List<String> gccOptions;
    private List<String> visualCppOptions;

    /**
     * Creates a new instance.
     *
     * @param name The linker name.
     */
    @Inject
    public DefaultCLinker(String name) {
        this.name = name;
        this.gccOptions = new ArrayList<>();
        this.visualCppOptions = new ArrayList<>();
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public boolean noDefaultLibraries() {
        return this.noDefaultLibraries;
    }

    /**
     * Sets the flag to avoid using the standard system libraries when linking.
     *
     * @param noDefaultLibraries The flag to avoid using the standard system libraries when linking.
     */
    public void setNoDefaultLibraries(boolean noDefaultLibraries) {
        this.noDefaultLibraries = noDefaultLibraries;
    }

    @Override
    public List<String> getGccOptions() {
        return this.gccOptions;
    }

    @Override
    public List<String> getVisualCppOptions() {
        return this.visualCppOptions;
    }
}
