
package dev.guillermo.gradle.language.c;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.gradle.api.GradleException;
import org.junit.jupiter.api.Test;

class CDialectTest {

    @Test
    void invalidCDialect() {
        assertThrows(GradleException.class, () -> CDialect.from("INVALID"));
    }
}
