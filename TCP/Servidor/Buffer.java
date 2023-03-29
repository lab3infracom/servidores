public class Buffer {
    private static int numClientes=0;
    public Buffer(){

    }
    public static synchronized int dar(){
        return numClientes;
    }
    public static synchronized void aumentar(){
        numClientes++;
    }
}
