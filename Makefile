.DEFAULT_GOAL := build-run

clean:
	make -C app clean
build:
	make -C app build
installDist:
	make -C app installDist
dev:
	make -C app dev
lint:
	make -C app lint
test:
	make -C app test
report:
	make -C app report

build-run: clean build installDist lint test report

.PHONY: build
