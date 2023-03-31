public class Buffer {

    private static int numClientes=0;

    public synchronized void aumentar(){
        
        while (numClientes==25)
        {
            wait();
        }

        numClientes++;
    }

    public synchronized void disminuir(){

        numClientes--;

        notify();
    }


}