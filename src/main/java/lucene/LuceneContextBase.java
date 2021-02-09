package lucene;

import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;

import java.io.IOException;

/**
 * LuceneManager:
 *
 * @author chenhao
 * @version 1.0
 * @date 2021-1-30 16:20
 */
public abstract class LuceneContextBase {

    protected String indexUri;

    protected IndexWriterConfig writerConfig;

    protected boolean started = false;

    private static Iterable<ContextStausListener> listeners;

    public LuceneContextBase(String indexUri, IndexWriterConfig writerConfig) {
        this.indexUri = indexUri;
        this.writerConfig = writerConfig;
    }

    public  abstract IndexWriter getWriter() throws IOException;

    public  abstract IndexSearcher getSearcher() throws IOException;

    public void start() {
        started = true;
        if(listeners != null){
            for(ContextStausListener listener:listeners){
                listener.afterContextInit(this);
            }
        }
    }

    public void stop() {
        started = false;
    }

    public boolean isStarted() {
        return started;
    }

    public static void registLister(ContextStausListener listener){

    }


}
