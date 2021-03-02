package lucene.search;

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

    public SearchResult(List docs,long total){
        this.docs = docs;
        this.total = total;
    }

    public List getDocs(){
        return docs;
    }

    public long getTotal(){
        return total;
    }

}
