
package dev.guillermo.gradle.language.c.plugins;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.gradle.api.Action;
import org.gradle.api.Project;
import org.gradle.api.file.ConfigurableFileCollection;
import org.gradle.api.file.ConfigurableFileTree;
import org.gradle.api.logging.Logger;
import org.gradle.api.plugins.ExtensionContainer;
import org.gradle.api.provider.ListProperty;
import org.gradle.api.provider.Provider;
import org.gradle.language.BinaryCollection;
import org.gradle.language.cpp.CppApplication;
import org.gradle.language.cpp.CppBinary;
import org.gradle.language.cpp.CppComponent;
import org.gradle.language.cpp.CppExecutable;
import org.gradle.language.cpp.CppLibrary;
import org.gradle.language.cpp.CppSharedLibrary;
import org.gradle.language.cpp.tasks.CppCompile;
import org.gradle.nativeplatform.tasks.AbstractLinkTask;
import org.gradle.nativeplatform.tasks.LinkExecutable;
import org.gradle.nativeplatform.toolchain.GccCompatibleToolChain;
import org.gradle.nativeplatform.toolchain.NativeToolChain;
import org.gradle.nativeplatform.toolchain.VisualCpp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import dev.guillermo.gradle.language.c.CCompiler;
import dev.guillermo.gradle.language.c.internal.DefaultCCompiler;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("unchecked")
class CBasePluginTest {

    static final String MAIN = "main";
    static final Map<String, String> MAIN_SOURCE = Map.of("dir", "src/main/c", "include", "**/*.c");
    static final String COMPILER = "compiler";

    DefaultCCompiler compiler;

    @Mock
    Project project;

    @Mock
    Logger logger;

    @Mock
    ExtensionContainer extensions;

    @Mock
    BinaryCollection<? extends CppBinary> binaryCollection;

    @Mock
    Provider<CppCompile> compileTaskProvider;

    @Mock
    CppCompile compile;

    @Mock
    Provider<LinkExecutable> linkTaskProvider;

    @Mock
    AbstractLinkTask link;

    @Mock
    ConfigurableFileCollection source;

    @Mock
    ConfigurableFileTree from;

    @Mock
    ListProperty<String> compilerArgs;

    @Mock
    ListProperty<String> linkerArgs;

    @Mock
    Map<String, String> macros;

    @BeforeEach
    void setUp() {
        compiler = new DefaultCCompiler(COMPILER);
        when(project.getLogger()).thenReturn(logger);
        when(project.getExtensions()).thenReturn(extensions);
        when(project.fileTree(MAIN_SOURCE)).thenReturn(from);
        when(compileTaskProvider.get()).thenReturn(compile);
        when(compile.getSource()).thenReturn(source);
        when(compile.getMacros()).thenReturn(macros);
        when(compile.getCompilerArgs()).thenReturn(compilerArgs);
        lenient().when(linkTaskProvider.get()).then(x -> link);
        lenient().when(link.getLinkerArgs()).thenReturn(linkerArgs);
    }

    <C extends CppComponent, T extends NativeToolChain> void setUpMocks(
            Class<C> componentClass, Class<T> toolChainClass, CCompiler compiler, CppBinary binary) {
        final C component = mock();
        when(extensions.getByType(componentClass)).thenReturn(component);
        when(component.getName()).thenReturn(MAIN);
        when(component.getBaseName()).thenReturn(mock());
        when(component.getBinaries()).then(x -> binaryCollection);
        when(extensions.getByType(CCompiler.class)).thenReturn(compiler);
        when(binary.getToolChain()).thenReturn(mock(toolChainClass));
        when(binary.getCompileTask()).thenReturn(compileTaskProvider);
        doAnswer(executeBinary(binary)).when(binaryCollection).configureEach(any());
    }

