package lucene.search;

import log.ClassicLuceneAppder;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.standard.StandardQueryParser;
import org.apache.lucene.queryparser.flexible.standard.config.StandardQueryConfigHandler;
import org.apache.lucene.search.*;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleFragmenter;
import lucene.analyzer.TStopAnalyzer;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;
import java.util.stream.Collectors;

public class DefaultLogSearchAdapter implements SearchAdaper{

    private final StandardQueryParser queryParser;
    private final Analyzer hightLightAnalyzer;
    {

        Analyzer t;
        try {
            InputStream in = ClassicLuceneAppder.class.getResourceAsStream("/StopWord.txt");
            Reader stopWords = new InputStreamReader(in);
            t = new TStopAnalyzer(stopWords);
        } catch (Exception e) {
            t = new StopAnalyzer((CharArraySet)null);
        }
        hightLightAnalyzer = t;

        Map<String,Analyzer> m = new HashMap<>();
        m.put("content",t);
        m.put("message",t);
        m.put("logger",t);
        Analyzer analyer = new PerFieldAnalyzerWrapper(new KeywordAnalyzer(),m);
        queryParser = new StandardQueryParser(analyer);
        queryParser.setDefaultOperator(StandardQueryConfigHandler.Operator.AND);

    }



    @Override
    public SearchResult doSearch(IndexSearcher searcher, String query, int perpage, Sort sort) throws IOException {
        Query q;
        try {
             q = queryParser.parse(query,"content");
        } catch (QueryNodeException e) {
            throw new IllegalArgumentException(e);
        }
        IndexReader reader = searcher.getIndexReader();
        TopDocs topDocs;
        if(sort == null){
            topDocs = searcher.search(q,perpage);
        }else{
            topDocs = searcher.search(q,perpage,sort);
        }
        List<Document> docs = new LinkedList<>();
        for(ScoreDoc sDoc:topDocs.scoreDocs){
            docs.add(reader.document(sDoc.doc));
        }
        Highlighter hightLighter = new Highlighter(new QueryScorer(q));
        hightLighter.setTextFragmenter(new SimpleFragmenter(10000));
        hightLighter.setEncoder(s ->{
            if(s == null || "".equals(s)){
                return s;
            }
            return s.replaceAll("\\r\\n","</br>");
        });
        List<String> result = docs.stream().map(d ->{
            String content = d.get("content");
            if(content.length() > 9001){
                content=content.substring(0,9000);
            }
            try {
                content = Optional.ofNullable(hightLighter.getBestFragment(hightLightAnalyzer,"content",d.get("content"))).orElse(content);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return  content;
        }).collect(Collectors.toList());

        return new SearchResult(result,topDocs.totalHits.value,q);
    }
}
