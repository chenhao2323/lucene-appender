package lucene.search;

import lucene.LuceneContextBase;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TopDocs;

import java.io.IOException;

public class SearchFacade {

    private static LuceneContextBase context;

    private SearchAdaper searchAdaper;

    public static void setContext(LuceneContextBase newContext) {
        context = newContext;
    }

    public TopDocs search(String query, int hitsPerPage, Sort sort) throws IOException {
        if (context == null) {
            throw new IllegalStateException("no context set");
        }
        return searchAdaper.doSearch(context.getSearcher(), query, hitsPerPage,sort);
    }


}
