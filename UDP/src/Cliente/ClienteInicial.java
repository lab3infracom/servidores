package Cliente;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class ClienteInicial {

    /************************************************* CONSTANTES ***********************************************/

    // Directorio donde se encuentran los archivos del servidor
    private static final String DIRECTORIO_ARCHIVOS = "./src/Cliente/";

    // Logger
    private static final Logger LOGGER=Logger.getLogger("GENERAR LOG");

    /************************************************* MAIN ***********************************************/
    public static void main(String[] args) throws SecurityException, IOException {
        // Generar log
        int anioActual = LocalDate.now().getYear();
        int mesActual = LocalDate.now().getMonthValue();
        int diaActual = LocalDate.now().getDayOfMonth();
        int horaActual = LocalTime.now().getHour();
        int minutoActual = LocalTime.now().getMinute();
        int segundoActual = LocalTime.now().getSecond();
        FileHandler fh = new FileHandler(DIRECTORIO_ARCHIVOS + "Logs/"+anioActual+"-"+mesActual+"-"+diaActual+"-"+horaActual+"-"+minutoActual+"-"+segundoActual+"-log.txt");
        LOGGER.addHandler(fh);

        // Obtener numero de clientes concurrentes
        System.out.println("--------------------------------------------------");
        System.out.println("NUMERO DE CONEXIONES CONCURRENTES");
        Scanner myObj = new Scanner(System.in);
        int numConexiones = Integer.parseInt(myObj.nextLine());
        myObj.close();

        // Crear clientes y ejecutarlos
        for(int i = 1; i<=numConexiones; i++) {
        	ClienteUDP clienteUdp = new ClienteUDP(i, numConexiones, LOGGER);
            clienteUdp.start ();
        }
    }
}