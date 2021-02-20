package lucene.search;

import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.index.DocValues;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TopDocs;

import java.io.IOException;

public class DefaultLogSearchAdapter implements SearchAdaper{

    private final QueryParser queryParser = new QueryParser("content",new KeywordAnalyzer());

    @Override
    public TopDocs doSearch(IndexSearcher searcher, String query, int perpage, Sort sort) throws IOException {
        DocValues.getNumeric().nextDoc()
        searcher.getIndexReader().leaves().
        Query q;
        try {
             q = queryParser.parse(query);
        } catch (ParseException e) {
            return null;
        }
        if(sort == null){
            TopDocs topDocs =  searcher.search(q,perpage);
            topDocs.
        }
    }
}
