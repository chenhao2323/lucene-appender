package lucene.search;

import lucene.LuceneContextBase;
import org.apache.lucene.search.Sort;

import java.io.IOException;

/**
 * @author chenhao
 */
public class SearchFacade {

    private SearchFacade(){}

    private static volatile LuceneContextBase context;

    private static SearchAdaper searchAdaper = new DefaultLogSearchAdapter();

    public static synchronized void setContext(LuceneContextBase newContext) {
        if(context == null){
            context = newContext;
        }else{
            throw new IllegalStateException("multiple context set");
        }
    }

    public static SearchResult search(String query, int hitsPerPage, Sort sort) throws IOException {
        if (context == null) {
            throw new IllegalStateException("no context set");
        }
        return searchAdaper.doSearch(context.getSearcher(), query, hitsPerPage,sort);
    }

}
