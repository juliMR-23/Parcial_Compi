# The Feline Graph Chronicles

Sistema Java para resolver problemas de grafos mediante algoritmos clásicos, inspirado en las aventuras de Pola, Minerva y Nina contra los villanos Limón y Nero.

> Julián Marín Ramírez
> Juan José Florez López

## Requisitos

- JDK 17 o superior

## Build y ejecución con un solo comando

```bash
# Compilar
javac -d out -sourcepath src src/Main.java

# Ejecutar
java -cp out Main
```

En Windows (PowerShell):

```powershell
javac -d out -sourcepath src src/Main.java; java -cp out Main
```

## Tests automáticos

```bash
# Compilar todo (app + tests)
javac -d out -sourcepath src src/Main.java src/test/TestMission*.java

# Ejecutar todos los tests
java -cp out test.TestMission1
java -cp out test.TestMission2
java -cp out test.TestMission3
java -cp out test.TestMission4
```

En Windows (PowerShell), si `javac` no está en el PATH:

```powershell
$J = "C:\Users\Usuario\.jdks\openjdk-26.0.1\bin"
& "$J\javac.exe" -d out -sourcepath src src/Main.java src/test/TestMission*.java
& "$J\java.exe" -cp out test.TestMission1
& "$J\java.exe" -cp out test.TestMission2
& "$J\java.exe" -cp out test.TestMission3
& "$J\java.exe" -cp out test.TestMission4
```

Los tests cubren: sample cases del enunciado, unreachable, start==end, grafo desconectado, nodo único, ciclos positivos, grafo lineal.

## Estructura del proyecto

```
src/
├── Main.java                          → Punto de entrada
├── algorithms/                        → Lógica pura (sin dependencia de UI)
│   ├── BFSSolver.java                 → BFS con reconstrucción de camino
│   ├── DFSSolver.java                 → DFS iterativo con pila explícita
│   ├── DijkstraSolver.java            → Dijkstra con PriorityQueue
│   ├── FloydWarshallSolver.java       → Floyd-Warshall para maximización
│   ├── BellmanFordSolver.java         → Bellman-Ford para detección de ciclos
│   └── KruskalSolver.java             → Kruskal con Union-Find
├── models/                            → Estructuras de datos
│   ├── Edge.java                      → Arista ponderada (Comparable)
│   ├── Node.java                      → Nodo con posición gráfica
│   ├── Graph.java                     → Grafo dirigido/no dirigido
│   ├── GridMap.java                   → Mapa de grilla con bombas
│   └── UnionFind.java                 → Disjoint Set con path compression
├── parsers/                           → Parseo de entrada por misión
│   ├── MineFieldParser.java           → Parser de grilla de minas
│   ├── MinefieldCase.java
│   ├── DijkstraParser.java            → Parser de grafo ponderado
│   ├── DijkstraCase.java
│   ├── Mission3Parser.java            → Parser de grafo dirigido
│   ├── Mission3Case.java
│   ├── Mission4Parser.java            → Parser de red de cables
│   └── Mission4Case.java
├── ui/                                → Interfaz gráfica (Swing)
│   ├── App.java                       → JFrame con CardLayout
│   ├── LandingPage.java               → Menú principal con cards
│   ├── MissionPanel.java              → Base abstracta para paneles
│   ├── Mission1Panel.java             → BFS & DFS (grilla)
│   ├── Mission2Panel.java             → Dijkstra (grafo)
│   ├── Mission3Panel.java             → Floyd-Warshall & Bellman-Ford
│   ├── Mission4Panel.java             → Kruskal (MST)
│   ├── GraphCanvas.java               → Dibujo de grafos con aristas
│   └── GridCanvas.java                → Dibujo de grilla R×C
├── test/                              → Pruebas automáticas (30 tests)
│   ├── TestMission1.java              → 10 tests (BFS/DFS)
│   ├── TestMission2.java              → 7 tests (Dijkstra)
│   ├── TestMission3.java              → 6 tests (Floyd/Bellman)
│   └── TestMission4.java              → 7 tests (Kruskal)
└── utils/                             → Utilidades de parseo genérico
    ├── InputParser.java               → Tokenizador
    ├── TokenReader.java               → Lector secuencial de tokens
    └── InvalidInputException.java     → Excepción para input malformado
```
