package Search;

import Communication.GlobalConfig;
import Files.FileInfo;

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


    public List<FileInfo> search(){
        GlobalConfig gc = GlobalConfig.getInstance();
        Map<FileInfo,Integer> occurrenceMap = new TreeMap<FileInfo, Integer>();
        File[] files = gc.getFilesInDirectory();
        for (File file : files) {
                int occurrences = countOccurrences(file.getName());
                FileInfo info = new FileInfo(file);
                if(occurrences != 0)
                    occurrenceMap.put(info, occurrences);
        }
        return new ArrayList<>(occurrenceMap.keySet());
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

}