    @Test
    void cAppWithGcc() {
        // Given
        compiler.getMacros().put("FOO", "BAR");
        final CppExecutable binary = mock();
        this.setUpMocks(CppApplication.class, GccCompatibleToolChain.class, compiler, binary);
        when(binary.getLinkTask()).then(x -> linkTaskProvider);
        // When
        CBasePlugin.configure(project, CppApplication.class);
        // Then
        verify(source).setFrom(from);
        verifyNoMoreInteractions(source);
        verify(macros).putAll(compiler.getMacros());
        verifyNoMoreInteractions(macros);
        verify(compilerArgs).addAll("-x", "c");
        verify(compilerArgs).get();
        verifyNoMoreInteractions(compilerArgs);
        verify(linkerArgs).addAll("-nodefaultlibs", "-lc");
        verify(linkerArgs).get();
        verifyNoMoreInteractions(linkerArgs);
    }

    @Test
    void c90AppWithGccFailOnWarning() {
        // Given
        compiler.setDialect("C90");
        compiler.setFailOnWarning(true);
        final CppExecutable binary = mock();
        this.setUpMocks(CppApplication.class, GccCompatibleToolChain.class, compiler, binary);
        when(binary.getLinkTask()).then(x -> linkTaskProvider);
        // When
        CBasePlugin.configure(project, CppApplication.class);
        // Then
        verify(source).setFrom(from);
        verifyNoMoreInteractions(source);
        verify(macros).putAll(compiler.getMacros());
        verifyNoMoreInteractions(macros);
        verify(compilerArgs).addAll("-x", "c");
        verify(compilerArgs).add("-std=c90");
        verify(compilerArgs).add("-Werror");
        verify(compilerArgs).get();
        verifyNoMoreInteractions(compilerArgs);
        verify(linkerArgs).addAll("-nodefaultlibs", "-lc");
        verify(linkerArgs).get();
        verifyNoMoreInteractions(linkerArgs);
    }

    @Test
    void c99AppWithGccSuppressAllWarnings() {
        // Given
        compiler.setDialect("C99");
        compiler.setSuppressAllWarnings(true);
        final CppExecutable binary = mock();
        this.setUpMocks(CppApplication.class, GccCompatibleToolChain.class, compiler, binary);
        when(binary.getLinkTask()).then(x -> linkTaskProvider);
        // When
        CBasePlugin.configure(project, CppApplication.class);
        // Then
        verify(source).setFrom(from);
        verifyNoMoreInteractions(source);
        verify(macros).putAll(compiler.getMacros());
        verifyNoMoreInteractions(macros);
        verify(compilerArgs).addAll("-x", "c");
        verify(compilerArgs).add("-std=c99");
        verify(compilerArgs).add("-w");
        verify(compilerArgs).get();
        verifyNoMoreInteractions(compilerArgs);
        verify(linkerArgs).addAll("-nodefaultlibs", "-lc");
        verify(linkerArgs).get();
        verifyNoMoreInteractions(linkerArgs);
    }

    @Test
    void c11AppWithGccEnableOpenMp() {
        // Given
        compiler.setDialect("C11");
        compiler.setEnableOpenMp(true);
        final CppExecutable binary = mock();
        this.setUpMocks(CppApplication.class, GccCompatibleToolChain.class, compiler, binary);
        when(binary.getLinkTask()).then(x -> linkTaskProvider);
        // When
        CBasePlugin.configure(project, CppApplication.class);
        // Then
        verify(source).setFrom(from);
        verifyNoMoreInteractions(source);
        verify(macros).putAll(compiler.getMacros());
        verifyNoMoreInteractions(macros);
        verify(compilerArgs).addAll("-x", "c");
        verify(compilerArgs).add("-std=c11");
        verify(compilerArgs).add("-fopenmp");
        verify(compilerArgs).get();
        verifyNoMoreInteractions(compilerArgs);
        verify(linkerArgs).addAll("-nodefaultlibs", "-lc");
        verify(linkerArgs).get();
        verifyNoMoreInteractions(linkerArgs);
    }

