package Client;

// Cria uma Thread que executa o SocketCliente

public class RunnableSocketClient implements Runnable {
    private final SocketClient sharedObject;

    public RunnableSocketClient(SocketClient sharedObject) {
        this.sharedObject = sharedObject;
    }

    @Override
    public void run() {
        System.out.println("Running in thread: " + Thread.currentThread().getName());
        sharedObject.startSocket();
    }
}
