package log;

import ch.qos.logback.core.Appender;
import ch.qos.logback.core.Context;
import ch.qos.logback.core.LogbackException;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.ContextAwareBase;
import ch.qos.logback.core.spi.FilterReply;
import ch.qos.logback.core.status.Status;

import java.util.List;

/**
 * LuceneAppender: ${description}
 *
 * @author chenhao
 * @version 1.0
 * @date 2021-1-28 20:31
 */
public class LuceneAppender<E> extends ContextAwareBase implements Appender<E> {
    public String getName() {
        return null;
    }

    public void doAppend(E event) throws LogbackException {

    }

    public void addFilter(Filter<E> newFilter) {

    }

    public void clearAllFilters() {

    }

    public List<Filter<E>> getCopyOfAttachedFiltersList() {
        return null;
    }

    public FilterReply getFilterChainDecision(E event) {
        return null;
    }

    public void start() {

    }

    public void stop() {

    }

    public boolean isStarted() {
        return false;
    }
}
