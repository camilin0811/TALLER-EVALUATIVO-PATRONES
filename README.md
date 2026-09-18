# Taller evaluativo — Red municipal de monitoreo de calidad del aire

Caso 3. Aplicacion de consola en Java que procesa la jornada de 24 horas de cuatro
estaciones, calcula el ICA de cada una y emite el boletin diario.

Java SE 17+ (probado en OpenJDK 21). Sin frameworks ni librerias externas.

## Como compilar y ejecutar

```bash
javac -d out $(find src -name "*.java")
java -cp out app.Main
```

La salida completa esta guardada en `salida.txt`.

## Estructura

```
src/
  analizador/     Analizador (interfaz) y los tres analizadores concretos
  puesto/         PuestoDeMonitoreo (abstracta) y los tres tipos de puesto
  configuracion/  ConfiguracionEstacion y TareaMantenimiento (clonables)
  ica/            tabla de tramos, interpolacion lineal y resultado
  boletin/        BoletinDiario inmutable con su Builder, e impresor
  app/            Main, generador de lecturas y estadisticas de red
```

## Patrones

Los tres patrones obligatorios estan documentados en `PATRONES.md` con su ubicacion
y justificacion.

| Patron | Donde |
|---|---|
| Factory Method | `puesto/PuestoDeMonitoreo.crearAnalizador` |
| Prototype | `configuracion/ConfiguracionEstacion.clone` |
| Builder | `boletin/BoletinDiario.Builder` |

## Que demuestra la ejecucion

1. **Prototype:** se crea la configuracion modelo "Zona residencial" y se clona para dos
   estaciones. Se cambia el umbral de PM2.5 y se agrega una tarea de mantenimiento en una
   de ellas; se imprime el modelo y el clon hermano para evidenciar que quedan intactos.
2. **Factory Method:** se procesan cuatro estaciones — dos fijas de referencia, una movil
   y una de bajo costo — y cada una aplica la correccion de su propio analizador.
3. **Correccion por humedad:** la estacion de bajo costo tiene HR de 88 %, por encima del
   umbral de 75 %, y se imprime lectura cruda y corregida de tres horas.
4. **Dato insuficiente:** Av. Quebradaseca queda con 15 lecturas validas de O3, por debajo
   del minimo de 18, y ese contaminante no participa en el indice.
5. **Builder:** se arma y publica el boletin, y luego se intenta construir uno en categoria
   "Danina a la salud" sin recomendaciones dentro de un `try/catch` para mostrar el mensaje
   de la excepcion.

## Reglas implementadas

- Se descartan lecturas negativas o fuera del rango del analizador, contando cuantas.
- Promedio diario valido solo con 18 o mas lecturas horarias de las 24.
- ICA por interpolacion lineal sobre una tabla de puntos de corte (`ica/TablaIca`), no con
  cadenas de `if` por contaminante.
- ICA de estacion = maximo entre contaminantes validos, con su contaminante critico.
- Ranking por ICA descendente y, a igualdad, por nombre alfabetico.
- Cobertura de la red = porcentaje de datos validos sobre estaciones x contaminantes x 24.

Las lecturas se generan con `Random` de semilla fija, asi que la salida es reproducible.
