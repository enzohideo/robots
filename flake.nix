{
  inputs = {
    lejos = {
      url = "github:enzohideo/lejos-nix";
      inputs.nixpkgs.follows = "nixpkgs";
    };
  };

  outputs = {
    systems,
    nixpkgs,
    lejos,
    ...
  }: let
    inherit (nixpkgs) lib;
    eachSystem = lib.genAttrs (import systems);
  in  {
    devShell = eachSystem (system: lejos.devShells.${system}.lejos-nxj);
  };
}
