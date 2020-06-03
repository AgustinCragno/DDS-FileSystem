import java.util.function.Consumer;

public interface HighLevelFileSystem {
    void openFile();
    void closeFile();
    byte[] syncReadFile(int start, int length);
    void syncWriteFile(int start, byte[] buffer);
    void asyncReadFile(int start, int length, FileSystemCallback callback);
}
