package Server;

public class RunnableSocketServer implements Runnable {
    private final SocketServer sharedObject;

    public RunnableSocketServer(SocketServer sharedObject) {
        this.sharedObject = sharedObject;
    }

    @Override
    public void run() {
        System.out.println("Running in thread: " + Thread.currentThread().getName());
        sharedObject.startServer();
    }
}
