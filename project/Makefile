PROGRAM := Project
PROGRAM_DEPS := Claw.java Sonar.java
PROGRAM_DEPS += $(addprefix align/,Align.java ColorPID.java)
PROGRAM_DEPS += $(addprefix dijkstra/,Dijkstra.java PathFinder.java)

TEST_DIR := test
TEST := $(TEST_DIR)/Test
TEST_DEPS := $(addprefix $(TEST_DIR)/,TestDijkstra.java)

UPFLAGS := -usb

# ===================================================== #

$(PROGRAM).class: $(PROGRAM).java $(PROGRAM_DEPS)
	nxjc $^

$(PROGRAM).nxj: $(PROGRAM).class
	nxjlink -o $@ $(PROGRAM)

upload: $(PROGRAM).nxj
	nxjupload $(UPFLAGS) $<

run: $(PROGRAM).nxj
	nxjupload $(UPFLAGS) -r $<

# ===================================================== #

$(TEST).class: $(TEST).java $(TESTS)
	nxjpcc $^

test: $(TEST).class
	nxjpc $(TEST)

# ===================================================== #

all: test run

clean:
	rm -vf *.class *.nxj

.PHONY: clean all test run upload
