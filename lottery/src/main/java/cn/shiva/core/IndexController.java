package cn.shiva.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author shiva   2020/6/4 22:49
 */
@Controller
public class IndexController {

    @Autowired
    private HandleService handleService;

    @RequestMapping(value= {"/index",""})
    public String index(Model model) {
        return "index";
    }

    @ResponseBody
    @RequestMapping("gogogo")
    public List<ConcurrentHashMap<String, Integer>> gogogo(String num){
        if (num == null || num.length() == 0 ){
            return null;
        }
        handleService.handle(Integer.parseInt(num));
        return HandleService.result;
    }

}
