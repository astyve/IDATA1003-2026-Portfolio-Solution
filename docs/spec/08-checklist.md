# 08 – Delivery Checklist

Source: PDF §7.

## Build and structure
- [ ] Maven project with a clean, correct directory structure
- [ ] `mvn compile` succeeds without errors
- [ ] `mvn test` runs all tests without failures
- [ ] Runs from both the IDE and the command line (`java -jar`)

## Version control
- [ ] Central repo on GitHub
- [ ] Regular commits with short, concise messages
- [ ] Branches used where appropriate
- [ ] Tags match the `pom.xml` version and the application version

## Unit tests
- [ ] Descriptive test names
- [ ] Each test documents what is tested and the expected result
- [ ] Arrange-Act-Assert
- [ ] Positive and negative cases
- [ ] Sensible coverage (UI excluded)

## Code quality
- [ ] JavaDoc on **all** public methods, including getters/setters
- [ ] Robust: validation and sensible exception types (not only `IllegalArgumentException`)
- [ ] Descriptive English names
- [ ] Logical package structure
- [ ] Low coupling, high cohesion, layered (UI ↔ logic separated)
- [ ] Lambdas/streams used where natural
- [ ] CheckStyle (Google) clean

## Functional coverage
- [ ] FR1-1 … FR1-4
- [ ] FR2-1 … FR2-9
- [ ] At least one extension (EXT-x or own)

## Report and declarations
- [ ] 2500–3000 words, template structure
- [ ] UML diagrams (use case, class, sequence)
- [ ] AI log with at least one rejected/corrected suggestion per phase
- [ ] AI declaration form filled in
- [ ] Code comments where substantial AI help was used
