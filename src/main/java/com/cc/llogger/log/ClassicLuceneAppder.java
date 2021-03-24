package com.cc.llogger.log;

import ch.qos.logback.classic.ClassicConstants;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.encoder.Encoder;
import com.cc.llogger.helper.analyzer.StopCharAnalyzer;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.document.*;
import org.apache.lucene.index.DocValuesType;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexWriterConfig;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

public class ClassicLuceneAppder extends LunceneAppenderBase {

    private Encoder<ILoggingEvent> encoder;

    private Charset charset;

    private int maxContentSize = -1;

    private static final FieldType DOC_TYPE = new FieldType();
    private static final FieldType TIME_STAMP = new FieldType();

    static {
        DOC_TYPE.setTokenized(true);
        DOC_TYPE.setIndexOptions(IndexOptions.DOCS);
        DOC_TYPE.setOmitNorms(true);

        TIME_STAMP.setTokenized(false);
        TIME_STAMP.setDocValuesType(DocValuesType.NUMERIC);
        TIME_STAMP.setIndexOptions(IndexOptions.DOCS);
        TIME_STAMP.setOmitNorms(true);
    }

    @Override
    protected Document buildDocument(ILoggingEvent eventObject) {

        byte[] bytes = encoder.encode(eventObject);
        String content = charset != null ? new String(bytes,charset) : new String(bytes);

        if(maxContentSize > -1 && content.length() > maxContentSize){
            return null;
        }

        Document doc = new Document();
        String requestUri = eventObject.getMDCPropertyMap().get(ClassicConstants.REQUEST_REQUEST_URI);
        if(requestUri != null && !"".equals(requestUri)){
            doc.add(new Field("requestUri",requestUri, DOC_TYPE));
        }
        doc.add(new IndexdDocNumField("timeStamp",eventObject.getTimeStamp()));
        doc.add(new StringField("level",eventObject.getLevel().levelStr.toLowerCase(), Field.Store.NO));

        doc.add(new Field("log",defaultIfnull(eventObject.getLoggerName()).toLowerCase(),DOC_TYPE));
        doc.add(new Field("message", defaultIfnull(eventObject.getFormattedMessage()),DOC_TYPE));
        
        doc.add(new Field("content",defaultIfnull(content),DOC_TYPE));
        doc.add(new StoredField("content",defaultIfnull(content)));

        return doc;
    }

    private String defaultIfnull(String s){
        if(s == null){
            return "null";
        }
        return s;
    }
    @Override
    protected IndexWriterConfig buildWriterConfig() {
        Analyzer stopAnalyzer;
        try (InputStream  in = ClassicLuceneAppder.class.getResourceAsStream("/StopWord.txt")){
            Reader stopWords = new InputStreamReader(in);
            stopAnalyzer = new StopCharAnalyzer(stopWords);
        } catch (Exception e) {
            stopAnalyzer = new StopAnalyzer((CharArraySet)null);
        }
        return new IndexWriterConfig(new PerFieldAnalyzerWrapper(stopAnalyzer));
    }

    public void setEncoder(Encoder<ILoggingEvent> encoder) {
        this.encoder = encoder;
    }

    public void setCharset(Charset charset) {
        this.charset = charset;
    }

    public void setMaxContentSize(int maxSize) {
        this.maxContentSize = maxSize;
    }

    class IndexdDocNumField extends Field {
        IndexdDocNumField(String name, Long value) {
            super(name, TIME_STAMP);
            fieldsData = value;
        }
    }
}
