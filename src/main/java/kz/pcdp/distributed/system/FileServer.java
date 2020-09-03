package kz.pcdp.distributed.system;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public final class FileServer {

  public static final String GET_METHOD = "GET";
  public static final String SPACE_SEPARATOR = " ";
  ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);

  public void run(final ServerSocket socket, final PCDPFilesystem fs, final int ncores) throws IOException {

    while (true) {

      final Socket request = socket.accept();
      Thread worker = new Thread(
        () -> {
          try {
            String firstLine = FileServer.this.extractRequestFirstLine(request);
            if (FileServer.this.isGetRequest(firstLine)) {
              String filePath = FileServer.this.extractRequestFilePath(firstLine);

              Optional<String> fileContent = FileServer.this.readFileContent(fs, filePath);
              if (fileContent.isPresent()) {
                FileServer.this.printSuccessResponse(request, fileContent.get());
              } else {
                FileServer.this.printNotFoundResponse(request);
              }
            }
          } catch (IOException io) {
            throw new RuntimeException(io);
          }
        }
      );
      executor.execute(worker);
      worker.start();
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
