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
          lejos-nxj = lejos.packages.${system}.lejos-nxj;
          lejos-nxj-src = import ./nix/lejos-nxj-src.nix {
            lejos-nxj-src = lejos.packages.${system}.lejos-nxj-src;
          };
          lejos-nxj-classes = "${lejos-nxj}/lib/nxt/classes.jar";
        in
        rec {
          default = lejos.devShells.${system}.lejos-nxj.overrideAttrs (prevAttrs: {
            CLASSPATH = lejos-nxj-classes;
          });
          experimental = default.overrideAttrs {
            buildInputs = [ lejos-nxj-src ];
          };
          jdtls = self.devShells.${system}.default.overrideAttrs (prevAttrs: {
            buildInputs = prevAttrs.buildInputs ++ [
              pkgs.jdt-language-server
            ];
          });
          vscodium = lejos.devShells.${system}.lejos-nxj.overrideAttrs (
            prevAttrs:
            let
              vscodium = self.packages.${system}.vscodium;
              vscode-settings = ''
                {
                  "java.project.referencedLibraries": [
                    "${lejos-nxj-classes}"
                  ]
                }
              '';
            in
            {
              buildInputs = prevAttrs.buildInputs ++ [
                vscodium
                pkgs.jdk
              ];
              shellHook =
                prevAttrs.shellHook
                + ''
                  mkdir -pv ".vscode"
                  [ ! -f .vscode/settings.json ] \
                    && printf '${vscode-settings}' > .vscode/settings.json
                '';
            }
          );
        }
      );
    };
}
