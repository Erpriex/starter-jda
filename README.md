# StarterJDA
This repository provides a minimal Discord bot skeleton built with JDA.<br />
It is mainly focused on offering a simple and structured way to implement commands, making it easier to expand your bot with new features.

## Requirements
This project requires the following to get started:
- Java version `8` or higher
- Maven version `3.9.11` or higher

## Getting Started

### Clone the repository
```bash
git clone https://github.com/Erpriex/starter-jda.git
cd starter-jda/
```

### Setup
1. Go to `src/main/resources`
2. Copy `config-sample.json` and rename it to `config.json`
3. Update the values in `config.json` to match your environment

### Compilation
```bash
mvn clean package
```

### Running
```bash
java -jar target/StarterJDA-1.0-SNAPSHOT.jar
```