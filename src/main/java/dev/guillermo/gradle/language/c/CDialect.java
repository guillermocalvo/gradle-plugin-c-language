
package dev.guillermo.gradle.language.c;

import static dev.guillermo.gradle.language.c.CDialect.values;

import java.util.Arrays;
import java.util.Optional;

import org.gradle.api.GradleException;

/** Dialect of C */
public enum CDialect {
    DEFAULT_DIALECT, C90, C99, C11, C17;

    /**
     * Tries to convert a string into a valid dialect of C.
     *
     * @param dialect The string to convert
     * @return An optional dialect of C.
     */
    public static Optional<CDialect> from(String dialect) {
        try {
            return Optional.of(dialect).map(String::toUpperCase).map(CDialect::valueOf);
        } catch (IllegalArgumentException e) {
            throw new GradleException(
                    "Invalid C dialect: "
                            + dialect
                            + ". Available dialects are: "
                            + Arrays.toString(values()));
        }
    }
}
