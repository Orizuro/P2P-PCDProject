package Search;

import Communication.GlobalConfig;
import Files.FileSearchManager;

import java.io.File;
import java.io.Serializable;
import java.util.*;

public class WordSearchMessage implements Serializable {
    private final String message;

    public WordSearchMessage(String text) {
        this.message = text;
    }

    public String getMessage(){
        return this.message;
    }


    public List<FileSearchManager> search(){
        Map<Integer, FileSearchManager> occurrenceMap = new TreeMap<Integer, FileSearchManager>();
        File[] files = readAllFiles();
        for (File file : files) {
            if (!file.getName().endsWith(".ser")){
                int occurrences = countOccurrences(file.getName());
                FileSearchManager info = new FileSearchManager(file);
                if(occurrences != 0)
                    occurrenceMap.put(occurrences,info);
            }
        }
        return new ArrayList<>(occurrenceMap.values());
    }

    public int countOccurrences(String text) {
        int n = text.length();
        int m = this.message.length();
        int count = 0;

        for (int s = 0; s <= n - m; s++) {
            if (text.substring(s, s + m).equals(this.message)) {
                count++;
            }
        }
        return count;
    }

    public File[] readAllFiles() {
        GlobalConfig gc = new GlobalConfig();
        File[] allFiles = new File(gc.getDefaultPath()).listFiles();
        List<File> fileList = new ArrayList<>();

        if (allFiles != null) {
            for (File file : allFiles) {
                if (file.isFile()) {
                    fileList.add(file);
                }
            }
        }

        File[] files = fileList.toArray(new File[0]);
        return files;
    }
}
