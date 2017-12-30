package com.nowcoder.controller;

import com.nowcoder.model.User;
import com.nowcoder.service.WendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;


@Controller
public class IndexController {

    private final
    WendaService wendaService;

    @Autowired
    public IndexController(WendaService wendaService) {
        this.wendaService = wendaService;
    }


    @RequestMapping(path = {"/", "/index", "/profile"}, method = {RequestMethod.GET})
    @ResponseBody
    public String index(HttpSession session) {
        return wendaService.getMessages(2) + "Hello, world!" + session.getAttribute("msg");
    }

    @RequestMapping(path = {"/profile/{groupId}/{userId}"})
    @ResponseBody
    public String index(@PathVariable("userId") int userId,
                        @PathVariable("groupId") String groupId,
                        @RequestParam(value = "type", required = false) int type,
                        @RequestParam(value = "key", defaultValue = "pp")  String key) {
        return String.format("Profile for %s/%d, type:%d, key:%s",
                groupId, userId, type, key);
    }

    @RequestMapping(path = {"/fm/{userName}"}, method = {RequestMethod.GET})
    public String template(Model model,
                           @PathVariable("userName") String userName) {
        model.addAttribute("value1", "papapapppa");
        List<String> colors = Arrays.asList( new String[] {"GREEN", "RED", "BULE"});
        model.addAttribute("colors", colors);
        model.addAttribute("userName", userName);

        Map<String, String> map = new HashMap<>();

        for (int i = 0; i < 5; i++) {
            map.put(String.valueOf(i), String.valueOf(i*i));
        }

        model.addAttribute("map", map);
        model.addAttribute("user",new User(1, "admin"));

        return "hello";
    }

    @RequestMapping(path = {"/request"}, method = {RequestMethod.GET})
    @ResponseBody
    public String request(Model model,
                          HttpServletRequest request,
                          HttpServletResponse response,
                          HttpSession session,
                          @CookieValue("JSESSIONID") String sessionId) {

        StringBuilder sb = new StringBuilder();
        sb.append("CookieValue: ").append(sessionId);

        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements() ) {
            String name = headerNames.nextElement();
            sb.append(name).append(" : ").append(request.getHeader(name)).append("<br>");
        }

        if (request.getCookies() != null) {
            for (Cookie cookie: request.getCookies()) {
                sb.append("Cookie:").append(cookie.getName()).
                        append("value: ").append(cookie.getValue());
            }
        }
        sb.append(request.getMethod()).append("<br>");
        sb.append(request.getQueryString()).append("<br>");
        sb.append(request.getRequestURL()).append("<br>");
        sb.append(request.getPathInfo()).append("<br>");

        response.addHeader("nowcoder", "hello");
        response.addCookie(new Cookie("nowcoder", "msg"));
        return sb.toString();
    }

    @RequestMapping(path = {"/redirect/{code}"}, method = {RequestMethod.GET})
    public RedirectView redirect(Model model,
                                 HttpSession session,
                                 @PathVariable("code") int code) {


        session.setAttribute("msg", "jump msg from /redirect");

        RedirectView re = new RedirectView("/", true);
        if (code == 301) {
            re.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
        }

        return re;
    }

    @RequestMapping(path = {"/{user}"}, method = {RequestMethod.GET})
    @ResponseBody
    public String admin(@PathVariable("user") String user) {
        if (user.equals("admin")) {
            return " Hello, admin! ";
        }

        throw new IllegalArgumentException("路径错误");
    }

    @ExceptionHandler()
    @ResponseBody
    public String err(Exception e) {
        return "err: " + e.getMessage();
    }
}
