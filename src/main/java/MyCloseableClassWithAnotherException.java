import java.io.IOException;

public class MyCloseableClassWithAnotherException implements AutoCloseable {
    @Override
    public void close() throws Exception {
        throw new IOException();
    }
}
