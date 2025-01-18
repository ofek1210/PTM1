package test;

import test.RequestParser.RequestInfo;


import java.io.*;
import java.net.*;
import java.util.concurrent.*;



public class MyHTTPServer extends Thread implements HTTPServer{


    private int port;
    private int numberOfThreads = 0;
    private ServerSocket serverSocket;
    private ExecutorService threadPool;

    private ConcurrentHashMap<String, Servlet> getHttpCommandMap = new ConcurrentHashMap<String, Servlet>();
    private ConcurrentHashMap<String, Servlet> postHttpCommandMap = new ConcurrentHashMap<String, Servlet>();
    private ConcurrentHashMap<String, Servlet> deleteHttpCommandMap = new ConcurrentHashMap<String, Servlet>();


    public MyHTTPServer(int port,int nThreads){
        this.port = port;
        this.numberOfThreads = nThreads;
    }


    public void addServlet(String httpCommand, String uri, Servlet s){
        switch(httpCommand.toUpperCase()) {
            case "GET":
                this.getHttpCommandMap.put(uri, s);
                break;
            case "POST":
                this.postHttpCommandMap.put(uri, s);
                break;
            case "DELETE":
                this.deleteHttpCommandMap.put(uri, s);
                break;
            default:
                throw new IllegalArgumentException("Unsupported HTTP command: " + httpCommand);
        }
    }

    public void removeServlet(String httpCommand, String uri){
        switch(httpCommand.toUpperCase()) {
            case "GET":
                this.getHttpCommandMap.remove(uri);
                break;
            case "POST":
                this.postHttpCommandMap.remove(uri);
                break;
            case "DELETE":
                this.deleteHttpCommandMap.remove(uri);
                break;
            default:
                throw new IllegalArgumentException("Unsupported HTTP command: " + httpCommand);
        }
    }


    public void run(){
        try {
            serverSocket = new ServerSocket(port);
            threadPool = Executors.newFixedThreadPool(numberOfThreads);


            while (serverSocket.isClosed() == false) {
                try {
                    Thread.sleep(1000);
                    Socket clientSocket = serverSocket.accept();
                    // Handle client
                    threadPool.execute(() -> handleRequest(clientSocket));
                } catch (IOException e) {
                    if (serverSocket.isClosed() == true) {
                        break;
                    }
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void close() {
        // Shut down
        threadPool.shutdown();
        // Wait for all tasks to finish or timeout after 60 seconds
        try {
            // Await termination of running tasks or force shut down if not finish after 60 seconds
            if (threadPool.awaitTermination(60, TimeUnit.SECONDS) == true)
                threadPool.shutdownNow();

            // Close server socket
            if(serverSocket != null)
                serverSocket.close();

            // Close all servlets
            closeServlets();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void closeServlets() throws IOException {


        for(Servlet servlet : getHttpCommandMap.values()) {
            servlet.close();
        }

        for(Servlet servlet : postHttpCommandMap.values()) {
            servlet.close();
        }

        for(Servlet servlet : deleteHttpCommandMap.values()) {
            servlet.close();
        }

    }


    private void handleRequest(Socket clientSocket) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             OutputStream out = clientSocket.getOutputStream()) {
            // Parse request
            RequestInfo requestInfo = RequestParser.parseRequest(reader);
            Servlet servlet = null;


            switch(requestInfo.getHttpCommand().toUpperCase()) {
                case "GET":
                    servlet = matchUriToServlet(getHttpCommandMap, requestInfo.getUri());
                    break;
                case "POST":
                    servlet = matchUriToServlet(postHttpCommandMap, requestInfo.getUri());
                    break;
                case "DELETE":
                    servlet = matchUriToServlet(deleteHttpCommandMap, requestInfo.getUri());
                    break;
            }

            if (servlet != null) {
                servlet.handle(requestInfo, out);
            } else {

            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private Servlet matchUriToServlet(ConcurrentHashMap<String, Servlet> commandMap, String uri) {

        Servlet matchedServlet = null;
        int longestMatchLength = -1;

        for (String key : commandMap.keySet()) {

            if (uri.startsWith(key) == true && key.length() > longestMatchLength) {
                matchedServlet = commandMap.get(key);
                longestMatchLength = key.length();
            }
        }

        return matchedServlet;
    }

}