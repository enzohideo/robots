PROGRAM_DIR := src/main
PROGRAM_NAME := Project
PROGRAM := $(PROGRAM_DIR)/$(PROGRAM_NAME)

TEST_DIR := src/test
TEST_NAME := Test
TEST := $(TEST_DIR)/$(TEST_NAME)

COMMON_DEPS += $(addprefix $(PROGRAM_DIR)/deliver/,Node.java Arena.java)

PROGRAM_DEPS := $(COMMON_DEPS)
PROGRAM_DEPS += $(addprefix $(PROGRAM_DIR)/hardware/,Hardware.java)
PROGRAM_DEPS += $(addprefix $(PROGRAM_DIR)/align/,Align.java ColorPID.java LightPID.java)
PROGRAM_DEPS += $(addprefix $(PROGRAM_DIR)/deliver/,Deliver.java)
PROGRAM_DEPS += $(addprefix $(PROGRAM_DIR)/claw/,Claw.java)
PROGRAM_DEPS += $(addprefix $(PROGRAM_DIR)/sonar/,Sonar.java)

TEST_DEPS := $(COMMON_DEPS)
TEST_DEPS += $(addprefix $(TEST_DIR)/,TestArena.java TestNode.java)

UPFLAGS := -usb

# ===================================================== #

$(PROGRAM).class: $(PROGRAM).java $(PROGRAM_DEPS)
	nxjc $^

$(PROGRAM).nxj: $(PROGRAM).class
	nxjlink -o $@ $(PROGRAM_NAME) --classpath $(PROGRAM_DIR)

upload: $(PROGRAM).nxj
	nxjupload $(UPFLAGS) $<

run: $(PROGRAM).nxj
	nxjupload $(UPFLAGS) -r $<

# ===================================================== #

$(TEST).class: $(TEST).java $(TEST_DEPS) $(PROGRAM_DEPS)
	nxjpcc -cp '$(PROGRAM_DIR):$(TEST_DIR)' $(TEST).java $(TEST_DEPS)

test: $(TEST).class
	nxjpc -cp '$(PROGRAM_DIR):$(TEST_DIR)' $(TEST_NAME)

# ===================================================== #

all: test run

clean:
	find . -name \*.class -type f -delete -print
	find . -name \*.nxj -type f -delete -print

.PHONY: clean all test run upload
