import java.util.concurrent.Semaphore;

public class Buffer {

    private final Semaphore semaphore;

    public Buffer() {
        semaphore = new Semaphore(25);
    }

    public void adquirir() throws InterruptedException {
        semaphore.acquire();
    }

    public void liberar() {
        semaphore.release();
    }
}

