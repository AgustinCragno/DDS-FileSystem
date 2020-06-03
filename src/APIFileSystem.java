import java.util.function.Consumer;

public class APIFileSystem implements HighLevelFileSystem {
    private LowLevelFileSystem lowLevelFileSystem;
    private int fileDescriptor;
    private String path;

    public APIFileSystem(String path, LowLevelFileSystem lowLevelFileSystem) {
        this.path = path;
        this.lowLevelFileSystem = lowLevelFileSystem;
    }

    @Override
    public void openFile() {
        fileDescriptor = lowLevelFileSystem.openFile(path);
    }

    @Override
    public void closeFile() {
        lowLevelFileSystem.closeFile(fileDescriptor);
    }

    @Override
    public byte[] syncReadFile(int start, int length) {
        byte[] buffer = new byte[length];
        int resp = lowLevelFileSystem.syncReadFile(fileDescriptor, buffer, start, start + length);
        if(resp < 0)
            throw new ReadFileException();

        return buffer;
    }

    @Override
    public void syncWriteFile(int start, byte[] buffer) {
        lowLevelFileSystem.syncWriteFile(fileDescriptor, buffer, start, buffer.length);
    }

    @Override
    public void asyncReadFile(int start, int length, FileSystemCallback callback) {
        byte[] buffer = new byte[length];
        lowLevelFileSystem.asyncReadFile(fileDescriptor, buffer, start, start + length, resp -> {
            if(resp < 0)
                throw new ReadFileException();

            callback.aplicar(resp, buffer);
        });
    }
}
