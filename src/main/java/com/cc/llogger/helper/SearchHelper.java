package com.cc.llogger.helper;

import com.cc.llogger.writer.WriterFactory;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.SearcherManager;

import java.io.IOException;

/**
 * SearchHelper:
 *
 * @author chenhao
 * @version 1.0
 * @date 2021-3-12 15:36
 */
public class SearchHelper {

    private SearchHelper(){

    }
    private static WriterFactory writerFactory;

    private static IndexWriter currentWriter;

    private static SearcherManager manager;

    public static IndexSearcher getCurrenIndexSearcher() throws IOException {
        if(writerFactory == null){
            return null;
        }
        ensureNewWriter();
        return manager.acquire();
    }

    public static void refreshSearcher() throws IOException {
        if (manager != null) {
            try {
                manager.maybeRefresh();
            } catch (RuntimeException e) {

            }
        }
    }

    private static void ensureNewWriter() throws IOException{
        IndexWriter newWriter = writerFactory.getWriter();
        if(currentWriter == null || !currentWriter.equals(newWriter) || manager == null){
            currentWriter = newWriter;
            manager = new SearcherManager(currentWriter,null);
        }
    }

    public static void setLogWriterFactory(WriterFactory writerFactory){
         SearchHelper.writerFactory = writerFactory;
    }

}
