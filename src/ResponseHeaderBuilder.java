public class ResponseHeaderBuilder {

    public static String badFormatRequest() {
        return "HTTP/1.0 400 Bad Request\r\n";
    }
    public static String fileNotFound() {
        return "HTTP/1.0 404 Not Found\r\n";
    }
    public static String verbAllowed() {
        return "HTTP/1.0 405 Method Not Allowed\r\n" +
                "Allow: GET\r\n";
    }
    private static String mediaType(String type){
        if (type.equals("html")) return "Content-Type: text/html; charset=UTF-8\r\n";
        else return "Content-Type: image/" + type + "\r\n";
    }
    public static String buildHeader(String fileExtension, long length){

        System.out.println("HTTP/1.0 200 Document Follows\r\n" +
                mediaType(fileExtension) +
                "Content-Length: " + length + "\r\n\r\n");

        return "HTTP/1.0 200 Document Follows\r\n" +
                mediaType(fileExtension) +
                "Content-Length: " + length + "\r\n\r\n";
    }
}
