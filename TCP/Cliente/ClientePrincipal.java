import java.util.Scanner;

public class ClientePrincipal {
	public static void main(String[] args) {

        Scanner myObj = new Scanner(System.in);  
        System.out.println("Ingrese la cantidad de clientes que va a conectar");
        int numclientes = Integer.parseInt(myObj.nextLine());
        
        
        for(int i = 0; i<numclientes; i++)
        {
        	ClienteTCP elcliente = new ClienteTCP (i, numclientes);
            elcliente.start () ; 
        }
        
    }

}
