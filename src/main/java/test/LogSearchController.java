package test;

import lucene.search.SearchFacade;
import lucene.search.SearchResult;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Optional;

@RestController
public class LogSearchController {

    private static Logger logger = LoggerFactory.getLogger(LogSearchController.class);

    String htmlStart = "<html><head><meta charset=\"utf-8\"></head><body>";
    String title = "<div>总数:%s</div><div>查询计划:%s</div>";
    String htmlEnd = "</body></html>";

    @RequestMapping("search")
    public String search(String query,Integer page,Boolean reverse){
        Sort s = new Sort(new SortField("timeStamp'", SortField.Type.LONG, Optional.ofNullable(reverse).orElse(true)));
        SearchResult result;
        try {
            result = SearchFacade.search(query,page,s);
        } catch (IOException e) {
            e.printStackTrace();
            return e.getMessage();
        }
        StringBuilder sb = new StringBuilder(htmlStart);
        sb.append(String.format(title,result.getTotal(),result.getQuery().toString()));
        if(!CollectionUtils.isEmpty(result.getDocs())){
            result.getDocs().forEach(o ->{
                sb.append("<p>");
                sb.append(o.toString());
                sb.append("</p>");
            });
        }
        sb.append(htmlEnd);
        return sb.toString();
    }

    @RequestMapping("test")
    public String test(String query,Integer page,String sort){
        return htmlStart+"<h1>我的第一个标题</h1>"+htmlEnd;
    }

    @RequestMapping("testError")
    public String error(String query,Integer page,String sort){
        String s= null;
        try {
            s.equals("");
        }catch (Exception e){
            logger.error("测试异常",e);
        }

        return "error";
    }
}
