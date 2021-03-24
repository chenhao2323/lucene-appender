package com.cc.llogger.helper.analyzer;

import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.StopwordAnalyzerBase;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;

import java.io.Reader;

/**
 * StopCharAnalyzer: ${description}
 *
 * @author chenhao
 * @version 1.0
 * @date 2021-3-3 17:14
 */
public class StopCharAnalyzer extends StopwordAnalyzerBase {

    private Tokenizer source;

    public StopCharAnalyzer(Reader stopWords){
        super();
        source = new StopCharTokenizer(stopWords);
    }

    @Override
    protected TokenStreamComponents createComponents(String fieldName) {
        return new TokenStreamComponents(source, new LowerCaseFilter(source));
    }

    @Override
    protected TokenStream normalize(String fieldName, TokenStream in) {
        return new LowerCaseFilter(in);
    }
}
