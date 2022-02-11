package com.gwhn.elasticsearch.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author banxian1804@qq.com
 * @date 2022/2/9 12:06
 */
@Controller
public class PageController {

    @RequestMapping("/index")
    public ModelAndView indexModelAndView(ModelAndView modelAndView){
        modelAndView.setViewName("/index.html");
        return modelAndView;
    }
}
