import java.util.concurrent.Semaphore;

public class Buffer {

    private final Semaphore semaphore;

    public Buffer(int maxConcurrentConnections) {
        semaphore = new Semaphore(maxConcurrentConnections);
    }

    public void adquirir() throws InterruptedException {
        semaphore.acquire();
    }

    public void liberar() {
        semaphore.release();
    }
}
