package filessegregation;

import filessegregation.service.CreateFoldersService;
import filessegregation.service.MoveFileService;

import java.io.IOException;

public class Main {

    private static final String ROOT_DIR = "ROOT";
    private static final String HOME_DIR = ROOT_DIR + "/HOME";
    private static final String DEV_DIR = ROOT_DIR + "/DEV";
    private static final String TEST_DIR = ROOT_DIR + "/TEST";
    private static final String counterFile = HOME_DIR + "/count.txt";

    public static void main(String[] args) throws IOException {
        CreateFoldersService createFoldersService = new CreateFoldersService();
        createFoldersService.createFolders(ROOT_DIR, HOME_DIR, DEV_DIR, TEST_DIR, counterFile);

        MoveFileService moveFileService = new MoveFileService();
        moveFileService.runMoveService(HOME_DIR, DEV_DIR, TEST_DIR, counterFile);
    }
}
