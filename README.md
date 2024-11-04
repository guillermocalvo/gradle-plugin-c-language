
[![Build Status](https://github.com/guillermocalvo/gradle-plugin-c-language/workflows/Build/badge.svg)](https://github.com/guillermocalvo/gradle-plugin-c-language/actions?query=workflow%3ABuild)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=guillermocalvo_gradle-plugin-c-language&metric=alert_status)](https://sonarcloud.io/dashboard?id=guillermocalvo_gradle-plugin-c-language)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=guillermocalvo_gradle-plugin-c-language&metric=coverage)](https://sonarcloud.io/component_measures?id=guillermocalvo_gradle-plugin-c-language&metric=coverage&view=list)
[![Gradle Plugin Portal](https://img.shields.io/gradle-plugin-portal/v/dev.guillermo.gradle.c-application)](https://plugins.gradle.org/search?term=dev.guillermo)


# C Language Gradle Plugin

This is a Gradle Plugin for Building C Projects.


## Getting Started

Use the [plugins DSL](https://docs.gradle.org/current/userguide/plugins.html#sec:plugins_block) and
[learn how to apply plugins to subprojects](https://docs.gradle.org/current/userguide/plugins.html#sec:subprojects_plugins_dsl).


## C Application Plugin

The C Application Plugin provides the tasks, configurations and conventions for building a C application.

It is currently based on [the C++ Application Plugin](https://docs.gradle.org/current/userguide/cpp_application_plugin.html).

### Usage

```gradle
plugins {
  id "dev.guillermo.gradle.c-application" version "0.5.0"
}
```


## C Library Plugin

The C Library Plugin provides the tasks, conventions and conventions for building a C library.

It is currently based on [the C++ Library Plugin](https://docs.gradle.org/current/userguide/cpp_library_plugin.html).

### Usage

```gradle
plugins {
  id "dev.guillermo.gradle.c-library" version "0.5.0"
}
```


## C Unit Test Plugin

The C Unit Test Plugin provides the tasks, configurations and conventions for integrating with a C executable-based testing framework.

It is currently based on [the C++ Unit Test Plugin](https://docs.gradle.org/current/userguide/cpp_unit_test_plugin.html).

### Usage

```gradle
plugins {
  id "dev.guillermo.gradle.c-unit-test" version "0.5.0"
}
```


## Releases

This library adheres to [Semantic Versioning](https://semver.org/).

Artifacts are available in [Gradle Plugin Portal](https://plugins.gradle.org/search?term=dev.guillermo).


## Author

Copyright 2024 [Guillermo Calvo](https://github.com/guillermocalvo)

[![](https://guillermo.dev/assets/images/thumb.png)](https://guillermo.dev/)


## License

This library is licensed under the *Apache License, Version 2.0* (the "License");
you may not use it except in compliance with the License.

You may obtain a copy of the License at <http://www.apache.org/licenses/LICENSE-2.0>

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an
"AS IS" BASIS, **WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND**, either express or implied.

See the License for the specific language governing permissions and limitations under the License.


### Permitted

- **Commercial Use**: You may use this library and derivatives for commercial purposes.
- **Modification**: You may modify this library.
- **Distribution**: You may distribute this library.
- **Patent Use**: This license provides an express grant of patent rights from contributors.
- **Private Use**: You may use and modify this library without distributing it.

### Required

- **License and Copyright Notice**: If you distribute this library you must include a copy of the license and copyright
  notice.
- **State Changes**: If you modify and distribute this library you must document changes made to this library.

### Forbidden

- **Trademark use**: This license does not grant any trademark rights.
- **Liability**: The library author cannot be held liable for damages.
- **Warranty**: This library is provided without any warranty.
