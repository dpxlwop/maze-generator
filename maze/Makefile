APP_NAME=maze

ifndef DESTDIR
DESTDIR=$(HOME)/$(APP_NAME)
endif

all:
	./gradlew build

install:
	./gradlew jlink
	rm -rf $(DESTDIR)
	mkdir -p $(DESTDIR)
	cp -R build/image/* $(DESTDIR)

uninstall:
	rm -rf $(DESTDIR)

clean:
	./gradlew clean
	rm -rf dist

dvi:
	open README.md

dist: clean
	./gradlew jlink
	mkdir -p dist
	tar -czf dist/$(APP_NAME).tar.gz -C build image

tests:
	./gradlew test --rerun-tasks