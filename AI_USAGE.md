# AI_USAGE.md — Reporte de uso de herramientas de IA

## Herramientas utilizadas

Se utilizaron dos asistentes de IA a lo largo del proyecto:

- **opencode**, para el scaffolding inicial del proyecto, la generación de la estructura de paquetes y la corrección de errores de compilación.
- **Claude**, para el diseño y depuración de los algoritmos (BFS, DFS, Dijkstra), el diseño de los parsers de entrada, y la corrección de bugs lógicos específicos (no solo de compilación) encontrados al probar contra los samples del enunciado.

El rol del equipo fue definir requisitos, revisar cada resultado generado, probarlo contra los ejemplos del enunciado y tomar las decisiones finales de diseño. Ningún fragmento de código se aceptó sin antes entenderlo y verificar que pasara los samples.

## Prompts decisivos

### 1. Scaffolding completo del proyecto
**Prompt**: "You are a senior Java software architect... build the full project scaffolding for 'The Feline Graph Chronicles'... flat package structure under src/... algorithms package must NOT import Swing/JavaFX..."

**Por qué fue necesario**: El proyecto tenía una fecha límite apretada y la estructura de archivos debía ser coherente desde el inicio, respetando el requisito de la Sección 7.2 de que los algoritmos no dependan de la GUI. Un error temprano en la arquitectura habría obligado a reescribir todo más adelante.

### 2. Corrección de errores de compilación
**Prompt**: "Todos los paneles tienen errores, no corre el Main"

**Por qué fue necesario**: Después del scaffolding, el proyecto no compilaba por el uso de un método inexistente en `JTextArea` y por una excepción `checked` (`InvalidInputException`) declarada dentro de una lambda `Supplier<String>`, que en Java no puede lanzar excepciones checked.

### 3. Diseño de los parsers de entrada (Misiones 1 y 2)
**Prompt (resumen de la conversación)**: pedí ayuda para que las clases `BFSSolver`/`DFSSolver` no parsearan el texto cada una por su cuenta, sino que recibieran los datos ya convertidos desde un parser común, y luego pedí lo mismo para Dijkstra.

**Por qué fue necesario**: Mi primer intento hacía que cada solver leyera el texto crudo por separado, lo que iba a duplicar la lógica de parseo y arriesgaba que BFS y DFS terminaran corriendo sobre datos ligeramente distintos si corregía un bug de parseo en una clase y se me olvidaba en la otra. Con ayuda de la IA diseñé el patrón: `TokenReader` (cursor sobre los tokens) → `MinefieldCase`/`DijkstraCase` (objeto con los datos ya parseados) → `MinefieldParser`/`DijkstraParser` (arma la lista de casos una sola vez) → los solvers solo reciben el caso ya parseado. Esto también permite testear los algoritmos con JUnit sin pasar por texto plano, como pide la Sección 7.2.

### 4. Depuración de un bug de DFS que no aparecía por errores de compilación
**Prompt (resumen)**: "el DFS me está devolviendo 28 en vez de 32 del sample" → luego, tras confirmar que el grid de bombas parseado era correcto, seguimos revisando la lógica interna del algoritmo.

**Por qué fue necesario**: El programa compilaba y corría sin errores, pero daba un resultado numérico incorrecto — el tipo de bug más difícil de encontrar porque no hay ningún stack trace que señale la línea. Ver más abajo el detalle de la causa y la corrección.

## Salidas incorrectas o subóptimas

### 1. `InvalidInputException` como checked exception
**Problema**: Se creó `InvalidInputException extends Exception` (checked). Pero los parsers se llaman dentro de lambdas `Supplier<String>` en los paneles, y `Supplier.get()` no puede declarar excepciones checked. Todos los paneles quedaron con errores de compilación.

**Corrección**: Se cambió a `extends RuntimeException`.

### 2. `rank` sin inicializar en `UnionFind`
**Problema**: El constructor de `UnionFind` inicializaba `parent` pero no `rank`, produciendo `NullPointerException` al ejecutar Kruskal en la Misión 4.

**Corrección**: Se agregó `rank = new int[n]` en el constructor.

