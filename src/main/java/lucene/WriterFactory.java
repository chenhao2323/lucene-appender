package lucene;

import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;

import java.io.IOException;

public interface WriterFactory {
    IndexWriter openWriter(String uri, IndexWriterConfig config) throws  IOException;
}
