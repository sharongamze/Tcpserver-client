package TcpServer;
import Client.Client;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class TcpServer {
    private final int port;
    private volatile boolean stopServer;
    private ThreadPoolExecutor threadPool;
    private IHandler requestHandler;
    private final ReentrantReadWriteLock readWriteLock=new ReentrantReadWriteLock();

    public TcpServer(int port) {
        this.port = port;
        stopServer = false;
    }

    public void run(IHandler concreteHandler) {
        this.requestHandler = concreteHandler;

        new Thread(() -> {
            threadPool = new ThreadPoolExecutor(3, 5, 10,
                    TimeUnit.SECONDS, new LinkedBlockingQueue<>());
            try {
                ServerSocket serverSocket = new ServerSocket(port);
                while (!stopServer) {
                    Socket serverToSpecificClient = serverSocket.accept(); // 2 operations: listen()+accept()

                    Runnable handleLogic = () -> {
                        try {
                            requestHandler.handle(serverToSpecificClient.getInputStream(),
                                    serverToSpecificClient.getOutputStream());

                            serverToSpecificClient.getInputStream().close();
                            serverToSpecificClient.getOutputStream().close();
                            serverToSpecificClient.close();

                        } catch (IOException ioException) {
                            System.err.println(ioException.getMessage());
                        } catch (ClassNotFoundException ce) {
                            System.err.println(ce.getMessage());
                        }
                    };
                        threadPool.execute(handleLogic);

                }
                serverSocket.close();

            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }).start();
    }

    // only one thread allowed to write
    public void stop(){
        try {
            readWriteLock.writeLock().lock();
            if (!stopServer)
                stopServer = true;
            if (threadPool != null) threadPool.shutdown();
        }
        finally {
            readWriteLock.writeLock().unlock();
        }

    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        TcpServer myServer = new TcpServer(8010);
        myServer.run(new MatrixIHandler());
    }
}



