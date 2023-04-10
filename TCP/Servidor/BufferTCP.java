public class BufferTCP {
    private static int numClientes=0;
    public BufferTCP(){

    }
    public static synchronized int dar(){
        return numClientes;
    }
    public static synchronized void aumentar(){
        numClientes++;
    }
}
