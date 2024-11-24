package Download;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.Map;

public class FileDownloadResult implements Serializable {
    private String fileName;
    private final AtomicLong totalTime;     // Tempo total do descarregamento, usando AtomicLong para garantir operações seguras em múltiplas threads
    private final Map<String, Integer> blockStats;      // Mapa que mapeia o endereço de cada nó (chave) para o número de blocos que ele descarregou (valor).
                                                        // A classe usa ConcurrentHashMap para garantir que várias threads possam acessar e modificar esse mapa sem causar condições de corrida.
    private final AtomicBoolean success;     // Indica se o descarregamento foi bem-sucedido, armazenando o valor em um AtomicBoolean para permitir modificações e leituras seguras em um
                                             // ambiente com múltiplas threads.

    public FileDownloadResult(String fileName) {
        this.fileName = fileName;
        this.totalTime = new AtomicLong(0);
        this.blockStats = new ConcurrentHashMap<>();
        this.success = new AtomicBoolean(true);
    }

    public String getFileName() {
        return fileName;
    }

    public synchronized void setFileName(String fileName) {     //  ESte métódo é sincronizado para garantir que a modificação seja segura em ambiente multithread
        this.fileName = fileName;
    }

    public long getTotalTime() {
        return totalTime.get();
    }

    public void setTotalTime(long totalTime) {
        this.totalTime.set(totalTime);
    }

    public Map<String, Integer> getBlockStats() {
        return blockStats;
    }

    public void addBlockStat(String nodeAddress, int blocksDownloaded) {
        blockStats.merge(nodeAddress, blocksDownloaded, Integer::sum);
    }

    public boolean isSuccess() {
        return success.get();
    }

    public void setSuccess(boolean success) {
        this.success.set(success);
    }

    @Override
    public synchronized String toString() {
        return "FileDownloadResult{" +
                "fileName='" + fileName + '\'' +
                ", totalTime=" + totalTime +
                ", blockStats=" + blockStats +
                ", success=" + success +
                '}';
    }
}
