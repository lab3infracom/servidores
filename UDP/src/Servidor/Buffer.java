import java.util.ArrayList;
import java.util.List;

public class Buffer {
    private final List<Mensajero> buffer = new ArrayList<>();
    private final int tamanoMaximo;

    public Buffer(int tamanoMaximo) {
        this.tamanoMaximo = tamanoMaximo;
    }

    public synchronized void put(Mensajero mensajero) throws InterruptedException {
        while (buffer.size() == tamanoMaximo) {
            wait();
        }
        buffer.add(mensajero);
        notifyAll();
    }

    public synchronized Mensajero get() throws InterruptedException {
        while (buffer.isEmpty()) {
            wait();
        }
        Mensajero mensajero = buffer.remove(0);
        notifyAll();
        return mensajero;
    }

    public synchronized int dar() {
        return buffer.size();
    }
}
