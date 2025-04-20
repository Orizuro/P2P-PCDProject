package Communication;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GlobalConfig {

    private static GlobalConfig instance = null;  // (singleton)
    private String defaultPath = "documents/"; // Caminho padrão para diretórios
    private File[] filesInDirectory;

    public static GlobalConfig getInstance() {
        if (instance == null) {    // Verifica se a instância ainda não foi criada
            instance = new GlobalConfig();
        }
        return instance;
    }

    private GlobalConfig(){
        readAllFiles();
    }

    // Metodo para obter o caminho padrão
    public String getDefaultPath() {
        validateDirectory();
        return defaultPath;
    }

    public void setDefaultPath(String defaultPath) {
        this.defaultPath = defaultPath;
        validateDirectory();
        readAllFiles();

    }

    private void validateDirectory(){
        File directory = new File(this.defaultPath);
        if (directory.isFile()) {
            System.out.println("The path is a file");
        }
        if (!directory.exists()) {
            if(!directory.mkdir())
                System.out.println("The directory could not be created");
            System.out.println("The directory was created");
        }
    }

    private void readAllFiles(){
        File[] allFiles = new File(getDefaultPath()).listFiles();
        List<File> fileList = new ArrayList<>();
        if (allFiles != null) {
            for (File file : allFiles) {
                if (file.isFile() && !file.getName().endsWith(".ser")) {
                    fileList.add(file);
                }
            }
        }
        this.filesInDirectory = fileList.toArray(new File[0]);
    }

    public File[] getFilesInDirectory() {
        readAllFiles();
        return filesInDirectory;
    }
}
