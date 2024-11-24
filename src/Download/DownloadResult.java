package Download;

import Files.FileManager;
import java.io.Serializable;

public class DownloadResult {

    public class downloadResult implements Serializable {
        private FileBlockAnswerMessage wordSearchMessage;
        private FileManager fileManager;
        private String ip;
        private int port;


        public downloadResult(FileBlockAnswerMessage wordSearchMessage, FileManager fileManager, String ip, int port) {
            this.wordSearchMessage = wordSearchMessage;
            this.fileManager = fileManager;
            this.ip = ip;
            this.port = port;
        }

        @Override
        public String toString() {
            return (fileManager.name);
        }
    }
}