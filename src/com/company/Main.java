package com.company;

import spark.ModelAndView;
import spark.Session;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.ArrayList;
import java.util.HashMap;

public class Main {

    static HashMap<String,User> users = new HashMap<>();
    static ArrayList<Message> messages = new ArrayList<>();

    public static void main(String[] args) {
        users.put("Alice", new User("Alice","pass"));
        users.put("Bob",new User("Bob","pass"));
        users.put("Charlie",new User("Charlie","pass"));

        messages.add(new Message(0,-1,"Alice","Hello everyone!"));
        messages.add(new Message(1,-1,"Bob","This is another thread!"));
        messages.add(new Message(2,0,"Charlie","Cool thread, Alice!"));
        messages.add(new Message(3,2,"Alice","Thanks, Charlie!"));

        Spark.get(
                "/",
                (request, response) -> {
                    String replyId = request.queryParams("replyId");
                    int idNum = -1;
                    if (replyId != null) {
                        idNum = Integer.valueOf(replyId);
                    }
                    Session session = request.session();
                    String name = session.attribute("loginName");


                    HashMap m = new HashMap();
                    ArrayList<Message> msgs = new ArrayList<>();
                    for (Message message : messages) {
                        if (message.replyId == idNum) {
                            msgs.add(message);
                        }
                    }
                    m.put("messages",msgs);
                    m.put("name",name);
                    m.put("replyId",idNum);
                    return new ModelAndView(m, "home.html");
                },
                new MustacheTemplateEngine()
        );

        Spark.post(
                "/login",
                (request,response) -> {
                    String name = request.queryParams("loginName");
                    String password = request.queryParams("password");
                    User user = users.get(name);
                    if (user == null) {
                        user = new User(name,password);
                        users.put(name,user);
                    }
                    else if(!password.equals(user.password)) {
                        Spark.halt(403);
                        return null;
                    }
                    Session session = request.session();
                    session.attribute("loginName",name);
                    response.redirect("/");
                    return null;
                }

        );

        Spark.post(
                "/create-message",
                (request, response) -> {
                    String text = request.queryParams("text");
                    int replyId = Integer.valueOf(request.queryParams("replyId"));
                    Session session = request.session();
                    String name = session.attribute("loginName");
                    Message message = new Message(messages.size(),replyId,name,text);
                    messages.add(message);
                    response.redirect("/");
                    return null;
                }
        );

    }
}
