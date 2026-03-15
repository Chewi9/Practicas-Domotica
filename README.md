# 🏠 Electrónica para Domótica - Universidad de Málaga
Este repositorio contiene las prácticas y desarrollos realizados para la asignatura Electrónica para Domótica. El objetivo principal es el diseño y programación de un sistema de control domótico modular utilizando la plataforma Arduino UNO y la placa de expansión DomoBoard.

# 🚀 Descripción del Proyecto
El sistema implementa una arquitectura basada en eventos y punteros de función, lo que permite separar la lógica del hardware de la lógica de aplicación. A través de una librería personalizada (DomoBoard), se gestionan sensores y actuadores de forma eficiente, permitiendo reconfigurar el comportamiento del sistema en tiempo real mediante comandos por puerto serie.

# 🛠️ Hardware Utilizado
- Microcontrolador: Arduino UNO (Atmega328P).
- Placa DomoBoard: Incluye pulsadores, entradas optocopladas y relés.
- Driver de Potencia: ULN2003A (Darlington array) para el accionamiento de relés de 100mA.
- Actuador Principal: Relé FanHar W11 para control de cargas de potencia (iluminación/persianas).

# 📂 Estructura de las Prácticas
### Práctica 1: Sensores y Lógica Digital
* Implementación de lectura de sensores digitales con gestión de rebotes.
* Configuración de comportamientos: Pulsador simple e Interruptor (Toggle).

### Práctica 2: Conexión Serie Java-Arduino
* Implementación de un "Serial Monitor" usando Java para poder cambiar entre diferentes apartados de la práctica anterior.

### Práctica 3: Actuadores y Control de Potencia
* Introducción al uso de Relés como interruptores controlados electrónicamente.
* Implementación del modo Conmutador: emulación de una luz de pasillo donde múltiples puntos de control actúan sobre un mismo actuador.
* Aislamiento galvánico entre la etapa de control y la de potencia.

# ⚙️ Arquitectura del Software
Lo más destacado de este repositorio es la fusión dinámica de las prácticas. El programa principal permite alternar entre diferentes modos de funcionamiento sin necesidad de reprogramar el dispositivo:
| Comando | Modo | Lógica del Relé (Pin 4) |
| :--- | :--- | :--- |
| `#1` | **Pulsado/Soltado** | El relé se activa solo mientras se pulsa el Botón 1. |
| `#2` | **Interruptor** | El Botón 1 cambia el estado (On/Off) del relé en cada pulsación. |
| `#3` | **Conmutador** | **Cualquier botón** (1, 2 o el Opto) cambia el estado del relé (Modo P3). |

# 💻 Instalación y Uso
1. Clona el repositorio.
2. Añade los archivos .cpp y .h a tu entorno de desarrollo (Arduino IDE, Sloeber o VS Code + PlatformIO).
3. Carga el sketch a usar en tu Arduino UNO.
4. Abre el Monitor Serie a 115200 baudios (o modifícalo para ajustarse a tu dispositivo).
5. Interactúa con el sistema usando los comandos de cabecera # para cambiar el apartado.
