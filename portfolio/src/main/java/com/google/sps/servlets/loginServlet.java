package com.google.sps.servlets;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import java.io.IOException;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import com.google.gson.Gson;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/login")
public class loginServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UserService userService = UserServiceFactory.getUserService();
        HashMap <String, String> statusInfo = new HashMap<>();
        if (userService.isUserLoggedIn()) {
            String userEmail = userService.getCurrentUser().getEmail();
            String redirectAfterLogout = "/contact/";
            String logoutUrl = userService.createLogoutURL(redirectAfterLogout);
            statusInfo.put("status", "loggedIn");
            statusInfo.put("url", logoutUrl);
        }
        else {
            String redirectAfterLogin = "/contact/#commentSection";
            String loginUrl = userService.createLoginURL(redirectAfterLogin);
            statusInfo.put("status", "loggedOut");
            statusInfo.put("url", loginUrl);
        }
        String json = convertoJsonUsingGson(statusInfo);  
        response.setContentType("application/json;");
        response.getWriter().println(json); 
    }

    private String convertoJsonUsingGson(HashMap statusInfo) {
        Gson gson = new Gson();
        String resultString = gson.toJson(statusInfo);
        return resultString; 
    }
}