### 3. Marcar `visited` al insertar en la pila del DFS, en vez de al procesar (Misión 1)
**Problema**: El DFS iterativo daba **28** en vez de **32** contra el sample del enunciado, sin ningún error de compilación ni excepción — el grid de bombas parseado era correcto (se verificó imprimiéndolo celda por celda), así que el bug no estaba en el parser. La causa real era marcar `visited[nr][nc] = true` en el momento de hacer `push()` de un vecino a la pila, en vez de marcarlo al momento de sacarlo con `pop()`. Esto hacía que una celda quedara "bloqueada" para otros caminos apenas se descubría, aunque todavía no hubiera sido realmente procesada — lo que no reproduce el comportamiento de un DFS recursivo real, y le "cortaba camino" al algoritmo antes de tiempo.

**Corrección**: Se movió la marca de `visited` al momento del `pop()`, agregando un chequeo `if (visited[cur[0]][cur[1]]) continue;` justo después de sacar el elemento de la pila, para descartar entradas que ya fueron procesadas por otro camino que llegó primero. Con este cambio el resultado coincidió exactamente con el esperado (32).

## Aprendizaje guiado: diseño de las clases de parser

Antes de este proyecto no tenía un patrón claro para separar "leer el input" de "resolver el problema" — mi primer instinto era que cada algoritmo (BFS, DFS) leyera el texto crudo por su cuenta. Con la guía de la IA entendí varios conceptos que aplico ahora en todas las misiones:

- **Por qué duplicar el parseo es peligroso**: si BFS y DFS parsean el mismo input cada uno por separado, un bug corregido en una clase y no en la otra puede hacer que ambos algoritmos corran sobre grids sutilmente distintos, dando resultados inconsistentes sin que sea obvio por qué.
- **El patrón "tokenizar con cursor"**: en vez de dividir el input en líneas (que no funciona bien aquí porque el enunciado exige tolerar saltos de línea y espacios extra), aprendí a tratar todo el input como un flujo plano de tokens (`String.split("\\s+")`) y recorrerlo con un cursor (`TokenReader.nextInt()`) que no le importa en qué línea estaba cada número originalmente.
- **Separar "el dato parseado" del "algoritmo que lo usa"**: aprendí a crear una clase simple tipo `MinefieldCase`/`DijkstraCase` que solo guarda los datos ya convertidos (grid de bombas, nodo inicio/destino, lista de adyacencia), de modo que el parser se prueba por separado del algoritmo, y los algoritmos se pueden testear con JUnit pasándoles un caso armado a mano, sin necesidad de texto ni de abrir la GUI — que es justo lo que exige la Sección 7.2 del enunciado.
- **Diferencia entre bug de compilación y bug de lógica**: el caso del DFS que daba 28 en vez de 32 me enseñó que un programa puede compilar y correr perfectamente y aun así dar resultados incorrectos por un error puramente lógico (el orden en que se marca `visited`). Aprendí a aislar ese tipo de bug imprimiendo estructuras intermedias (el grid de bombas, el orden de visita) para descartar el parser antes de sospechar del algoritmo, en vez de revisar todo el código a la vez.

## Lo que cada miembro aprendió

### Julián
- **Bellman-Ford para detección de ciclos**: Aprendió que la iteración N (después de N-1 relajaciones) permite identificar nodos afectados por ciclos de ganancia positiva.
- **Union-Find**: No conocía path compression ni union by rank. Aprendió que hacen las operaciones prácticamente O(1).

### Juan José Flórez
- **Separación de parseo y algoritmo**: aprendió el patrón `TokenReader` → `Case` (objeto parseado) → `Parser` → `Solver`, y por qué evita que dos algoritmos que deben correr sobre el mismo input (BFS y DFS) terminen procesando datos inconsistentes.
- **Depuración de bugs lógicos silenciosos**: aprendió a diferenciar un error de compilación (bloquea todo, es obvio) de un error lógico (el programa corre pero da un resultado numérico incorrecto), y a aislarlo imprimiendo estructuras intermedias en vez de revisar todo el código de una vez.
- **Orden de exploración con pila explícita**: aprendió por qué, para respetar un orden fijo de vecinos (arriba, abajo, izquierda, derecha) usando una pila (LIFO), hay que empujar los vecinos en orden inverso — y por qué marcar "visitado" al insertar en la pila en vez de al procesar puede alterar el resultado del recorrido.
