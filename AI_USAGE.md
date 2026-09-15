# AI_USAGE.md — Reporte de uso de herramientas de IA

## Herramientas utilizadas

Se utilizó **opencode** (asistente de IA basado en LLM) como herramienta principal de desarrollo a lo largo de todo el proyecto. La IA generó código, depuró errores, diseñó la estructura del proyecto y refactorizó componentes. El rol del equipo fue definir requisitos, revisar resultados, probar ejecución y tomar decisiones de diseño.

## Prompts decisivos

### 1. Scaffolding completo del proyecto
**Prompt**: "You are a senior Java software architect... build the full project scaffolding for 'The Feline Graph Chronicles'... flat package structure under src/... algorithms package must NOT import Swing/JavaFX..."

**Por qué fue necesario**: El proyecto tenía una fecha límite apretada y la estructura de 32 archivos debía ser coherente desde el inicio. Un error temprano en la arquitectura (por ejemplo, que los algoritmos dependieran de Swing) habría requerido reescribir todo. La IA generó la estructura completa en una sola sesión, lo que habría tomado horas de diseño manual.

### 2. Corrección de errores de compilación
**Prompt**: "Todos los paneles tienen errores, no corre el Main"

**Por qué fue necesario**: Después del scaffolding, el proyecto no compilaba. La IA revisó los 32 archivos, encontró `setLineWrapStyle` (método inexistente en JTextArea) y que `InvalidInputException` era checked pero `Supplier.get()` no puede lanzar checked exceptions. Sin esta corrección, nada funcionaba.

### 3. Algoritmos con información visual para la UI
**Prompt**: "Ahora haz que los algoritmos puedan entregar la información de grafos, caminos, etc para graficar correctamente en UI"

**Por qué fue necesario**: Los algoritmos solo retornaban el resultado numérico (distancia, costo), pero la UI necesitaba los caminos reales, las aristas del MST, los nodos en ciclos, etc. La IA reescribió los 6 algoritmos para que almacenaran y expusieran esta información附加.

## Salidas incorrectas o subóptimas

### 1. InvalidInputException como checked exception
**Problema**: La IA creó `InvalidInputException extends Exception` (checked). Pero los parsers se llaman dentro de lambdas `Supplier<String>` en los paneles, y `Supplier.get()` no puede declarar checked exceptions. Todos los paneles tenían errores de compilación.

**Corrección**: Se cambió a `extends RuntimeException`. La IA original diseñó la excepción como checked pensando en que los parsers la declaraban con `throws`, pero no consideró que el patrón `execute(Supplier)` lo impedía.

### 2. rank sin inicializar en UnionFind
**Problema**: El constructor de `UnionFind` inicializaba `parent` pero olvidaba `rank = new int[n]`. En Misión 4, al ejecutar Kruskal, se producía `NullPointerException` al intentar acceder a `rank[rx]`.

**Corrección**: Se agregó `rank = new int[n]` en el constructor. Este es un error clásico de Java donde los arrays de int se inicializan en 0 por defecto, pero solo si se crea el array. La IA generó el código sin verificar que ambos campos estaban inicializados.

### 3. Long.MAX_VALUE en maximización (Misión 3)
**Problema**: La IA inicializó Floyd-Warshall y Bellman-Ford con `Long.MAX_VALUE` como "infinito". Pero en maximización, el infinito debe ser `Long.MIN_VALUE` (un valor muy bajo que siempre se supera). Con MAX_VALUE, la condición `newDist > distances[j]` nunca se cumple porque任何值 + peso < MAX_VALUE.

**Corrección**: El equipo detectó el problema y cambió a `Long.MIN_VALUE`. La IA luego actualizó Mission3Panel para que comparara correctamente contra MIN_VALUE en vez de MAX_VALUE.

## Lo que cada miembro aprendió

### Julián
- **DFS iterativo**: No sabía que DFS recursivo causaría StackOverflow en grillas de 10^6 celdas. Aprendió a usar `ArrayDeque` como pila explícita y por qué es necesario para problemas de escala.
- **Bellman-Ford para detección de ciclos**: Aprendió que la iteración V (después de V-1 relajaciones) permite identificar nodos afectados por ciclos de ganancia positiva.
- **Union-Find**: No conocía path compression ni union by rank. Aprendió que hacen las operaciones prácticamente O(1).

### Valentina
- **Floyd-Warshall para maximización**: Solo conocía la versión de minimización. Aprendió que cambiando la inicialización a MIN_VALUE y la comparación a `>`, el mismo algoritmo encuentra el camino más pesado.
- **Swing internals**: Aprendió a crear bordes redondeados con `AbstractBorder` y `RoundRectangle2D`, y a usar `GridBagLayout` para centrar componentes que deben envolver texto.
- **Separación UI/algoritmos**: Aprendió la importancia de que `algorithms/` no importe `javax.swing`, lo que permite testear algoritmos sin abrir ventanas.
