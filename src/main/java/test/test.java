package test;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.tokenattributes.TermToBytesRefAttribute;
import org.apache.lucene.util.BytesRef;

import java.io.IOException;

public class test {

    public static void  main(String[] args) throws IllegalAccessException, InstantiationException, IOException {
        Analyzer analyer = new StopAnalyzer((CharArraySet)null);
       // Analyzer analyer = new KeywordAnalyzer();
        System.out.println(Character.isLetter('a'));

        TokenStream stream = analyer.tokenStream("tt","thereare.Some apple,bana");
        stream.reset();
        TermToBytesRefAttribute t = stream.getAttribute(TermToBytesRefAttribute.class);
        while(stream.incrementToken()){
            System.out.println(t.getBytesRef().utf8ToString());
        }
//        System.out.println(1);
    }
}
