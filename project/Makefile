program = Project
deps = Dijkstra.java

UPFLAGS = -usb

$(program).class: $(program).java
	nxjc $< $(deps)

$(program).nxj: $(program).class
	nxjlink -o $@ $(program)

upload: $(program).nxj
	nxjupload $(UPFLAGS) $<

run: $(program).nxj
	nxjupload $(UPFLAGS) -r $<

test:
	nxjpcc Test.java TestDijkstra.java && nxjpc Test

all: test run

clean:
	rm -vf *.class *.nxj

.PHONY: clean all run test
