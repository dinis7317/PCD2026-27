# ProjetoPCD_26_27_IscoT

Este estado do projeto esta focado na **1a entrega** (fases 1 a 3):
- GUI de sensores e atuadores
- estrutura de areas e hierarquia de eventos
- simulacao local sem servidor, sem sockets e sem concorrencia/distribuicao

## Compilar

```bash
cd /home/dinoc/reps/ProjetoPCD_26_27
rm -rf bin
mkdir -p bin
javac -d bin $(find src -name "*.java")
```

## Executar simulador da 1a entrega

Abre os 4 sensores, os 4 atuadores e o centro de comando:

```bash
cd /home/dinoc/reps/ProjetoPCD_26_27
java -cp bin iscot.client.RunEverything
```

## Nota

Nesta entrega intermédia nao ha servidor nem message broker ativo.