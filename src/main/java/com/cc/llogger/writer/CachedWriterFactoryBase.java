package com.cc.llogger.writer;

import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;

import java.io.IOException;

/**
 * CachedWriterFactoryBase: ${description}
 *
 * @author chenhao
 * @version 1.0
 * @date 2021-3-12 15:42
 */
public abstract class CachedWriterFactoryBase implements WriterFactory{

    private String indexUri;

    private IndexWriterConfig writerConfig;

    private boolean configured = false;

    private volatile IndexWriter cachedWriter;

    private final Object initLock = new Object();


    @Override
    public void configure(String indexUri,IndexWriterConfig writerConfig){
        this.indexUri = indexUri;
        this.writerConfig = writerConfig;
        configured = true;
    }

    @Override
    public IndexWriter getWriter() throws IOException {
        if(!configured){
            throw new IllegalStateException("this writer not configured yet");
        }
        if(cachedWriter == null || needUpdate(cachedWriter)){
            synchronized (initLock){
                if(cachedWriter == null || needUpdate(cachedWriter)){
                    cachedWriter = openWriter(indexUri,writerConfig);
                }
            }
        }
        return cachedWriter;
    }

    protected abstract IndexWriter openWriter(String uri,IndexWriterConfig config) throws IOException;

    protected abstract boolean needUpdate(IndexWriter oldWriter);
}
