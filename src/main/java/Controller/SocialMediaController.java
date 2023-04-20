package Controller;

import Model.Account;
import Model.Message;
import Service.SocialMediaService;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 * Controller uses Rest related verbs to describe method behavior.
 */
public class SocialMediaController {

    private SocialMediaService socialMediaService;

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::registerHandler);
        app.post("/login", this::loginHandler);
        app.post("/messages", this::postMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageHandler);
        app.delete("/messages/{message_id}", this::deleteMessageHandler);
        app.patch("/mesages/{message_id}", this::patchMessageHandler);
        app.get("/accounts/{account_id}/messages", this::getMessagesByIdHandler);
        return app;
    }

    /**
     * Uses Object mapper to turn json request body into an account object
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     * @throws JsonProcessingException
     * @throws JsonMappingException
     */
    private void registerHandler(Context context) throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account;
        account = mapper.readValue(context.body(), Account.class);
        Account addedAccount = socialMediaService.register(account);
        if (addedAccount == null) {
            context.status(400);
        } else {
            context.json(mapper.writeValueAsString(addedAccount));
        }
        
    }
    /**
     * 
     * @param context
     * @throws JsonMappingException
     * @throws JsonProcessingException
     */
    private void loginHandler(Context context) throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account;
        account = mapper.readValue(context.body(), Account.class);
        Account addedAccount = socialMediaService.login(account);
        if (addedAccount == null) {
            context.status(400);
        } else {
            context.json(mapper.writeValueAsString(addedAccount));
        }
    }
    private void getMessageHandler(Context context) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        int message_id = Integer.parseInt(context.pathParam("message_id"));
        Message message = socialMediaService.readMessage(message_id);
        if (message == null) {
            context.status(400);
        } else {
            context.json(mapper.writeValueAsString(message));
        }
    }
    private void getAllMessagesHandler(Context context) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        List<Message> messages = socialMediaService.readAllMessages();
        if (messages.isEmpty()) {
            context.status(400);
        } else {
            context.json(mapper.writeValueAsString(messages));
        }
    }
    private void getMessagesByIdHandler(Context context) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        List<Message> messages = socialMediaService.readAllMessages();
        int account_id = Integer.parseInt(context.pathParam("account_id"));
        context.json(socialMediaService.readAllMessagesById(account_id));
        if (messages.isEmpty()) {
            context.status(400);
        } else {
            context.json(mapper.writeValueAsString(messages));
        }
    }
    private void postMessageHandler(Context context) throws JsonMappingException, JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(context.body(), Message.class);
        message = socialMediaService.createMessage(message);
        if (message == null) {
            context.status(400);
        } else {
            context.json(mapper.writeValueAsString(message));
        }
    }
    private void patchMessageHandler(Context context) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        int message_id = Integer.parseInt(context.pathParam("message_id"));
        Message message = socialMediaService.readMessage(message_id);
        //Return body of Json object as a string
        String message_text = context.body();
        System.out.print(message_text);
        message.setMessage_text(message_text);
        message = socialMediaService.updateMessage(message_id, message_text);
        if (message == null) {
            context.status(400);
        } else {
            context.json(mapper.writeValueAsString(message));
        }
    }
    private void deleteMessageHandler(Context context) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        int message_id = Integer.parseInt(context.pathParam("message_id"));
        Message message = socialMediaService.readMessage(message_id);
        if (message == null) {
            context.status(400);
        } else {
            context.json(mapper.writeValueAsString(message));
        }
    }
}