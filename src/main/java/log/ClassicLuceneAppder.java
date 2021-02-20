package log;

import ch.qos.logback.classic.ClassicConstants;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.encoder.Encoder;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexOptions;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

public class ClassicLuceneAppder extends  LunceneAppenderBase<ILoggingEvent>{

    private Encoder<ILoggingEvent> encoder;

    private Charset charset;

    private static final FieldType docType = new FieldType();
    private static final FieldType timeStamp = new FieldType();
    private  Analyzer analyzer;

    static {
        docType.setTokenized(true);
        docType.setIndexOptions(IndexOptions.DOCS);
        docType.setOmitNorms(true);

//        timeStamp.setIndexOptions(IndexOptions.DOCS);
//        timeStamp.setTokenized(false);
//        timeStamp.setOmitNorms(true);
    }

    @Override
    protected Document buildDocument(ILoggingEvent eventObject) {

        Document doc = new Document();
        String requestUri = eventObject.getMDCPropertyMap().get(ClassicConstants.REQUEST_REQUEST_URI);
        if(requestUri != null && !"".equals(requestUri)){
            doc.add(new StringField("requestUri",requestUri, Field.Store.NO));
        }
        doc.add(new StringField("timeStamp",String.valueOf(eventObject.getTimeStamp()),Field.Store.NO));
        doc.add(new NumericDocValuesField("timeStamp",eventObject.getTimeStamp()));
        doc.add(new StringField("level",eventObject.getLevel().levelStr, Field.Store.NO));
        doc.add(new StringField("logger",eventObject.getLoggerName(), Field.Store.NO));


        byte[] bytes = encoder.encode(eventObject);
        String content = charset != null ? new String(bytes,charset) : new String(bytes);

        doc.add(new Field("contentIndex",analyzer.tokenStream("content",content),docType));

        doc.add(new StoredField("content",content));

        return doc;
    }

    @Override
    public void start(){
        Analyzer defaultAn;
        try {
            InputStream  in = ClassicLuceneAppder.class.getResourceAsStream("/StopWord.txt");
            Reader stopWords = new InputStreamReader(in);
            defaultAn = new StopAnalyzer(stopWords);
        } catch (Exception e) {
            addError("read stopWord error ,user null analyzer", e);
            defaultAn = new KeywordAnalyzer();
        }
        analyzer = new PerFieldAnalyzerWrapper(defaultAn);
        super.start();
    }

    public void setEncoder(Encoder<ILoggingEvent> encoder) {
        this.encoder = encoder;
    }

    public void setCharset(Charset charset) {
        this.charset = charset;
    }
}
