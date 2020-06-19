package com.google.sps.servlets;

import java.lang.*;
import java.text.SimpleDateFormat; 
import java.util.Date; 
import java.io.PrintWriter;
import com.google.sps.data.Comment;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import com.google.gson.Gson;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/data")
public class DataServlet extends HttpServlet {
    List <String> msgs = new ArrayList<>();
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UserService userService = UserServiceFactory.getUserService(); 
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        String id = userService.getCurrentUser().getUserId();
        Entity commentEntity = new Entity("Comment",id);
        String name = request.getParameter("userName");
        String comment = request.getParameter("comment");
        long timeStamp =  System.currentTimeMillis();
        String userEmail = userService.getCurrentUser().getEmail();
        //dealing  with an all whitespace input in name and comment
        if (comment.trim().length() == 0) {
            response.setContentType("text/html");
            response.sendRedirect("/contact/?invalid=true");
            return; 
        }
        if (name.trim().length() == 0) {
            commentEntity.setProperty("userName","Anonymous");
        }
        else {
            commentEntity.setProperty("userName", name);
        }
        commentEntity.setProperty("email", userEmail);
        commentEntity.setProperty("userId", id);
        commentEntity.setProperty("text", comment);
        commentEntity.setProperty("timeStamp", timeStamp);
        datastore.put(commentEntity);
        response.sendRedirect("/contact/#commentSection");
    }

   @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List <Comment> commentRecord = new ArrayList<>();
        Query query = new Query("Comment").addSort("timeStamp", SortDirection.DESCENDING);
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        PreparedQuery results = datastore.prepare(query);
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");
        //populate the array list with comment objects after getting info from datastore  
        for (Entity entity : results.asIterable()) {  
            String userName = (String) entity.getProperty("userName");
            String text = (String) entity.getProperty("text");
            long timeStamp = (long) entity.getProperty("timeStamp");
            Date resultdate = new Date(timeStamp);
            String readableDate = sdf.format(resultdate);
            Comment newComment = new Comment(text, userName, readableDate);
            commentRecord.add(newComment);
        } 
        String commentJson = convertoJsonUsingGson(commentRecord);
        response.setContentType("application/json;");
        response.getWriter().println(commentJson);
    }

    private String convertoJsonUsingGson(List commentList) {
        Gson gson = new Gson();
        String resultString = gson.toJson(commentList);
        return resultString; 
    }
}