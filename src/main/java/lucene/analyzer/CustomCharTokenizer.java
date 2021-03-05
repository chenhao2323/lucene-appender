package lucene.analyzer;

import org.apache.lucene.analysis.util.CharTokenizer;
import org.apache.lucene.util.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashSet;
import java.util.Set;

/**
 * CustomTokenizer: ${description}
 *
 * @author chenhao
 * @version 1.0
 * @date 2021-3-3 17:20
 */
public class CustomCharTokenizer extends CharTokenizer {

    private Set<Character> stopChars = new HashSet<>();

    public CustomCharTokenizer(Reader stopChars) {
        super();
        try {
            setWordSet(stopChars);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected boolean isTokenChar(int c) {
        return !stopChars.contains((char)c);
    }

    private   void setWordSet(Reader reader) throws IOException {
        BufferedReader br = null;
        try {
            StringBuilder sb = new StringBuilder();
            br = getBufferedReader(reader);
            char[] buf = new char[16];
            int i = -1;
            while( (i = br.read(buf)) != -1){
                for(int f =0;f < i;f++){
                    stopChars.add(buf[f]);
                }
            }
        }
        finally {
            IOUtils.close(br);
        }
    }

    private BufferedReader getBufferedReader(Reader reader) {
        return (reader instanceof BufferedReader) ? (BufferedReader) reader
            : new BufferedReader(reader);
    }

}
