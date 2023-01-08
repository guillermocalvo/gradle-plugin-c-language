
package dev.guillermo.gradle.language.c.plugins;

import java.io.File;
import java.io.IOException;

class CBasePluginTest {

    static File touch(File dir, String path) throws IOException {
        final File canonicalDir = new File(dir.getCanonicalPath());
        final File file = new File(canonicalDir, path);
        file.getParentFile().mkdirs();
        file.createNewFile();
        return file;
    }
}
