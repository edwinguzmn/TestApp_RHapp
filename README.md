# Android, TestApp_RHapp. 

Este proyecto consiste en una aplicación móvil dedicada para el cuidado de las mascotas, algunas de las principales funcionalidades son: 

- verificación y validación de cuenta en tiempo real. 
- chat general en tiempo real. 
- agenda para eventos dinámica 
- sección de mascotas personalizable.

## Screenshots

### Mobile App (Android)

### Ingreso/inicio
![Captura1](https://github.com/edwinguzmn/TestApp_RHapp/assets/61489964/166580a3-fe01-4a26-a70d-869afc22cc1f)
### funcionalidades
![Captura2](https://github.com/edwinguzmn/TestApp_RHapp/assets/61489964/fa28fb5d-0d7a-4816-84d7-8a8aeaf82c25)

## Librerías/herramientas

Este proyecto utiliza bibliotecas y herramientas como:
- Google firabase: (Autentificación y base de datos.)
- Android espresso: automatización de pruebas unitarias. 
- Android studio:   para las aplicaciones Android.
- figma: Prototipado de UX/UI. 
- [Electron](http://electron.atom.io) to package the Desktop App
- [flux](https://facebook.github.io/flux) to organize the data flow management

### Componentes

El verdadero interés del proyecto está en cómo se han estructurado los componentes para compartir la mayor parte de su lógica y solo redefinir lo que es específico de cada dispositivo.

Básicamente, cada componente tiene una "Clase" principal que hereda una "Clase" base que contiene toda la lógica. Luego, el componente principal importa una función de renderizado diferente que se seleccionó durante la compilación. La herramienta de compilación utiliza la extensión de archivo `.ios.js`, `.android.js` o `.js` para importar solo el archivo correcto.
The `.native.js` files contain code that is shared between both mobile platforms (iOS & Android). Currently, the `.ios.js` and `.android.js` files compose this `.native.js` file since all code is shared right now. However, if a component needed to be different for platform specific reasons, that code would be included in the corresponding platform specific files.

At the end, every component is defined by 6 files. If we look at the screen component, here is its structure.
