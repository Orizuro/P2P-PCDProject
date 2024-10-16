package Communication;

import java.io.File;

public class GlobalConfig {

    private GlobalConfig instance = null;
    private String defaultPath = "./documents/";

    public GlobalConfig getInstance() {
        if (instance == null) {
            instance = new GlobalConfig();
        }
        return instance;
    }

    public String getDefaultPath() {
        validateDirectory();
        return defaultPath;
    }

    public void setDefaultPath(String defaultPath) {
        validateDirectory();
        this.defaultPath = defaultPath;
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
}
