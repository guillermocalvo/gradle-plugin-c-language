
package dev.guillermo.gradle.language.c.plugins;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

class TestHelper {

    static File touch(File dir, String path) throws IOException {
        final File canonicalDir = new File(dir.getCanonicalPath());
        final File file = new File(canonicalDir, path);
        file.getParentFile().mkdirs();
        file.createNewFile();
        return file;
    }

    static File writeFile(File dir, String filePath, String... lines) throws IOException {
        final File file = touch(dir, filePath);
        try (Writer writer = new FileWriter(file)) {
            for (String line : lines) {
                writer.write(line);
                writer.write("\n");
            }
        }
        return file;
    }
}
