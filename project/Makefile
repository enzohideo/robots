program = Project
deps = Align.java Dijkstra.java Claw.java PathFinder.java Sonar.java

UPFLAGS = -usb

$(program).class: $(program).java
	nxjc $< $(deps)

$(program).nxj: $(program).class
	nxjlink -o $@ $(program)

upload: $(program).nxj
	nxjupload $(UPFLAGS) $<

run: $(program).nxj
	nxjupload $(UPFLAGS) -r $<

Test.class: TestDijkstra.java
	nxjpcc Test.java $^

test: Test.class
	nxjpc Test

all: test run

clean:
	rm -vf *.class *.nxj

.PHONY: clean all run upload test
