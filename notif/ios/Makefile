.PHONY: all generate build install clean

# Default target: run everything
all: generate build install

# Generate Xcode project using XcodeGen
generate:
	xcodegen generate

# Build the app for iOS Simulator
build:
	xcodebuild build -sdk iphonesimulator

# Install the app on the currently booted simulator
install:
	xcrun simctl install booted ./build/Debug-iphonesimulator/NotificationTest.app/

# Clean the build directory
clean:
	rm -rf build NotificationTest.xcodeproj
