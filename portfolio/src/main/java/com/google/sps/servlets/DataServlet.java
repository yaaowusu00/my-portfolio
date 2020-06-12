package com.google.sps.servlets;

import java.lang.*;
import java.text.SimpleDateFormat; 
import java.util.Date; 
import com.google.sps.data.Comment;
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
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Entity commentEntity = new Entity("Comment");
        String name = request.getParameter("userName");
        String comment = request.getParameter("comment");
        long timeStamp =  System.currentTimeMillis();

//deal with whitespace in both categories 
        if (comment.trim().length() == 0 || comment.equals("Add a comment...")) {
            response.sendRedirect("/contact.html");
            return; 
        }
        if (name.trim().length() == 0 || name.equals("Your name...")) {
            commentEntity.setProperty("userName","Anonymous");
        }
        else {
            commentEntity.setProperty("userName", name);
        }
        commentEntity.setProperty("text", comment);
        commentEntity.setProperty("timeStamp", timeStamp);
        datastore.put(commentEntity);
        
        response.sendRedirect("./contact.html#commentSection");
    }
    
    private List getNameandComment(HttpServletRequest request, String request1, String request2) {
        String name = request.getParameter(request1);
        String comment = request.getParameter(request2);
        List <String> res = new ArrayList<>();
        if (comment.equals("")) {
            return res; 
        }
        if (name.equals("")) {
            res.add(comment);
            return  res; 
        }
        res.add(name);
        res.add(comment);
        return res; 
    }

   @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List <Comment> commentRecord = new ArrayList<>();
        Query query = new Query("Comment").addSort("timeStamp", SortDirection.DESCENDING);
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        PreparedQuery results = datastore.prepare(query);
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");  
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

    private List generateMsgs() {
        List <String> msgs = new ArrayList<>();
        msgs.add("hello");
        msgs.add("nice to meet you");
        msgs.add("thanks for visiting");
        return msgs;
    }
}