    @Test
    void c17LibraryWithGcc() {
        // Given
        compiler.setDialect("C17");
        final CppSharedLibrary binary = mock();
        this.setUpMocks(CppLibrary.class, GccCompatibleToolChain.class, compiler, binary);
        when(binary.getLinkTask()).then(x -> linkTaskProvider);
        // When
        CBasePlugin.configure(project, CppLibrary.class);
        // Then
        verify(source).setFrom(from);
        verifyNoMoreInteractions(source);
        verify(macros).putAll(compiler.getMacros());
        verifyNoMoreInteractions(macros);
        verify(compilerArgs).addAll("-x", "c");
        verify(compilerArgs).add("-std=c17");
        verify(compilerArgs).get();
        verifyNoMoreInteractions(compilerArgs);
        verify(linkerArgs).addAll("-nodefaultlibs", "-lc");
        verify(linkerArgs).get();
        verifyNoMoreInteractions(linkerArgs);
    }

    @Test
    void c23LibraryWithGcc() {
        // Given
        compiler.setDialect("C23");
        final CppSharedLibrary binary = mock();
        this.setUpMocks(CppLibrary.class, GccCompatibleToolChain.class, compiler, binary);
        when(binary.getLinkTask()).then(x -> linkTaskProvider);
        // When
        CBasePlugin.configure(project, CppLibrary.class);
        // Then
        verify(source).setFrom(from);
        verifyNoMoreInteractions(source);
        verify(macros).putAll(compiler.getMacros());
        verifyNoMoreInteractions(macros);
        verify(compilerArgs).addAll("-x", "c");
        verify(compilerArgs).add("-std=c23");
        verify(compilerArgs).get();
        verifyNoMoreInteractions(compilerArgs);
        verify(linkerArgs).addAll("-nodefaultlibs", "-lc");
        verify(linkerArgs).get();
        verifyNoMoreInteractions(linkerArgs);
    }

    @Test
    void cAppWithVisualCpp() {
        // Given
        compiler.getMacros().put("FOO", "BAR");
        final CppExecutable binary = mock();
        this.setUpMocks(CppApplication.class, VisualCpp.class, compiler, binary);
        when(binary.getLinkTask()).then(x -> linkTaskProvider);
        // When
        CBasePlugin.configure(project, CppApplication.class);
        // Then
        verify(source).setFrom(from);
        verifyNoMoreInteractions(source);
        verify(macros).putAll(compiler.getMacros());
        verifyNoMoreInteractions(macros);
        verify(compilerArgs).add("/TC");
        verify(compilerArgs).get();
        verifyNoMoreInteractions(compilerArgs);
        verify(linkerArgs).get();
        verifyNoMoreInteractions(linkerArgs);
    }

    @Test
    void c90AppWithVisualCppFailOnWarning() {
        // Given
        compiler.setDialect("C90");
        compiler.setFailOnWarning(true);
        final CppExecutable binary = mock();
        this.setUpMocks(CppApplication.class, VisualCpp.class, compiler, binary);
        when(binary.getLinkTask()).then(x -> linkTaskProvider);
        // When
        CBasePlugin.configure(project, CppApplication.class);
        // Then
        verify(source).setFrom(from);
        verifyNoMoreInteractions(source);
        verify(macros).putAll(compiler.getMacros());
        verifyNoMoreInteractions(macros);
        verify(compilerArgs).add("/TC");
        verify(compilerArgs).add("/Za");
        verify(compilerArgs).add("/WX");
        verify(compilerArgs).get();
        verifyNoMoreInteractions(compilerArgs);
        verify(linkerArgs).get();
        verifyNoMoreInteractions(linkerArgs);
    }

    @Test
    void c99AppWithVisualCppSuppressAllWarnings() {
        // Given
        compiler.setDialect("C99");
        compiler.setSuppressAllWarnings(true);
        final CppExecutable binary = mock();
        this.setUpMocks(CppApplication.class, VisualCpp.class, compiler, binary);
        when(binary.getLinkTask()).then(x -> linkTaskProvider);
        // When
        CBasePlugin.configure(project, CppApplication.class);
        // Then
        verify(source).setFrom(from);
        verifyNoMoreInteractions(source);
        verify(macros).putAll(compiler.getMacros());
        verifyNoMoreInteractions(macros);
        verify(compilerArgs).add("/TC");
        verify(compilerArgs).add("/w");
        verify(compilerArgs).get();
        verifyNoMoreInteractions(compilerArgs);
        verify(linkerArgs).get();
        verifyNoMoreInteractions(linkerArgs);
    }

