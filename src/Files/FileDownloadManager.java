package Files;

import java.util.Map;

// Gerenciamento de tarefas de download
public class FileDownloadManager {
    private final Map<String, List<byte[]>> fileChunks = new ConcurrentHashMap<>(); // Blocos por arquivo
    private final ExecutorService threadPool = Executors.newFixedThreadPool(5);     // Máx. 5 threads

    public synchronized void addChunk(String fileName, int chunkIndex, byte[] data) {
        fileChunks.computeIfAbsent(fileName, k -> new ArrayList<>());
        fileChunks.get(fileName).add(chunkIndex, data);
    }

    public synchronized boolean isDownloadComplete(String fileName, int totalChunks) {
        return fileChunks.containsKey(fileName) && fileChunks.get(fileName).size() == totalChunks;
    }

    public synchronized void writeToFile(String fileName, String outputDir) throws IOException {
        if (!fileChunks.containsKey(fileName)) return;

        List<byte[]> chunks = fileChunks.get(fileName);
        try (FileOutputStream fos = new FileOutputStream(outputDir + "/" + fileName)) {
            for (byte[] chunk : chunks) {
                fos.write(chunk);
            }
        }
    }

    public void addDownloadTask(String fileName, int totalChunks, Runnable task) {
        threadPool.submit(() -> {
            task.run(); // Executar o download
            if (isDownloadComplete(fileName, totalChunks)) {
                try {
                    writeToFile(fileName, "./downloads");
                } catch (IOException e) {
                    System.err.println("Erro ao salvar o arquivo: " + e.getMessage());
                }
            }
        });
    }

    public void shutdown() {
        threadPool.shutdown();
    }
}
