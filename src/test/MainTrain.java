package test;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class MainTrain {
    

    private static void testParseRequest() {
        // Test data
        String request = "GET /api/resource?id=123&name=test HTTP/1.1\n" +
                            "Host: example.com\n" +
                            "Content-Length: 5\n"+
                            "\n" +
                            "filename=\"hello_world.txt\"\n"+
                            "\n" +
                            "hello world!\n"+
                            "\n" ;

        BufferedReader input=new BufferedReader(new InputStreamReader(new ByteArrayInputStream(request.getBytes())));
        try {
            RequestParser.RequestInfo requestInfo = RequestParser.parseRequest(input);

            // Test HTTP command
            if (!requestInfo.getHttpCommand().equals("GET")) {
                System.out.println("HTTP command test failed (-5)");
            }

            // Test URI
            if (!requestInfo.getUri().equals("/api/resource?id=123&name=test")) {
                System.out.println("URI test failed (-5)");
            }

            // Test URI segments
            String[] expectedUriSegments = {"api", "resource"};
            if (!Arrays.equals(requestInfo.getUriSegments(), expectedUriSegments)) {
                System.out.println("URI segments test failed (-5)");
                for(String s : requestInfo.getUriSegments()){
                    System.out.println(s);
                }
            } 
            // Test parameters
            Map<String, String> expectedParams = new HashMap<>();
            expectedParams.put("id", "123");
            expectedParams.put("name", "test");
            expectedParams.put("filename","\"hello_world.txt\"");
            if (!requestInfo.getParameters().equals(expectedParams)) {
                System.out.println("Parameters test failed (-5)");
            }

            // Test content
            byte[] expectedContent = "hello world!\n".getBytes();
            if (!Arrays.equals(requestInfo.getContent(), expectedContent)) {
                System.out.println("Content test failed (-5)");
            } 
            input.close();
        } catch (IOException e) {
            System.out.println("Exception occurred during parsing: " + e.getMessage() + " (-5)");
        }        
    }


    public static void testServer() throws Exception {
        // יצירת מופע של השרת
        MyHTTPServer server = new MyHTTPServer(8080, 5);

        // הוספת Servlet לטיפול בבקשת GET
        server.addServlet("GET", "/test", (requestInfo, outputStream) -> {
            String response = "HTTP/1.1 200 OK\r\n" +
                    "Content-Type: text/plain\r\n" +
                    "Content-Length: 11\r\n" +
                    "\r\n" +
                    "Hello world";
            outputStream.write(response.getBytes());
        });

        // הפעלת השרת
        server.start();

        // סימולציה של לקוח
        try (Socket clientSocket = new Socket("localhost", 8080);
             PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

            // שליחת בקשת GET
            writer.println("GET /test HTTP/1.1");
            writer.println("Host: localhost");
            writer.println();

            // קריאת התגובה מהשרת
            StringBuilder response = new StringBuilder();
            String line;

            // קריאת הכותרות
            while ((line = reader.readLine()) != null && !line.isEmpty()) {
                response.append(line).append("\n");
            }

            // קריאת הגוף (אם יש תוכן)
            char[] buffer = new char[1024];
            int charsRead;
            while ((charsRead = reader.read(buffer)) != -1) {
                response.append(buffer, 0, charsRead);
            }

            // בדיקת התגובה
            if (!response.toString().contains("200 OK")) {
                throw new AssertionError("Test failed: Response did not contain '200 OK'");
            }
            if (!response.toString().contains("Hello world")) {
                throw new AssertionError("Test failed: Response body did not contain expected content");
            }

        } finally {
            // סגירת השרת
            server.close();
        }
    }



    public static void main(String[] args) {
        testParseRequest(); // 40 points
        try{
            testServer(); // 60
        }catch(Exception e){
            System.out.println("your server throwed an exception (-60)");
        }
        System.out.println("done");
    }

}
