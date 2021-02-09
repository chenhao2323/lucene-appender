package log;

import ch.qos.logback.classic.ClassicConstants;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.encoder.Encoder;
import org.apache.lucene.document.*;

public class ClassicLuceneAppder extends  LunceneAppenderBase<ILoggingEvent>{

    private Encoder<ILoggingEvent> encoder;

    @Override
    protected Document buildDocument(ILoggingEvent eventObject) {
        Document doc = new Document();
        doc.add(new StringField("requestUri",eventObject.getMDCPropertyMap().get(ClassicConstants.REQUEST_REQUEST_URI), Field.Store.NO));
        doc.add(new LongPoint("timeStamp",eventObject.getTimeStamp()));
        doc.add(new StringField("level",eventObject.getLevel().levelStr, Field.Store.NO));
        doc.add(new StoredField("content",encoder.encode(eventObject)));
        return doc;
    }

    public void setEncoder(Encoder<ILoggingEvent> encoder) {
        this.encoder = encoder;
    }
}
