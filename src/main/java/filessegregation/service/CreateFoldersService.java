package filessegregation.service;

import java.io.File;
import java.io.IOException;

public class CreateFoldersService {

    public void createFolders(String rootDir, String homeDir, String devDir, String testDir, String counterFile) throws IOException {
        createFolderWithName(rootDir);
        createFolderWithName(homeDir);
        createFolderWithName(devDir);
        createFolderWithName(testDir);
        createCounterFile(counterFile);
    }

    private void createFolderWithName(String name) {
        File dir = new File(name);
         dir.mkdir();
    }

    private void createCounterFile(String dir) throws IOException {
        File counter = new File(dir);
        counter.createNewFile();
    }
}
