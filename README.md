# Proyecto Pokemon

La aplicación **Proyecto Pokemon** es una aplicación nativa desarrollada en Kotlin. Utiliza diversas implementaciones de Firebase, como Firebase Crashlytics, Analytics, Authentication y Real-time Database. Además, hace uso de las siguientes librerías y herramientas: Coroutines, Glide, Room, inyección de dependencias con Hilt, Retrofit, Shimmer y Mock para pruebas unitarias.

## Descripción

La aplicación es un juego que brinda una base de datos de pokémones. Al iniciar la aplicación por primera vez, se reproduce un video con todas las openings de las 10 temporadas, mientras se genera la base de datos de pokémones de forma interna. Este video solo se verá hasta que la base de datos se haya completado. Si se desactiva o acelera, no habrá tiempo suficiente para crear la base de datos interna, por lo que se volverá a mostrar el video hasta que se pueda generar de forma correcta.

Una posible solución a este problema podría ser crear una API REST que genere la base de datos de pokémones y consumirla en tiempo real desde la aplicación.

Una vez que finaliza el video, se puede iniciar sesión a través de la cuenta de Google para ver la lista de regiones disponibles.

## Funcionalidades

### Pantalla de Selección de Región

Al seleccionar una región, se muestra una pantalla con dos botones: "Crear Equipo" y "Cargar Equipo Externo". Si existen equipos creados en esta región, se mostrarán debajo de los botones. Esta lista de equipos permite borrarlos desde esta pantalla. Al hacer clic en un equipo, se muestra la información del equipo y dos nuevos botones: "Borrar" y "Compartir". El botón "Compartir" generará un número que otro usuario puede copiar en su aplicación como equipo externo para generar una copia propia del equipo creado.

### Pantalla de Creación de Equipo

El botón "Crear Equipo" lleva a una pantalla con una lista de pokémones. Cada pokémon se muestra con su nombre, ID, tipos y descripción (disponibles en inglés y español, y se muestran de forma aleatoria). El fondo de pantalla tiene el color relacionado con su tipo principal. Al tocar un pokémon, se selecciona y se muestra en la parte superior derecha. En la parte superior de la pantalla, se puede dar un nombre al equipo. El botón "Guardar" se habilitará si el nombre no está vacío y si la lista de pokémones seleccionados tiene más de 2 pokémones. Además, no se permite seleccionar más de 6 pokémones para el equipo.

## Arquitectura y Testing

La aplicación sigue la arquitectura MVVM (Modelo-Vista-ViewModel). Se han implementado pruebas unitarias utilizando Mockito para garantizar la calidad del código. También se ha configurado un sistema de integración continua (CI) para prevenir que se suba código que no compile y código que no pase las pruebas unitarias.

La aplicación está desarrollada siguiendo los estándares de Clean Architecture y los principios SOLID.

La aplicación se encuentra en Debug debido a que se trata de un challenge.
## Instalación

1. Clona el repositorio desde GitHub.
2. Importa el proyecto en Android Studio.
3. Ejecuta la aplicación en un emulador o dispositivo Android.

## Contribuciones

Las contribuciones son bienvenidas. Si tienes alguna mejora o corrección, por favor envía un pull request.
