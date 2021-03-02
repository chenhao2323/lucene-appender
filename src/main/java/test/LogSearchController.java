package test;

import lucene.search.SearchFacade;
import lucene.search.SearchResult;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class LogSearchController {

    String htmlStart = "<html><head><meta charset=\"utf-8\"></head><body>";
    String htmlEnd = "</body></html>";
    @RequestMapping("search")
    public String search(String query,Integer page,String sort){
        Sort s = sort != null ? new Sort(new SortField(sort, SortField.Type.LONG,true)):null;
        SearchResult result;
        try {
            result = SearchFacade.search(query,page,s);
        } catch (IOException e) {
            e.printStackTrace();
            return e.getMessage();
        }
        if(result == null || CollectionUtils.isEmpty(result.getDocs())){
            return "未查询到结果";
        }
        StringBuilder sb = new StringBuilder(htmlStart);
        result.getDocs().forEach(o ->{
            sb.append("<p>");
            sb.append(o.toString());
            sb.append("</p>");
        });
        sb.append(htmlEnd);
        return sb.toString();
    }
    @RequestMapping("test")
    public String test(String query,Integer page,String sort){
        return htmlStart+"<h1>我的第一个标题</h1>"+htmlEnd;
    }
}
