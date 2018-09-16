public class MyCloseableClassWithException implements AutoCloseable {
    @Override
    public void close() throws Exception {
        throw new ClassNotFoundException();
    }
}
