package com.cc.llogger.helper.analyzer;

import com.cc.llogger.log.ClassicLuceneAppder;
import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.IOUtils;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

/**
 * StopCharAnalyzer: ${description}
 *
 * @author chenhao
 * @version 1.0
 * @date 2021-3-3 17:14
 */
public class StopCharAnalyzer extends StopwordAnalyzerBase {
    private Set<Character> stopChars = new HashSet<>();
    private Tokenizer source;
    private CharArraySet charArraySet;

    public StopCharAnalyzer(Reader stopWords){
        super();
        try {
            setWordSet(stopWords);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        source = new StopCharTokenizer(stopChars);
        charArraySet = new CharArraySet(stopChars,false);
    }

    @Override
    protected TokenStreamComponents createComponents(String fieldName) {
        return new TokenStreamComponents(source,new StopFilter(new LowerCaseFilter(source),charArraySet));
    }

    @Override
    protected TokenStream normalize(String fieldName, TokenStream in) {
        return new LowerCaseFilter(in);
    }

    private   void setWordSet(Reader reader) throws IOException {
        BufferedReader br = null;
        try {
            br = getBufferedReader(reader);
            char[] buf = new char[16];
            int i;
            while( (i = br.read(buf)) > -1){
                for(int f =0;f < i;f++){
                    stopChars.add(buf[f]);
                }
            }
        }
        finally {
            IOUtils.close(br);
        }
    }

    private BufferedReader getBufferedReader(Reader reader) {
        return (reader instanceof BufferedReader) ? (BufferedReader) reader
            : new BufferedReader(reader);
    }
}
