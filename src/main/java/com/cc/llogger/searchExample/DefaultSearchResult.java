package com.cc.llogger.searchExample;

import org.apache.lucene.search.Query;

import java.util.List;

/**
 * DefaultSearchResult: ${description}
 *
 * @author chenhao
 * @version 1.0
 * @date 2021-2-26 15:35
 */
public class DefaultSearchResult {

    private List<String> docs;

    private long total;

    private Query query;

    public DefaultSearchResult(List<String> docs,long total , Query query){
        this.docs = docs;
        this.total = total;
        this.query = query;
    }

    public List<String> getDocs(){
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
