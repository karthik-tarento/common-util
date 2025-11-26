# Common Utilities

A common utility library for Spring Boot applications.

## Overview

This library provides reusable utility classes and components that can be integrated into any Spring Boot application.

## Project Information

- **Group ID**: io.github.karthik-tarento
- **Artifact ID**: common-util
- **Version**: 1.0.0
- **Spring Boot Version**: 3.2.0
- **Java Version**: 17

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6 or higher

### Building the Project

```bash
mvn clean install
```

### Running Tests

```bash
mvn test
```

## Usage

Add this dependency to your Spring Boot project's `pom.xml`:

```xml
<dependency>
    <groupId>io.github.karthik-tarento</groupId>
    <artifactId>common-util</artifactId>
    <version>1.0.0</version>
</dependency>
```

The library is available on Maven Central, so no additional repository configuration is needed.

## Publishing to Maven Central

This library is published to Maven Central for public use. See [PUBLISHING.md](PUBLISHING.md) for detailed instructions on how to publish new versions.

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## Development

### Project Structure

```
common-util/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── org/
│   │   │       └── igot/
│   │   │           └── common/
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       ├── java/
│       │   └── org/
│       │       └── igot/
│       │           └── common/
│       └── resources/
├── pom.xml
├── LICENSE
└── README.md
```

### Adding Utility Classes

Place your utility classes in the `src/main/java/org/igot/common/` directory following appropriate sub-package structure.

## Support

For issues and questions, please use the GitHub issue tracker.

## Authors

- iGOT Organization

## Acknowledgments

- Built with Spring Boot 3.2
- Uses Apache License 2.0
