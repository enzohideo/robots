program = Project
deps = Dijkstra.java

$(program).class: $(program).java
	nxjc $< $(deps)

$(program).nxj: $(program).class
	nxjlink -o $@ $(program)

upload: $(program).nxj
	nxjupload -r $<

test:
	nxjpcc Test.java TestDijkstra.java && nxjpc Test

all: test upload

clean:
	rm -vf *.class *.nxj

.PHONY: clean all run test
