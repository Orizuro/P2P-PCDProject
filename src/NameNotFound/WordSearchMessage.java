package NameNotFound;

import Communication.GlobalConfig;
import Files.FileManager;

import java.io.File;
import java.io.Serializable;
import java.util.*;

public class WordSearchMessage implements Serializable {
    private final String word;

    public WordSearchMessage(String word) {
        this.word = word;
    }

    public String getWord(){
        return this.word;
    }


    public List<FileManager> search(){
        Map<Integer, FileManager> occurrenceMap = new TreeMap<Integer, FileManager>();
        File[] files = readAllFiles();
        for (File file : files) {
            if (!file.getName().endsWith(".ser")){
                int occurrences = countOccurrences(file.getName());
                FileManager info = new FileManager(file);
                occurrenceMap.put(occurrences,info);
            }
        }

        return new ArrayList<>(occurrenceMap.values());
    }

    public int countOccurrences(String text) {
        int n = text.length();
        int m = this.word.length();
        int count = 0;

        for (int s = 0; s <= n - m; s++) {
            if (text.substring(s, s + m).equals(this.word)) {
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
