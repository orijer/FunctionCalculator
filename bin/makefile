# Define directories
SRC_DIR := src
BIN_DIR := bin

# Define source and class files
SOURCES := $(shell find $(SRC_DIR) -name '*.java')
CLASSES := $(patsubst $(SRC_DIR)/%.java,$(BIN_DIR)/%.class,$(SOURCES))

# Define the compiler
JC := javac

# Default target
all: directories compile

# Create bin directory if it does not exist
directories:
	mkdir -p $(BIN_DIR)

# Compile all .java files
compile: $(CLASSES)

# Rule to compile .java files to .class files
$(BIN_DIR)/%.class: $(SRC_DIR)/%.java
	$(JC) -d $(BIN_DIR) $<

# Clean up build artifacts
clean:
	rm -rf $(BIN_DIR)

# Phony targets
.PHONY: all directories compile clean
