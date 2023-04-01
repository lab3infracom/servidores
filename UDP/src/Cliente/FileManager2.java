package Cliente;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.util.HashMap;
import java.util.Map;

public class FileManager2 {

    private Map<String, Integer> fileSizes;

    public FileManager2() {
        fileSizes = new HashMap<>();
        fileSizes.put("file1", 100 * 1024 * 1024);
        fileSizes.put("file2", 250 * 1024 * 1024);
    }

    public boolean writeFile(DatagramPacket packet, String fileName) {
        boolean result = false;
        try (FileOutputStream fileOut = new FileOutputStream(fileName, true)) {
            fileOut.write(packet.getData(), packet.getOffset(), packet.getLength());
            result = true;
        } catch (IOException e) {
            System.err.println("Error writing file: " + e.getMessage());
        }
        return result;
    }

    public int getFileSize(String fileName) {
        return fileSizes.get(fileName);
    }

}
