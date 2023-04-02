public class Buffer {
    private static int numClientes=0;

    public synchronized void conectar(){  
        if (numClientes == 25) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        numClientes++;
    }

    public synchronized void desconectar(){
        numClientes--;
        notify();
    }
}