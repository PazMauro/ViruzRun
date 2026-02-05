# Changelog
Todos los cambios notables de este proyecto se documentan en este archivo.  
El formato sigue [Keep a Changelog](https://keepachangelog.com/en/1.0.0/) y este proyecto adhiere a [Semantic Versioning](https://semver.org/).

## [Unreleased]

## [0.1.35] - 2026-02-05
### Cambiado
- `PantallaJuego`: cierre del flujo online del servidor.
### Corregido
- `HiloServidor`: ajustes finales de sincronizacion y orden de mensajes.

## [0.1.34] - 2026-02-04
### Agregado
- `HiloServidor`: broadcast de estado a clientes conectados.
### Cambiado
- `HiloServidor`: ciclos de envio/recepcion consistentes.

## [0.1.33] - 2026-02-03
### Agregado
- `Controlador`: validaciones de estado recibido desde clientes.
### Cambiado
- `Jugador`: normalizacion de posiciones.

## [0.1.32] - 2026-02-02
### Agregado
- `Cliente`: administracion de clientes conectados.
### Corregido
- `HiloServidor`: manejo de desconexiones y reintentos.

## [0.1.31] - 2026-02-01
### Agregado
- `HiloServidor`: accept loop y canales base.
### Cambiado
- `Controlador`: encolado de eventos del servidor.

## [0.1.30] - 2026-01-31
### Cambiado
- `Main`: separacion de logica local y online.
### Corregido
- `Main`: limpieza de warnings y dependencias.

## [0.1.29] - 2026-01-30
### Agregado
- `Controlador`: estructura para flujo de eventos por red.
### Cambiado
- `Main`: preparacion del servidor para modo online.

## [0.1.28] - 2026-01-29
### Agregado
- `HiloServidor`: primer intercambio de mensajes con el cliente.
### Cambiado
- `Jugador` y `Obstaculo`: ajustes para rol autoritativo del servidor.

## [0.1.27] - 2026-01-28
### Agregado
- `Constantes`: valores base para el servidor dedicado.
### Cambiado
- `Main`: ajustes del loop para separar render de logica.

## [0.1.26] - 2026-01-27
### Cambiado
- `PantallaJuego`: pulido de reglas locales antes del online.
### Corregido
- `PantallaJuego`: detalles de reglas 1v1 local.

## [0.1.25] - 2026-01-26
### Cambiado
- `Jugador` y `Obstaculo`: ajustes de balance local.
### Corregido
- `PantallaJuego`: limites del escenario y posiciones iniciales.

## [0.1.24] - 2026-01-25
### Agregado
- `PantallaJuego`: loop local estable para 1v1.
### Cambiado
- `PantallaJuego`: integracion de entidades en el flujo local.

## [0.1.23] - 2026-01-24
### Agregado
- `PantallaMenu`: menu local con opciones basicas.
### Cambiado
- `PantallaMenu`: navegacion entre menu y juego.

## [0.1.22] - 2026-01-23
### Agregado
- `ControlJugador`: control de entradas locales para reglas.
### Corregido
- `ControlJugador`: lectura de input para evitar doble activacion.

## [0.1.21] - 2026-01-22
### Agregado
- `Obstaculo`: comportamiento base local.
### Cambiado
- `Obstaculo`: ajustes de velocidad y tamanos.

## [0.1.20] - 2026-01-21
### Agregado
- `Jugador`: estado local y posiciones.
### Cambiado
- `PantallaJuego`: colisiones basicas y limites.

## [0.1.19] - 2026-01-20
### Agregado
- `PantallaJuego`: logica de actualizacion local.
### Cambiado
- `PantallaJuego`: separacion de logica y render.

## [0.1.18] - 2026-01-19
### Agregado
- `assets`: recursos minimos para pruebas.
### Cambiado
- `Main`: carga de recursos locales.

## [0.1.17] - 2026-01-18
### Agregado
- `PantallaMenu` y `PantallaJuego`: esqueleto de pantallas.
### Cambiado
- `Main`: navegacion basica entre pantallas.

## [0.1.16] - 2026-01-17
### Agregado
- `Main`: configuracion de ventana y resolucion.
### Cambiado
- `Main`: ajustes del ciclo de vida.

## [0.1.15] - 2026-01-16
### Agregado
- `core`: estructura de paquetes.
### Cambiado
- `build.gradle`: layout inicial del proyecto.

## [0.1.14] - 2026-01-15
### Agregado
- `build.gradle`: tareas gradle basicas.
### Corregido
- `gradle.properties`: detalles de ejecucion local.

## [0.1.13] - 2026-01-14
### Agregado
- `settings.gradle`: plantilla gdx-liftoff.
### Cambiado
- `build.gradle`: nombres de proyecto y modulos.

## [0.1.12] - 2026-01-13
### Agregado
- `.gitignore`: estructura inicial de archivos.
### Cambiado
- `README.md`: limpieza de archivos generados.

## [0.1.11] - 2026-01-12
### Agregado
- `PantallaJuego`: base del flujo local 1v1.
### Cambiado
- `PantallaJuego`: preparacion del loop local.

## [0.1.10] - 2026-01-11
### Agregado
- `PantallaJuego`: escena de pruebas y reglas base.
### Cambiado
- `Jugador`: ajustes de escalas y tamanos.

## [0.1.9] - 2026-01-10
### Agregado
- `Jugador` y `Obstaculo`: estructura de entidades local.
### Cambiado
- `assets`: preparacion de carpetas.

## [0.1.8] - 2026-01-09
### Agregado
- `build.gradle`: configuracion inicial del proyecto.
### Cambiado
- `gradle.properties`: dependencias base.

## [0.1.7] - 2026-01-08
### Agregado
- `Main`: primeros archivos del servidor (fase local).
### Cambiado
- `Main`: ajustes del entorno de desarrollo.

## [0.1.6] - 2026-01-07
### Agregado
- `PantallaMenu`: boceto del flujo 1v1 local.
### Cambiado
- `core`: organizacion de paquetes.

## [0.1.5] - 2026-01-06
### Agregado
- `build.gradle`: configuracion basica de gradle.
### Cambiado
- `settings.gradle`: estandarizacion de nombres y rutas.

## [0.1.4] - 2026-01-05
### Agregado
- `core`: estructura base del proyecto.
### Cambiado
- `.project`: ajustes iniciales del workspace.

## [0.1.3] - 2026-01-04
### Agregado
- `README.md`: creacion del repositorio y archivos base.
### Cambiado
- `build.gradle`: primeros ajustes del build.

## [0.1.2] - 2026-01-03
### Agregado
- `assets`: preparacion de recursos y carpetas.
### Cambiado
- `settings.gradle`: ajustes de configuracion inicial.

## [0.1.1] - 2026-01-02
### Agregado
- `README.md`: definicion del alcance del juego local 1v1.
### Cambiado
- `README.md`: documentacion de ideas iniciales.

## [0.1.0] - 2026-01-01
### Agregado
- `Main`: inicio del proyecto 1v1 local (sin online).
