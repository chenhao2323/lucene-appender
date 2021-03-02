package test;

import ch.qos.logback.classic.ClassicConstants;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;

/**
 * LogSearchFilter: ${description}
 *
 * @author chenhao
 * @version 1.0
 * @date 2021-3-2 16:09
 */
public class LogSearchFilter extends Filter<ILoggingEvent> {
    @Override
    public FilterReply decide(ILoggingEvent event) {
        String uri = event.getMDCPropertyMap().get(ClassicConstants.REQUEST_REQUEST_URI);
        if("/znbq/search".equals(uri)){
            return  FilterReply.DENY;
        }
        return FilterReply.NEUTRAL;
    }
}
