package com.cc.llogger.searchExample.spi;

import org.apache.lucene.search.IndexSearcher;

/**
 * SearcherFactory: ${description}
 *
 * @author chenhao
 * @version 1.0
 * @date 2021-3-9 10:41
 */
public interface SearcherFactory {
    IndexSearcher getSearcher();
}
