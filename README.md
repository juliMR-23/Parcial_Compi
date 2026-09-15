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

## Decisiones de diseño

### Algoritmos

| Misión | Algoritmo | Por qué |
|--------|-----------|---------|
| 1 | BFS + DFS | BFS garantiza camino más corto en grilla no ponderada. DFS explora el grafo sin recursión (requerido por restricción del enunciado). |
| 2 | Dijkstra | Grafo ponderado no negativo. El camino más corto entre origen y destino. |
| 3 | Floyd-Warshall + Bellman-Ford | Floyd calcula caminos máximos entre todos los pares. Bellman-Ford detecta ciclos de ganancia positiva en la V-ésima iteración. |
| 4 | Kruskal + Union-Find | MST de costo mínimo. Union-Find con path compression y union by rank para eficiencia near-constant. |

### DFS iterativo vs recursivo

DFS es **iterativo** usando `ArrayDeque` como pila explícita. Razón: el enunciado pide que funcione con grillas de hasta 10^6 celdas. DFS recursivo causaría `StackOverflowError` en grillas grandes porque cada celda crea un frame de llamada. La versión iterativa usa heap en vez de stack.

### Estructuras de datos

- **Edge**: almacena peso `int` (individual), acumulación con `long` (para evitar overflow en caminos largos)
- **Union-Find**: path compression + union by rank → O(α(n)) por operación (prácticamente O(1))
- **PriorityQueue** en Dijkstra: O((V + E) log V)
- **Floyd-Warshall**: O(V^3) en tiempo, O(V^2) en espacio. Se usa maximización con `Long.MIN_VALUE` como "no alcanzable"
- **Bellman-Ford**: O(V × E). La iteración V detecta nodos afectados por ciclos positivos

### Interfaz gráfica

- **Swing** puro (java.awt + javax.swing). Sin librerías externas.
- **GraphCanvas**: dibuja nodos en círculo con layout automático, aristas con flechas (dirigido) o líneas, pesos, y resaltado de caminos/ciclos/MST
- **GridCanvas**: dibuja grilla R×C con celdas coloreadas (bomba, inicio, fin, camino BFS, orden DFS)
- **CardLayout** para navegación entre landing y misiones
- **JComboBox** para seleccionar entre múltiples cases en cada misión

## Limitaciones conocidas

- **Tamaño de grilla para visualización**: grillas grandes (>50×50) se renderizan pero las celdas son muy pequeñas para leer. El algoritmo funciona correctamente independientemente del tamaño.
- **Layout automático de grafos**: los nodos se distribuyen en círculo. No usa layout fuerza-dirigida ni jerárquico, así que grafos muy densos pueden tener aristas cruzadas.
- **Pesos negativos en Dijkstra**: el algoritmo no soporta aristas con pesos negativos (requerido por el enunciado). Si se ingresa peso negativo, el resultado puede ser incorrecto.
- **Múltiples aristas entre mismos nodos**: el parser las acepta. Kruskal las maneja correctamente (se eliminan en sort). Dijkstra puede tener resultados inesperados.
- **Sin persistencia**: los resultados se pierden al cerrar la aplicación. No hay guardado de inputs ni resultados.
- **Una ventana**: la UI abre en una sola ventana fija. No es redimensionable a pantalla completa de forma óptima.
