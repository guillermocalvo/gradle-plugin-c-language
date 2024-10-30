
package dev.guillermo.gradle.language.c;

import java.util.Arrays;
import java.util.Optional;

import org.gradle.api.GradleException;

/** Dialect of C */
public enum CDialect {

    /** The default C dialect. */
    DEFAULT_DIALECT,

    /** The default C90 dialect. */
    C90,

    /** The default C99 dialect. */
    C99,

    /** The default C11 dialect. */
    C11,

    /** The default C17 dialect. */
    C17,

    /** The default C23 dialect. */
    C23;

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
