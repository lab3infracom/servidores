package Servidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class TestSelector {
    private static final String TESTS_DIR = "Tests/";

    public static void main(String[] args) {
        // Display the available tests
        System.out.println("Available tests:");
        FileManager fileManager = new FileManager(TESTS_DIR);
        String[] testFiles = fileManager.listFiles();
        for (int i = 0; i < testFiles.length; i++) {
            System.out.println((i + 1) + ". " + testFiles[i]);
        }

        // Prompt the user to select a test
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int selection = -1;
        while (selection < 1 || selection > testFiles.length) {
            System.out.print("Select a test (1-" + testFiles.length + "): ");
            try {
                selection = Integer.parseInt(reader.readLine());
            } catch (IOException | NumberFormatException e) {
                // Ignore and prompt again
            }
        }

        // Run the selected test
        String testFile = TESTS_DIR + testFiles[selection - 1];
        FileTransferServer server = new FileTransferServer(4444);
        server.receiveFile(testFile);

        // Wait for the server to finish
        try {
            server.join();
        } catch (InterruptedException e) {
            // Ignore
        }
    }
}
