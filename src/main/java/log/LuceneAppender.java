package log;

import ch.qos.logback.classic.ClassicConstants;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import ch.qos.logback.core.encoder.Encoder;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * LuceneAppender: ${description}
 *
 * @author chenhao
 * @version 1.0
 * @date 2021-1-28 20:31
 */
public class LuceneAppender extends UnsynchronizedAppenderBase<ILoggingEvent> {

    private Encoder<ILoggingEvent> encoder;

    private String indexPath;

    private IndexWriter writer;

    private Analyzer analyzer;

    @Override
    protected void append(ILoggingEvent eventObject) {
        Document doc = new Document();
        doc.add(new StringField("requestUri",eventObject.getMDCPropertyMap().get(ClassicConstants.REQUEST_REQUEST_URI), Field.Store.NO));
        doc.add(new LongPoint("timeStamp",eventObject.getTimeStamp()));
        doc.add(new StringField("level",eventObject.getLevel().levelStr, Field.Store.NO));
        doc.add(new StoredField("content",encoder.encode(eventObject)));
        try {
            writer.addDocument(doc);
        } catch (IOException e) {
            addError("索引写入失败",e);
        }
    }

    @Override
    public void start() {
        if (indexPath == null) {
            addError("indexPath为空");
            return;
        }
        Map<String, Analyzer> customAnalyzer = new HashMap<>();
        analyzer = new PerFieldAnalyzerWrapper(new KeywordAnalyzer(), customAnalyzer);

        if (writer != null) {
            try {
                Directory dir = FSDirectory.open(Paths.get(indexPath));
                IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
                writer = new IndexWriter(dir, iwc);
            } catch (IOException e) {
                addError("索引打开失败", e);
            }
        }

        super.start();
    }

    @Override
    public void stop() {
        if(writer != null){
            try {
                writer.close();
            } catch (IOException e) {
                addError("关闭失败",e);
            }
        }
        super.stop();
    }

    public void setEncoder(Encoder<ILoggingEvent> encoder) {
        this.encoder = encoder;
    }

    public void setIndexPath(String indexPath) {
        this.indexPath = indexPath;
    }

    public void setWriter(IndexWriter writer) {
        if (writer == null) {
            this.writer = writer;
        }
    }
}
