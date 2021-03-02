package lucene.search;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.standard.StandardQueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleFragmenter;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DefaultLogSearchAdapter implements SearchAdaper{

    private final StandardQueryParser queryParser;
    {
        Map<String,Analyzer> m = new HashMap<>();
        Analyzer stopA = new StopAnalyzer((CharArraySet)null);
        m.put("message",stopA);
        Analyzer analyer = new PerFieldAnalyzerWrapper(new KeywordAnalyzer(),m);
        queryParser = new StandardQueryParser(analyer);
    }



    @Override
    public SearchResult doSearch(IndexSearcher searcher, String query, int perpage, Sort sort) throws IOException {
        Query q;
        try {
             q = queryParser.parse(query,"message");
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
        hightLighter.setTextFragmenter(new SimpleFragmenter(500));

        List<String> result = docs.stream().map(d ->{
            String content = d.get("content");
            try {
                return hightLighter.getBestFragment(new StopAnalyzer((CharArraySet)null),"contentIndex",d.get("content"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return content;
        }).collect(Collectors.toList());


        return new SearchResult(result,topDocs.totalHits.value);
    }
}
