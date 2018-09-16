public class MyCloseableClass implements AutoCloseable {
    @Override
    public void close() throws Exception {
        this.close();
    }
}
