package lucene.search;

import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TopDocs;

public class SimpleParseSearchAdapter implements SearchAdaper{


    @Override
    public TopDocs doSearch(IndexSearcher searcher, String query, int perpage, Sort sort) {
        return null;
    }
}
