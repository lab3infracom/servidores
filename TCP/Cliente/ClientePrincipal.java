import java.util.Scanner;
import java.util.logging.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

public class ClientePrincipal {
    private static final Logger LOGGER=Logger.getLogger("GENERAR LOG");
	public static void main(String[] args) throws SecurityException, IOException {
        int anioActual = LocalDate.now().getYear();
        int mesActual = LocalDate.now().getMonthValue();
        int diaActual = LocalDate.now().getDayOfMonth();
        int horaActual = LocalTime.now().getHour();
        int minutoActual = LocalTime.now().getMinute();
        int segundoActual = LocalTime.now().getSecond();
        FileHandler fh = new FileHandler("Logs/"+anioActual+"-"+mesActual+"-"+diaActual+"-"+horaActual+"-"+minutoActual+"-"+segundoActual+"-log.log");
        LOGGER.addHandler(fh);
            

        Scanner myObj = new Scanner(System.in);  
        System.out.println("Ingrese la cantidad de clientes que va a conectar");
        int numclientes = Integer.parseInt(myObj.nextLine());
        
        
        for(int i = 0; i<numclientes; i++)
        {
        	ClienteTCP elcliente = new ClienteTCP (i, numclientes, LOGGER);
            elcliente.start () ; 
        }
        
    }

}
