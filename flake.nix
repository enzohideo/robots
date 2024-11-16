{
  inputs = {
    lejos = {
      url = "github:enzohideo/lejos-nix";
      inputs.nixpkgs.follows = "nixpkgs";
    };
  };

  outputs =
    {
      self,
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
      packages = eachSystem (
        system:
        let
          pkgs = nixpkgs.legacyPackages.${system};
        in
        {
          vscodium = (
            pkgs.vscode-with-extensions.override {
              vscode = pkgs.vscodium;
              vscodeExtensions = with pkgs.vscode-extensions; [
                redhat.java
              ];
            }
          );
        }
      );

      devShells = eachSystem (
        system:
        let
          pkgs = nixpkgs.legacyPackages.${system};
          vscodium = self.packages.${system}.vscodium;
        in
        {
          default = lejos.devShells.${system}.lejos-nxj.overrideAttrs (prevAttrs: {
            buildInputs = prevAttrs.buildInputs ++ [
              pkgs.jdt-language-server
            ];
            CLASSPATH = "${prevAttrs.NXJ_HOME}/lib/nxt/classes.jar";
          });
          vscodium = lejos.devShells.${system}.lejos-nxj.overrideAttrs (prevAttrs: {
            buildInputs = prevAttrs.buildInputs ++ [
              vscodium
              pkgs.jdk
            ];
          });
        }
      );
    };
}
