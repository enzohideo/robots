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
          lejos-home = lejos.packages.${system}.lejos-nxj;
          lejos-nxj-classes = "${lejos-home}/lib/nxt/classes.jar";
        in
        {
          default = lejos.devShells.${system}.lejos-nxj.overrideAttrs (prevAttrs: {
            CLASSPATH = lejos-nxj-classes;
          });
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
