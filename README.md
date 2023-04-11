# Instrucciones TCP

### Servidor TCP
0. Se necesita descargar en la maquina virtual el paquete de java con la instruccion:
    - `sudo apt-get install default-jdk`
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
    - `dd if=/dev/zero of=archivo_100Mb.txt bs=1M count=100`
    - `dd if=/dev/zero of=archivo_250Mb.txt bs=1M count=250`
10. Se hace necesario la compilacion de los 4 programas a partir de la instruccion 
    - `javac nombrePrograma.java`
11. Se hace necesario correr el programa de ServidorTCP con la siguiente instrucciones:
    - `java -Xmx3g ServidorTCP`
    11.1. Se escoge el archivo a enviar y el numero de conexiones
12. Se hace necesario correr el programa de ClientePrincipal con la siguiente instrucciones:
    - `java -Xmx3g ClientePrincipal`
    12.1. Se eligen en numero de conexiones simultaneas.

# Instrucciones UDP

### Servidor UDP
0. Se descarga en la Maquina Virtual(MV) el paquete de java con el comando:
   - `sudo apt-get install default-jdk`

1. Clonar el repositorio del proyecto en la maquina virtual con la instruccion:
   - `git clone https://github.com/lab3infracom/servidores.git`

2. Se verifica que la MV se encuentre en BRIDGE.

3. Se ejecuta el comando `ifconfig` para saber la IP del servidor. Se utilizara mas adelante por lo que se recomienda tenerla a la mano.

4. Dirigirse a la carpeta del servidor con el comando:
   - `cd servidores/UDP/Servidor`

5. Se crean los archivos a enviar en la carpeta con los siguientes comandos:
    - `dd if=/dev/zero of=archivo_100Mb.txt bs=1M count=100`
    - `dd if=/dev/zero of=archivo_250Mb.txt bs=1M count=250`

7. Se deben crear los archivos ejecutables de todos los archivos .java con los siguientes comandos:
    - `javac Buffer.java`
    - `javac CustomFormatter.java`
    - `javac UDPServer.java`

6. Se ejecuta el programa ServidorUDP con el comando: 
    - `java UDPServer`

7. Indicar en la terminal el archivo que se quiere enviar: 100 o 250.

### Cliente UDP

0. Tener instalado el ambiente de java.

1. Clonar el repositorio del proyecto en la maquina virtual con la instruccion:
   - `git clone https://github.com/lab3infracom/servidores.git`

2. Abrir la carpeta `servidores/UDP/Cliente` en el editor de su preferencia.

3. Editar en la linea 14 del archivo UDPClient.java la IP del servidor (la cual se obtuvo previamente en el punto 3 del Servidor UDP).

4. Correr el archivo UDPClient.java en el editor de su preferencia y luego ejecutarlo. En caso de que prefiera compilarlo con el comando `javac UDPClient.java`, previo a la ejecución, es necesario eliminar la linea que especifica el paquete en el archivo y luego de compilarlo, si ejecutarlo con el comando `java UDPClient`.

5. Introducir en la terminal el numero de clientes (conexiones simultaneas) que se quiere ejecutar.

### Resultado UDP

- [En la MV] Si detiene el programa del servidor, ejecutando el comando `cd Logs` puede acceder a los logs de la ejecución del servidor. Se aclara que el servidor no se detiene de manera automatica ya que está siempre disponible esperando conexiones por parte de clientes.

- [En el cliente] Si se revisa la carpeta Logs, se encuentra el log de ejecucion del cliente. Por otro lado si se revisa la carpeta ArchivosRecibidos se puede observar el archivo recibido por cada uno de los clientes.
