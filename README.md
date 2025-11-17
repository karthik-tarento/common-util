# Common Utilities

A common utility library for Spring Boot applications.

## Overview

This library provides reusable utility classes and components that can be integrated into any Spring Boot application.

## Project Information

- **Group ID**: org.igot
- **Artifact ID**: common-util
- **Version**: 1.0.0-SNAPSHOT
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
    <groupId>org.igot</groupId>
    <artifactId>common-util</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

## Publishing

This library is designed to be published to GitHub Packages or Maven Central for public use.

### Publishing to GitHub Packages

1. Configure your GitHub token in `~/.m2/settings.xml`:

```xml
<settings>
    <servers>
        <server>
            <id>github</id>
            <username>YOUR_GITHUB_USERNAME</username>
            <password>YOUR_GITHUB_TOKEN</password>
        </server>
    </servers>
</settings>
```

2. Deploy the library:

```bash
mvn deploy
```

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
