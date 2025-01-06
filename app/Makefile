.DEFAULT_GOAL := build-run

update:
	./gradlew dependencyUpdates
clean:
	./gradlew clean
build:
	./gradlew build
installDist:
	./gradlew installDist
dev:
	./gradlew run
lint:
	./gradlew checkstyleMain
test:
	./gradlew test
report:
	./gradlew jacocoTestReport

build-run: clean build installDist lint test report

.PHONY: build