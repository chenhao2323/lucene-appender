package lucene.search;

import org.apache.lucene.search.Query;

import java.util.List;

/**
 * SearchResult: ${description}
 *
 * @author chenhao
 * @version 1.0
 * @date 2021-2-26 15:35
 */
public class SearchResult {

    private List docs;

    private long total;

    private Query query;

    public SearchResult(List docs,long total , Query query){
        this.docs = docs;
        this.total = total;
        this.query = query;
    }

    public List getDocs(){
        return docs;
    }

    public long getTotal(){
        return total;
    }

    public Query getQuery() {
        return query;
    }

    public void setQuery(Query query) {
        this.query = query;
    }
}
