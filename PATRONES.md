# Patrones aplicados

Caso 3 Red municipal de monitoreo de calidad del aire.

## Factory Method

Ubicacion: `src/puesto/PuestoDeMonitoreo.java` (Creator), `PuestoFijoReferencia`,
`PuestoMovil`, `PuestoBajoCosto` (ConcreteCreator). Productos en `src/analizador/`:
`Analizador` (Product), `AnalizadorReferencia`, `AnalizadorOpticoPortatil`,
`AnalizadorBajoCosto` (ConcreteProduct).

Justificacion: cada tipo de puesto instala un analizador distinto para el mismo
contaminante, y lo unico que cambia entre puestos es esa correccion. `procesarJornada`
vive una sola vez en la clase abstracta y orquesta el proceso completo (descartar
invalidas, corregir, verificar validez, promediar) sin saber que analizador le toco:
llama a `crearAnalizador(String contaminante)` y trabaja contra la interfaz.

La formula de correccion vive dentro de cada analizador, no en condicionales dentro del
puesto. Agregar un cuarto tipo de estacion es una subclase nueva mas su analizador; no se
toca ni una linea de `procesarJornada`.

## Prototype

Ubicacion: `src/configuracion/ConfiguracionEstacion.java` y
`src/configuracion/TareaMantenimiento.java`.

Justificacion: configurar una estacion desde cero tomaba dos dias cuando el 90 % es
identico a otra ya existente. La Secretaria mantiene configuraciones modelo por tipo de
zona y al desplegar una estacion nueva se clona el modelo y se ajusta.

`clone()` hace copia profunda de las tres estructuras mutables: el mapa de umbrales
(`LinkedHashMap` nuevo), la lista de contaminantes (`ArrayList` nuevo) y la lista de
`TareaMantenimiento`, donde ademas se clona **cada tarea**, no solo la lista. Una copia
superficial dejaria a la estacion clonada compartiendo objetos con el modelo, que es
justo lo que el enunciado prohibe.

La demostracion imprime el modelo, dos clones y la verificacion de que modificar el umbral
y agregar una tarea en uno de ellos no altera ni el modelo ni el clon hermano.

## Builder

Ubicacion: `src/boletin/BoletinDiario.java` (clase inmutable + `Builder` estatico
anidado).

Justificacion: el boletin tiene cinco campos obligatorios y cinco opcionales. Un
constructor telescopico con diez parametros seria ilegible y permitiria construir un
boletin invalido; el Builder da una API fluida donde cada campo se nombra en la llamada.

`BoletinDiario` es inmutable: todos los campos son `final`, no hay setters y la lista de
resultados se envuelve con `Collections.unmodifiableList` sobre una copia, para que quien
tenga la lista original no pueda mutar el boletin ya publicado.

`build()` es el punto donde se valida y lanza `IllegalStateException` si falta un
obligatorio, si la lista de estaciones esta vacia, o si la categoria es "Danina a la
salud" o peor y no se incluyeron recomendaciones a poblacion sensible. Esa ultima regla es
la que evita repetir el error del boletin publicado con una alerta sin recomendaciones.
