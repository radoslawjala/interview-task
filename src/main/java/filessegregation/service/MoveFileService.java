package filessegregation.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.ZoneId;
import java.util.Date;

public class MoveFileService {

    private int allFiles = 0;
    private int devFiles = 0;
    private int testFiles = 0;

    public void runMoveService(String homeDir, String devDir, String testDir, String counterFile) {
        try {
            WatchService watchService = FileSystems.getDefault().newWatchService();
            Path path = Paths.get(homeDir);
            path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);

            WatchKey key;
            while ((key = watchService.take()) != null) {
                for (WatchEvent<?> event : key.pollEvents()) {
                    serveEvent(homeDir, testDir, devDir, event, counterFile);
                }
                key.reset();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void serveEvent(String homeDir, String testDir, String devDir, WatchEvent<?> event, String counterFile) throws IOException {
        String fileName = homeDir + "/" + event.context();
        if(fileHasRequiredExtention(fileName)) {
            File file = new File(fileName);
            BasicFileAttributes attributes =
                    Files.readAttributes(file.toPath(), BasicFileAttributes.class);

            String newDirectory;
            if(isJar(fileName)) {
                Date creationDate = new Date(attributes.creationTime().toMillis());
                int hour = creationDate.toInstant().atZone(ZoneId.systemDefault()).getHour();
                if(isEvenHour(hour)) {
                    newDirectory = devDir + "/" + event.context();
                    devFiles++;
                } else {
                    newDirectory = testDir + "/" + event.context();
                    testFiles++;
                }
                move(fileName, newDirectory);
                allFiles++;
            }
            if(isXml(fileName)) {
                newDirectory = devDir + "/" + event.context();
                move(fileName, newDirectory);
                devFiles++;
                allFiles++;
            }
            updateCounterInFile(counterFile);
        }
    }

    private void updateCounterInFile(String counterFile) throws IOException {
        FileWriter writer = new FileWriter(counterFile);
        writer.write("All: " + allFiles + "\n");
        writer.write("DEV: " + devFiles + "\n");
        writer.write("TEST: " + testFiles + "\n");
        writer.close();
    }

    private void move(String fileName, String newDirectory) throws IOException {
        Files.move(Paths.get(fileName), Paths.get(newDirectory),
                StandardCopyOption.REPLACE_EXISTING);
    }

    private boolean isEvenHour(int hour) {
        return hour % 2 == 0;
    }

    private boolean isJar(String fileName) {
        return fileName.endsWith("jar");
    }

    private boolean isXml(String fileName) {
        return fileName.endsWith("xml");
    }

    private boolean fileHasRequiredExtention(String fileName) {
        return isJar(fileName) || isXml(fileName);
    }
}
