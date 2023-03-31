# Instrucciones TCP

### Servidor TCP
0. Se necesita descargar en la maquina virtual el paquete de java con la instruccion:
    sudo apt-get install default-jdk
1. Asegurese de que la maquina virtual tenga como minimo 2gb de ram, de no ser así configurele con los botones de VM Ware
2. Para poder correr el servidor TCP se hace necesaria la transliteracion de todo el codigo de los archivos ManejadorCliente.java y ServidorTCP.java a archivos homonimos en la maquina virtual ubuntu server 20.04.
3. Tambien hace falta la creacion de una carpeta Logs dentro de la misma carpeta donde se crearon los archivos ManejadorCliente.java y ServidorTCP.java.
4. Se necesita configurar la maquina virtual en NAT.
5. Se hace "ifconfig" para saber la IP del servidor
### Cliente TCP
6. Se copian los archivos dentro de la maquina cliente.
7. Se hace necesaria la modificacion del IP del servidor en el renglon 13 de la clase ClienteTCP.java
8. Se hace necesaria la creacion de las carpetas ArchivosRecibidos & Logs en el equipo cliente en la carpeta donde se copiaron los programas
### ¿Como correrlo?
9. Se deben crear los archivos a enviar en la carpeta donde se copiaron los programas en el servidor con las siguientes instrucciones:
    dd if=/dev/zero of=archivo_100Mb.txt bs=1M count=100
    dd if=/dev/zero of=archivo_250Mb.txt bs=1M count=250
10. Se hace necesario la compilacion de los 4 programas a partir de la instruccion 
    javac nombrePrograma.java
11. Se hace necesario correr el programa de ServidorTCP con la siguiente instrucciones:
    java -Xmx3g ServidorTCP
    11.1. Se escoge el archivo a enviar y el numero de conexiones
12. Se hace necesario correr el programa de ClientePrincipal con la siguiente instrucciones:
    java ClientePrincipal
    12.1. Se eligen en numero de conexiones simultaneas.

# Instrucciones UDP

### Servidor UDP
0. Se necesita descargar en la maquina virtual el paquete de java con la instruccion:
   - `sudo apt-get install default-jdk`
1. Asegurese de que la maquina virtual tenga como minimo 2gb de ram, de no ser así configurele con los botones de VM Ware.
2. Para poder correr el servidor TCP se hace necesaria la transliteracion de todo el codigo de los archivos ManejadorCliente.java y ServidorTCP.java a archivos homonimos en la maquina virtual ubuntu server 20.04 O 18.04.
3. Tambien hace falta la creacion de una carpeta Logs dentro de la misma carpeta donde se crearon los archivos ManejadorCliente.java y ServidorTCP.java.
4. Se necesita configurar la maquina virtual en BRIDGE.
5. Se hace "ifconfig" para saber la IP del servidor

### Cliente UDP
6. Se copian los archivos dentro de la maquina cliente.
7. Se hace necesaria la modificacion del IP del servidor en el renglon 13 de la clase ClienteTCP.java
8. Se hace necesaria la creacion de las carpetas ArchivosRecibidos & Logs en el equipo cliente en la carpeta donde se copiaron los programas.

### ¿Como correrlo?
9. Se deben crear los archivos a enviar en la carpeta donde se copiaron los programas en el servidor con las siguientes instrucciones:
    - `dd if=/dev/zero of=archivo_100Mb.txt bs=1M count=100`
    - `dd if=/dev/zero of=archivo_250Mb.txt bs=1M count=250`
10. Se hace necesario la compilacion de los 4 programas a partir de la instruccion 
    - `javac nombrePrograma.java`
11. Se hace necesario correr el programa de ServidorUDP con la siguiente instruccion:
    `java ServidorUDP`
    - 11.1. Se escoge el archivo a enviar y el numero de conexiones
12. Se hace necesario correr el programa de ClientePrincipal con la siguiente instruccion (es importante encontrarse en el directorio UDP al ejecutar este comando):
    `java ClienteInicial`
    - 12.1. Se eligen en numero de conexiones simultaneas.
