package com.cc.llogger.writer;

import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * @author chenhao
 */
public class SimpleLocalFileWriterFactory extends CachedWriterFactoryBase {

    @Override
    protected IndexWriter openWriter(String uri, IndexWriterConfig config) throws IOException {
        Directory dir = FSDirectory.open(Paths.get(uri));
        return new IndexWriter(dir,config);
    }

    @Override
    protected boolean needUpdate(IndexWriter oldWriter) {
        return  !oldWriter.isOpen();
    }
}
