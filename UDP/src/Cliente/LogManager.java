package Cliente;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogManager {

    private String logsDir;

    public LogManager(String logsDir) {
        this.logsDir = logsDir;
    }

    public void writeLog(String fileName, String fileSize, String transferTime, boolean success) {
        try (FileWriter writer = new FileWriter(logsDir + "/" + generateLogFileName())) {
            writer.write("File name: " + fileName + "\n");
            writer.write("File size: " + fileSize + "\n");
            writer.write("Transfer time: " + transferTime + "\n");
            writer.write("Success: " + (success ? "Yes" : "No") + "\n");
        } catch (IOException e) {
            System.err.println("Error writing log file: " + e.getMessage());
        }
    }

    private String generateLogFileName() {
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        return dateFormat.format(now) + "-log.txt";
    }

}
