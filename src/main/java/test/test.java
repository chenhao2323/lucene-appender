package test;

import log.ClassicLuceneAppder;
import lucene.analyzer.TStopAnalyzer;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.tokenattributes.TermToBytesRefAttribute;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class test {

    public static void  main(String[] args) throws IllegalAccessException, InstantiationException, IOException {
        Analyzer analyer = new StopAnalyzer((CharArraySet)null);
        InputStream in = ClassicLuceneAppder.class.getResourceAsStream("/StopWord.txt");
        Reader stopWords = new InputStreamReader(in);
        analyer = new TStopAnalyzer(stopWords);

        System.out.println(Character.isLetter('1'));

        TokenStream stream = analyer.tokenStream("tt","测试异常java.lang.NullPointerException:55");
        stream.reset();
        TermToBytesRefAttribute t = stream.getAttribute(TermToBytesRefAttribute.class);
        while(stream.incrementToken()){
            System.out.println(t.getBytesRef().utf8ToString());
        }
//        System.out.println(1);
    }
}
