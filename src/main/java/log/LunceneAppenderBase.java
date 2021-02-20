package log;

import ch.qos.logback.core.UnsynchronizedAppenderBase;
import lucene.LuceneContextBase;
import lucene.search.SearchFacade;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriterConfig;

import java.util.concurrent.TimeUnit;

public abstract class LunceneAppenderBase<E> extends UnsynchronizedAppenderBase<E> {

    private String uri;

    private LuceneContextBase luceneContext;

    private final IndexWriterConfig writerConfig = new IndexWriterConfig();



    @Override
    protected void append(E eventObject) {
        if (!isStarted()) {
            return;
        }
        try {
            luceneContext.getWriter().addDocument(buildDocument(eventObject));
        } catch (Exception e) {
            addError("error to write index", e);
        }
    }

    protected abstract Document buildDocument(E eventObject);

    @Override
    public void start() {
        if (uri == null) {
            addError("no index uri set");
            return;
        }
        try {
            luceneContext = new NrtLogSearchContext(uri, writerConfig);
            luceneContext.start();
            SearchFacade.setContext(luceneContext);
            getContext().getScheduledExecutorService().scheduleAtFixedRate(() ->{
                try {
                    luceneContext.getWriter().commit();
                } catch (Exception e) {
                    addWarn("commit index error",e);
                }
            },10,10, TimeUnit.SECONDS);
        } catch (Exception e) {
            addError("error to open indexWriter from uri:" + uri, e);
            return;
        }

        super.start();
    }

    @Override
    public void stop() {
        if (luceneContext != null) {
            luceneContext.stop();
        }
        super.stop();
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
