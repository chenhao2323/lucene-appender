package lucene.search;

import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Sort;

import java.io.IOException;

public interface SearchAdaper {

    SearchResult doSearch(IndexSearcher searcher, String query, int perpage, Sort sort) throws IOException;

}
