PROGRAM_DIR := src/main
PROGRAM_NAME := Project
PROGRAM := $(PROGRAM_DIR)/$(PROGRAM_NAME)

TEST_DIR := src/test
TEST_NAME := Test
TEST := $(TEST_DIR)/$(TEST_NAME)

COMMON_DEPS += $(addprefix $(PROGRAM_DIR)/deliver/,Node.java Arena.java)

PROGRAM_DEPS := $(COMMON_DEPS)
PROGRAM_DEPS += $(addprefix $(PROGRAM_DIR)/hardware/,Hardware.java)
PROGRAM_DEPS += $(addprefix $(PROGRAM_DIR)/align/,Align.java CompassAlign.java ColorPID.java LightPID.java)
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

$(PROGRAM_DIR)/claw/Claw.class: $(PROGRAM_DIR)/claw/Claw.java $(PROGRAM_DEPS)
	nxjc $^

Claw.nxj: $(PROGRAM_DIR)/claw/Claw.class $(PROGRAM_DEPS)
	nxjlink -o $@ claw/Claw --classpath $(PROGRAM_DIR)

claw: Claw.nxj
	nxjupload $(UPFLAGS) -r $<


# ===================================================== #

$(PROGRAM_DIR)/deliver/Deliver.class: $(PROGRAM_DIR)/deliver/Deliver.java $(PROGRAM_DEPS)
	nxjc $^

Deliver.nxj: $(PROGRAM_DIR)/deliver/Deliver.class $(PROGRAM_DEPS)
	nxjlink -o $@ deliver/Deliver --classpath $(PROGRAM_DIR)

deliver: Deliver.nxj
	nxjupload $(UPFLAGS) -r $<

# ===================================================== #

$(PROGRAM_DIR)/sonar/Sonar.class: $(PROGRAM_DIR)/sonar/Sonar.java $(PROGRAM_DEPS)
	nxjc $^

Sonar.nxj: $(PROGRAM_DIR)/sonar/Sonar.class $(PROGRAM_DEPS)
	nxjlink -o $@ sonar/Sonar --classpath $(PROGRAM_DIR)

sonar: Sonar.nxj
	nxjupload $(UPFLAGS) -r $<

# ===================================================== #

$(PROGRAM_DIR)/align/CompassAlign.class: $(PROGRAM_DIR)/align/CompassAlign.java $(PROGRAM_DEPS)
	nxjc $^

CompassAlign.nxj: $(PROGRAM_DIR)/align/CompassAlign.class $(PROGRAM_DEPS)
	nxjlink -o $@ align/CompassAlign --classpath $(PROGRAM_DIR)

compass: CompassAlign.nxj
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
