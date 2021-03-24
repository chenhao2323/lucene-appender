package com.cc.llogger.writer;

import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;

import java.io.IOException;

public interface WriterFactory {

    void configure(String uri, IndexWriterConfig config);

    IndexWriter getWriter() throws IOException;
}
