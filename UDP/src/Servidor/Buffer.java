public class Buffer {
    private int numClientes=0;
    public Buffer(){

    }
    public synchronized int dar(){
        return numClientes;
    }
    public synchronized void aumentar(){
        numClientes++;
    }
}
