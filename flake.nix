{
  inputs = {
    lejos = {
      url = "github:enzohideo/lejos-nix";
      inputs.nixpkgs.follows = "nixpkgs";
    };
  };

  outputs =
    {
      systems,
      nixpkgs,
      lejos,
      ...
    }:
    let
      inherit (nixpkgs) lib;
      eachSystem = lib.genAttrs (import systems);
    in
    {
      devShell = eachSystem (
        system:
        let
          pkgs = nixpkgs.legacyPackages.${system};
        in
        lejos.devShells.${system}.lejos-nxj.overrideAttrs (prevAttrs: {
          buildInputs =
            prevAttrs.buildInputs
            ++ (with pkgs; [
              jdt-language-server
            ]);
          CLASSPATH = "${prevAttrs.NXJ_HOME}/lib/nxt/classes.jar";
        })
      );
    };
}