    @Test
    void c11AppWithVisualCppEnableOpenMp() {
        // Given
        compiler.setDialect("C11");
        compiler.setEnableOpenMp(true);
        final CppExecutable binary = mock();
        this.setUpMocks(CppApplication.class, VisualCpp.class, compiler, binary);
        when(binary.getLinkTask()).then(x -> linkTaskProvider);
        // When
        CBasePlugin.configure(project, CppApplication.class);
        // Then
        verify(source).setFrom(from);
        verifyNoMoreInteractions(source);
        verify(macros).putAll(compiler.getMacros());
        verifyNoMoreInteractions(macros);
        verify(compilerArgs).add("/TC");
        verify(compilerArgs).add("/std:c11");
        verify(compilerArgs).add("/openmp");
        verify(compilerArgs).get();
        verifyNoMoreInteractions(compilerArgs);
        verify(linkerArgs).get();
        verifyNoMoreInteractions(linkerArgs);
    }

    @Test
    void c17LibraryWithVisualCpp() {
        // Given
        compiler.setDialect("C17");
        final CppSharedLibrary binary = mock();
        this.setUpMocks(CppApplication.class, VisualCpp.class, compiler, binary);
        when(binary.getLinkTask()).then(x -> linkTaskProvider);
        // When
        CBasePlugin.configure(project, CppApplication.class);
        // Then
        verify(source).setFrom(from);
        verifyNoMoreInteractions(source);
        verify(macros).putAll(compiler.getMacros());
        verifyNoMoreInteractions(macros);
        verify(compilerArgs).add("/TC");
        verify(compilerArgs).add("/std:c17");
        verify(compilerArgs).get();
        verifyNoMoreInteractions(compilerArgs);
        verify(linkerArgs).get();
        verifyNoMoreInteractions(linkerArgs);
    }

    @Test
    void c23LibraryWithVisualCpp() {
        // Given
        compiler.setDialect("C23");
        final CppSharedLibrary binary = mock();
        this.setUpMocks(CppApplication.class, VisualCpp.class, compiler, binary);
        when(binary.getLinkTask()).then(x -> linkTaskProvider);
        // When
        CBasePlugin.configure(project, CppApplication.class);
        // Then
        verify(source).setFrom(from);
        verifyNoMoreInteractions(source);
        verify(macros).putAll(compiler.getMacros());
        verifyNoMoreInteractions(macros);
        verify(compilerArgs).add("/TC");
        verify(compilerArgs).add("/std:clatest");
        verify(compilerArgs).get();
        verifyNoMoreInteractions(compilerArgs);
        verify(linkerArgs).get();
        verifyNoMoreInteractions(linkerArgs);
    }

    @Test
    void cBinaryWithUnknownToolchain() {
        // Given
        this.setUpMocks(CppApplication.class, NativeToolChain.class, compiler, mock());
        // When
        CBasePlugin.configure(project, CppApplication.class);
        // Then
        assertThat(compiler.getName()).isEqualTo(COMPILER);
        verify(source).setFrom(from);
        verifyNoMoreInteractions(source);
        verify(macros).putAll(compiler.getMacros());
        verifyNoMoreInteractions(macros);
        verify(compilerArgs).get();
        verifyNoMoreInteractions(compilerArgs);
        verifyNoMoreInteractions(linkerArgs);
    }

    static Answer<Object> executeBinary(CppBinary binary) {
        return invocation -> {
            final Action<CppBinary> action = invocation.getArgument(0);
            action.execute(binary);
            return null;
        };
    }

    static File touch(File dir, String path) throws IOException {
        final File canonicalDir = new File(dir.getCanonicalPath());
        final File file = new File(canonicalDir, path);
        file.getParentFile().mkdirs();
        file.createNewFile();
        return file;
    }
}
