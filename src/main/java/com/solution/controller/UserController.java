package com.solution.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.solution.model.AddUserReq;
import com.solution.model.User;
import com.solution.threadlocal.UserThreadLocalHold;
import com.solution.utils.IOUtil;
import com.solution.utils.JackJsonUtil;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
public class UserController {

    @RequestMapping("/admin/addUser")
    public String addUser(HttpServletRequest request, HttpServletResponse response, @RequestBody AddUserReq addUserReq) {
        User user = UserThreadLocalHold.threadLocal.get();
        if (user != null && "admin".equals(user.getRole())) {
            String context = JackJsonUtil.getJson(addUserReq);
            IOUtil.str2File("./db.txt", context, true, "utf-8");
            return "success";
        } else {
            return "no access to this endpoint";
        }
    }

    @RequestMapping("/user/{resource}")
    public String resourceCheck(HttpServletRequest request, HttpServletResponse response, @PathVariable("resource") String resource) {
        List<String> list = IOUtil.file2List("db.txt", "utf-8");
        if (list != null) {
            for (String line : list) {
                AddUserReq addUserReq = JackJsonUtil.getObject(line, new TypeReference<AddUserReq>() {
                });
                if (UserThreadLocalHold.threadLocal.get().getUserId() == addUserReq.getUserId()
                        && addUserReq.getEndpoint().contains(resource)) {
                    return "have this access";
                }
            }
        }
        return "not have this access";
    }
}