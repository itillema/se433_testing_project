## Code coverage

Powershell script:

.\gradlew test jacocoTestReport

Invoke-Item app\build\reports\jacoco\test\html\index.html


## Mutation testing (PIT)

Powerschell script:

.\gradlew pitest

Invoke-Item app\build\reports\pitest\index.html

