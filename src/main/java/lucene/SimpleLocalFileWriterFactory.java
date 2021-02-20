package lucene;

import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;

public class SimpleLocalFileWriterFactory implements  WriterFactory{

    @Override
    public IndexWriter openWriter(String uri, IndexWriterConfig config) throws IOException {
        Directory dir = FSDirectory.open(Paths.get(uri));
        return new IndexWriter(dir,config);
    }
}
