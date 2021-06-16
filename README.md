
# Struggle-IMU
===========================

![Struggle-IMU](https://github.com/delfer66/Struggle-IMU/blob/master/image/header.png)

Struggle-IMU es el diseño e implementación de un juguete electrónico que consta de dos robots luchadores, que se comunican y controlan atravez de aplicación móvil para Android.

![Struggle-IMU_game](https://github.com/delfer66/Struggle-IMU/blob/master/image/Lucha1.jpg)

El juego consiste en dos robots luchadores que deben golpearse para declarar un ganador. Quien de mas golpes al robot contrario ganara el juego, es nesesario dos jugadores para controlar a cada robot, cada jugador se conecta con uno de los robots atravez de bluetooth utilizando su celular con la aplicación, adicionalmente los dos jugadores se conectan por Wi-Fi para iniciar el juego y conocer el ganador.

![App1](https://github.com/delfer66/Struggle-IMU/blob/master/image/Aplicacion2.jpg)

Los robots cuenta con un sistema de control y potencia para el control de sus motores, sensores, comunicación Bluetooth y funcionamiento a bateria.

![robots](https://github.com/delfer66/Struggle-IMU/blob/master/image/Prototipo5.jpg)

La aplicación movil desarrollada en Java con el IDE de Android Studio, permite conectarse por bluetooth con los robots y tambien comunicarse con otro smart-phone que tenga la aplicación para iniciar una nueva pelea, contabilizar los golpes que recibe cada robot y tambien declarar un ganador y perdedor.

![App2](https://github.com/delfer66/Struggle-IMU/blob/master/image/App.jpg)

Feature
--------------

• Interface IU facil de usar para el control de los robots

• Sistema de juego integrado que declara un ganador o perdedor.

• Comunicación Bluetooth gracias a las librerias de [Android-BluetoothSPPLibrary](https://github.com/akexorcist/BluetoothSPPLibrary) de akexorcist.

• Comunicación Wi-Fi gracias a [WiFiDirectServiceDiscovery](https://android.googlesource.com/platform/development/+/master/samples/WiFiDirectServiceDiscovery/)



