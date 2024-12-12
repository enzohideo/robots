# Execução do projeto inteiro

`make run`

# Execução de partes individuais

- `make compass`
- `make deliver`
- `make sonar`
- `make claw`
- `make identifyline`

# Testes

`make test`

# Classes

## Align, ColorPID, LightPID

Alinhamento com a linha usando 2 sensores de luz/cor e PID.

## CompassAlign

Alinhamento com bússola

## IdentifyLine

Para quando chegar na linha

## Sonar

Anda para frente até encontrar um cano e identifica se é o pequeno ou
alto

## Claw

Abre/fecha garra e identifica a cor do cano

## Hardware

Guarda uma configuração global das portas dos sensores, motores, tamanho
da roda, etc.

## Project

Código com o loop principal do projeto.
