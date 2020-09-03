package kz.pcdp.distributed.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Optional;

public final class FileServer {

  public static final String GET_METHOD = "GET";
  public static final String SPACE_SEPARATOR = " ";

  public void run(final ServerSocket socket, final PCDPFilesystem fs) throws IOException {

    while (true) {

      Socket request = socket.accept();
      String firstLine = this.extractRequestFirstLine(request);

      if (this.isGetRequest(firstLine)) {
        String filePath = this.extractRequestFilePath(firstLine);

        Optional<String> fileContent = this.readFileContent(fs, filePath);
        if (fileContent.isPresent()) {
          this.printSuccessResponse(request, fileContent.get());
        } else {
          this.printNotFoundResponse(request);
        }
      }
    }
  }

  private Optional<String> readFileContent(PCDPFilesystem fs, String filePath) {
    String fileConent = fs.readFile(new PCDPPath(filePath));
    return Optional.ofNullable(fileConent);
  }

  private boolean isGetRequest(String firstLine) {
    return firstLine.startsWith(GET_METHOD);
  }

  private String extractRequestFirstLine(Socket request) throws IOException {
    BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
    String firstLine = br.readLine();
    assert firstLine != null;
    return firstLine;
  }

  private String extractRequestFilePath(String firstLine) {
    String[] firstLineParts = firstLine.split(SPACE_SEPARATOR);
    assert firstLineParts.length > 1;
    return firstLineParts[1];
  }

  private void printSuccessResponse(Socket request, String fileContent)
    throws IOException {
    StringBuilder response = new StringBuilder();

    response.append("HTTP/1.0 200 OK\r\n");
    response.append("Server: FileServer\r\n");
    response.append("\r\n");
    response.append(fileContent);
    response.append("\r\n");

    this.writeResponse(request, response.toString());
  }

  private void printNotFoundResponse(Socket request)
    throws IOException {
    StringBuilder response = new StringBuilder();

    response.append("HTTP/1.0 404 Not Found\r\n");
    response.append("Server: FileServer\r\n");
    response.append("\r\n");

    this.writeResponse(request, response.toString());
  }

  private void writeResponse(Socket socket, String response)
    throws IOException {
    OutputStream out = socket.getOutputStream();
    PrintStream ps = new PrintStream(out);

    ps.println(response);
    ps.close();
  }
}
