import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class WebServer {
    private int portNumber;
    private ServerSocket serverSocket;
    private Socket socket;
    private DataOutputStream dataOutputStream;
    private BufferedReader bufferedReader;
    private PrintWriter printWriter;

    public WebServer(int portNumber){
        this.portNumber = portNumber;
        openSocket();
        listen();
    }

    private void openSocket(){
        try {
            serverSocket = new ServerSocket(this.portNumber);
        }
        catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    private void listen(){

        while(true){
            try {
                socket = serverSocket.accept();
                setupStreams();
                decipherRequest(readRequest());
            }
            catch (IOException e){
                throw new RuntimeException(e);
            }
        }
    }

    private void setupStreams(){
        try {
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            printWriter = new PrintWriter(socket.getOutputStream());
        }
        catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    private String readRequest() throws IOException {

        StringBuilder stringBuilder = new StringBuilder();
        String line = "";

        while((line = bufferedReader.readLine()) != null && !line.isEmpty()){
            stringBuilder.append(line + "\n");
        }
        return stringBuilder.toString();
    }

    private void decipherRequest(String request) throws IOException {

        String[] splitRequest = request.split("\n");
        String[] demands = splitRequest[0].split(" ");

        String verb = demands[0];
        String path = demands[1];

        if (checkVerb(verb)) {
            try {
                treatPath(path);

            } catch (IOException e) {
                printWriter.write(ResponseHeaderBuilder.verbAllowed());
                printWriter.flush();
                socket.close();
            }
        }
    }
    private boolean checkVerb(String verb){
        return verb.equals("GET");
    }

    private void treatPath(String path) throws IOException {

        if (path.indexOf(".") != -1){
            sendFile(path, path.substring(path.indexOf(".") +1));
        }
        else{
            printWriter.write(ResponseHeaderBuilder.badFormatRequest());
            printWriter.flush();
            socket.close();
        }
    }

    private void sendFile(String path, String fileExtension) throws IOException {

        String fileName = "resources" + path;

        try {

            File file = new File(fileName);
            FileInputStream fileInputStream = new FileInputStream(file);

            printWriter.write(ResponseHeaderBuilder.buildHeader(fileExtension, file.length()));
            printWriter.flush();


                System.out.println("entrei");
                byte[] buffer = new byte[1024];
                int size;

                while ((size = fileInputStream.read(buffer)) != -1) {
                    dataOutputStream.write(buffer, 0, size);
                }
                socket.close();

        } catch (FileNotFoundException e) {
            printWriter.write(ResponseHeaderBuilder.fileNotFound());
            printWriter.flush();
            socket.close();
        }
    }

    public static void main(String[] args) {

        WebServer webServer = new WebServer(8085);

    }
}
