import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Date;

/**Выполнено согласно условию поставленному в задании.
 * @author Ilsur Chubarkin
 */
public class HomeTask6 {

    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(8080);
        while (true) {
            System.out.println("Server started");
            Socket client = ss.accept();
            System.err.println("Client accepted");
            readInputHeaders(client);
        }
    }

//записать в выходной поток
    private static void writeResponse(Socket client) throws IOException {

        String path = new File(".").getAbsolutePath();
        String[] list = new File(path).list();
        assert list != null;
        String response = Arrays.asList(list).toString();


        OutputStream os = client.getOutputStream();
        os.write(response(response).getBytes());
        os.flush();
    }
//сформировать выходной поток
    private static String response(String resp) {
        Date date = new Date();
        String start = "HTTP/1.1 200 OK\r\n";
        String header = "Date: " + date.toString() + "\r\n";
        header += "Content-Type: text/html\r\n";
        header += "Content-length: " + resp.length() + "\r\n";
        header += "\r\n";
        return start + header + resp;
    }
//прочитать входной поток
    private static void readInputHeaders(Socket client) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
        System.out.println(br.ready());
        String s = br.readLine();
        if (s != null && s.contains("GET")) {
            writeResponse(client);
        }
        else {
            client.getOutputStream().write(response("Http Error Code: 404. Resource not found").getBytes());
            client.getOutputStream().flush();
        }
        client.close();
    }

}

