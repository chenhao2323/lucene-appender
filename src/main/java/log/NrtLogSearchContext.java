package log;

import lucene.LuceneContextBase;
import lucene.SimpleLocalFileWriterFactory;
import lucene.WriterFactory;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.SearcherManager;

import java.io.IOException;

public class NrtLogSearchContext extends LuceneContextBase {

    private SearcherManager manager;

    private static IndexWriter writer;

    private final WriterFactory writerFactory = new SimpleLocalFileWriterFactory();

    private  static  final Object initLock = new Object();

    public NrtLogSearchContext(String indexUri, IndexWriterConfig writerConfig) {
        super(indexUri, writerConfig);
    }

    @Override
    public void start() {
        if (writer == null) {
            synchronized (initLock) {
                try {
                    if (writer == null) {
                        writer = writerFactory.openWriter(indexUri, writerConfig);
                    }
                    manager = new SearcherManager(writer, null);
                } catch (IOException e) {
                    //todo
                    return;
                }
            }
        }
        super.start();
    }

    @Override
    public void stop(){
        if(writer != null){
            try {
                writer.close();
            } catch (IOException e) {
                //todo
            }
        }
    }

    @Override
    public IndexWriter getWriter() {
        ensureInited();
        return writer;
    }

    @Override
    public IndexSearcher getSearcher() throws IOException {
        ensureInited();
        return manager.acquire();
    }

    private void ensureInited(){
        if(!started){
            throw new IllegalStateException("context has not init");
        }
    }
}
