public class Buffer {

    private static int numClientes=0;

    public synchronized void aumentar(){
        
        while (numClientes==25)
        {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        numClientes++;
        notify();
    }

    public synchronized void disminuir(){

        numClientes--;
        notify();
    }
}