# Changelog
Todos los cambios notables de este proyecto se documentan en este archivo.  
El formato sigue [Keep a Changelog](https://keepachangelog.com/en/1.0.0/) y este proyecto adhiere a [Semantic Versioning](https://semver.org/).

## [Unreleased]

## [0.1.35] - 2026-02-05
### Cambiado
- `PantallaJuego`: cierre del flujo online y estabilidad general de la partida.
### Corregido
- `PantallaJuego`: sincronizacion visual al renderizar estados remotos.

## [0.1.34] - 2026-02-04
### Agregado
- `PantallaJuego`: recepcion de estado remoto para reflejar posiciones.
### Cambiado
- `PantallaMenu`: inicio de sesion y entrada a partida online.

## [0.1.33] - 2026-02-03
### Agregado
- `Jugador` y `Obstaculo`: datos para actualizarse desde red.
### Cambiado
- `PantallaJuego`: loop de update basado en estado recibido.

## [0.1.32] - 2026-02-02
### Agregado
- `HiloCliente`: manejo de mensajes entrantes/salientes.
### Corregido
- `HiloCliente`: validacion basica de paquetes y tiempos.

## [0.1.31] - 2026-02-01
### Agregado
- `HiloCliente`: conexion y ciclo principal de comunicacion.
### Cambiado
- `Controlador`: encolado de acciones del jugador para enviar.

## [0.1.30] - 2026-01-31
### Cambiado
- `PantallaJuego`: separacion entre logica local y remota.
### Corregido
- `Main`: limpieza de dependencias y warnings.

## [0.1.29] - 2026-01-30
### Agregado
- `Main`: bandera/selector para modo local u online.
### Cambiado
- `ControlJugador`: preparacion para sincronizacion online.

## [0.1.28] - 2026-01-29
### Agregado
- `HiloCliente`: primer intercambio de mensajes con el servidor.
### Cambiado
- `ControlJugador`: separacion de input local para futura sincronizacion.

## [0.1.27] - 2026-01-28
### Agregado
- `assets`: limpieza y orden de recursos del modo local.
### Cambiado
- `PantallaJuego`: reglas de partida local 1v1.

## [0.1.26] - 2026-01-27
### Cambiado
- `PantallaMenu`: pulido del flujo local.
### Corregido
- `PantallaMenu`: detalles de UI y texto.

## [0.1.25] - 2026-01-26
### Cambiado
- `Jugador` y `Obstaculo`: balance (velocidad, tamanos).
### Corregido
- `PantallaJuego`: limites del escenario y posiciones iniciales.

## [0.1.24] - 2026-01-25
### Agregado
- `PantallaJuego`: loop local completo.
### Cambiado
- `PantallaJuego`: integracion de entidades en el render.

## [0.1.23] - 2026-01-24
### Agregado
- `PantallaMenu`: menu local con opciones basicas.
### Cambiado
- `PantallaMenu`: navegacion hacia juego local.

## [0.1.22] - 2026-01-23
### Agregado
- `ControlJugador`: acciones principales del jugador local.
### Corregido
- `ControlJugador`: lectura de input para evitar doble activacion.

## [0.1.21] - 2026-01-22
### Agregado
- `Obstaculo`: comportamiento base en modo local.
### Cambiado
- `Obstaculo`: ajustes de velocidad y tamanos.

## [0.1.20] - 2026-01-21
### Agregado
- `Jugador`: estado y movimiento en modo local.
### Cambiado
- `PantallaJuego`: colisiones basicas y limites.

## [0.1.19] - 2026-01-20
### Agregado
- `PantallaJuego`: ciclo de actualizacion local.
### Cambiado
- `PantallaJuego`: separacion de logica y render.

## [0.1.18] - 2026-01-19
### Agregado
- `assets`: recursos minimos para pruebas locales.
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
- `Main`: ajustes del ciclo de vida del juego.

## [0.1.15] - 2026-01-16
### Agregado
- `core`: estructura de paquetes para modo local.
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
- `PantallaJuego`: base del modo local 1v1.
### Cambiado
- `PantallaJuego`: preparacion del loop local.

## [0.1.10] - 2026-01-11
### Agregado
- `PantallaJuego`: primera escena de pruebas.
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
- `Main`: primeros archivos del juego local.
### Cambiado
- `Main`: ajustes del entorno de desarrollo.

## [0.1.6] - 2026-01-07
### Agregado
- `PantallaMenu`: boceto del flujo local 1v1.
### Cambiado
- `core`: organizacion de paquetes.

## [0.1.5] - 2026-01-06
### Agregado
- `build.gradle`: configuracion basica de gradle.
### Cambiado
- `settings.gradle`: estandarizacion de nombres y rutas.

## [0.1.4] - 2026-01-05
### Agregado
- `core`: estructura base del proyecto local.
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
- `README.md`: definicion del alcance 1v1 local.
### Cambiado
- `README.md`: documentacion de ideas iniciales.

## [0.1.0] - 2026-01-01
### Agregado
- `Main`: inicio del proyecto 1v1 totalmente local.
