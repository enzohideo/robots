{ lejos-nxj-src }:
lejos-nxj-src.overrideAttrs (prevAttrs: {
  patches = prevAttrs.patches ++ [
    ./do-not-exit.patch
  ];
})
