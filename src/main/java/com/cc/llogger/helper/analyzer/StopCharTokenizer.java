package com.cc.llogger.helper.analyzer;

import org.apache.lucene.analysis.util.CharTokenizer;

import java.util.Set;

/**
 * CustomTokenizer: ${description}
 *
 * @author chenhao
 * @version 1.0
 * @date 2021-3-3 17:20
 */
public class StopCharTokenizer extends CharTokenizer {

    private Set<Character> stopChars;

    public StopCharTokenizer(Set<Character> stopChars) {
        this.stopChars = stopChars;
    }

    @Override
    protected boolean isTokenChar(int c) {
        return !stopChars.contains((char)c);
    }


}
