package com.cc.llogger.log;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import com.cc.llogger.helper.Constants;
import com.cc.llogger.helper.SearchHelper;
import com.cc.llogger.writer.CommitStatistics;
import com.cc.llogger.writer.SimpleLocalFileWriterFactory;
import com.cc.llogger.writer.WriterFactory;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public abstract class LunceneAppenderBase extends UnsynchronizedAppenderBase<ILoggingEvent> {

    private String indexUrl;

    private WriterFactory indexWriterFactory = new SimpleLocalFileWriterFactory();

    private int commitInterval = 60;


    @Override
    protected void append(ILoggingEvent eventObject) {
        if (!isStarted()) {
            return;
        }
        if(eventObject.getMarker() != null && eventObject.getMarker().contains(Constants.NOT_INDEX_MARKER)){
            return ;
        }
        try {
            Document d = buildDocument(eventObject);
            if(d != null){
                getWriter().addDocument(d);
                CommitStatistics.addLogs();
            }
        } catch (Exception e) {
            addError("error to write index", e);
        }
    }

    protected abstract Document buildDocument(ILoggingEvent eventObject);


    @Override
    public void start() {
        if (indexUrl == null) {
            addError("no index indexUrl set");
            return;
        }
        try {
            IndexWriterConfig config = buildWriterConfig();
            indexWriterFactory.configure(indexUrl,config);
            SearchHelper.setLogWriterFactory(indexWriterFactory);
            initExcuteCommit();
        } catch (Exception e) {
            addError("error to open indexWriter from indexUrl:" + indexUrl, e);
            return;
        }
        super.start();
    }

    protected abstract IndexWriterConfig buildWriterConfig();

    private void initExcuteCommit() throws IOException {
        getContext().getScheduledExecutorService().scheduleAtFixedRate(() -> {
            try {
                excuteCommit(getWriter());
            }catch (IOException ioe){
                addWarn("commit logIndex error",ioe);
            }
        },commitInterval,commitInterval, TimeUnit.SECONDS);
    }

    protected void excuteCommit(IndexWriter writer) throws IOException {
        if (!writer.isOpen() || !writer.hasUncommittedChanges()) {
            return;
        }
        long now = System.currentTimeMillis();
        writer.commit();
        CommitStatistics.commitIndex(System.currentTimeMillis() - now);
    }

    protected IndexWriter getWriter() throws IOException{
        if(this.isStarted()){
            return indexWriterFactory.getWriter();
        }
        throw new IllegalStateException("logger not started yet");
    }

    @Override
    public void stop() {
        try {
            getWriter().close();
        } catch (IOException e) {
            addWarn("close writer error", e);
        }
        super.stop();
    }

    public void setIndexUrl(String indexUrl) {
        this.indexUrl = indexUrl;
    }

    public void setIndexWriterFactory(WriterFactory factory){
        this.indexWriterFactory = factory;
    }

    public void setCommitInterval(int commitInterval){
        this.commitInterval = commitInterval;
    }
}
