package Controller;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    MessageService messageService;
    AccountService accountService;
    public SocialMediaController() {
        messageService = new MessageService();
        accountService = new AccountService();
    }
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::postRegisterUser);
        app.post("/login", this::postLoginUser);
        app.post("/messages", this::postCreateMessage);
        app.get("/messages", this::getAllMessages);
        app.get("/messages/{message_id}", this::getMessageByID);
        app.delete("/messages/{message_id}", this::postDeleteMessage);
        app.patch("/messages/{message_id}", this::postUpdateMessageByID);
        app.get("/accounts/{account_id}/messages", this::getMessagesByAccountID);
        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void postRegisterUser(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        if(account.username == null || account.password.length() < 4 || accountService.checkExists(account.username) != null || account.username.isEmpty()) {
            ctx.status(400);
        }else{
            Account addedAccount = accountService.registerAccount(account.username, account.password);
            ctx.json(mapper.writeValueAsString(addedAccount));
            ctx.status(200);
        }
    }
    private void postLoginUser(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account loginAccount = accountService.loginAccount(account);
        if(loginAccount == null) {
            ctx.status(401);
        } else {
            ctx.json(mapper.writeValueAsString(loginAccount));
            ctx.status(200);
        }
    }

    public void postCreateMessage(Context ctx) throws JsonProcessingException {
        Message message = ctx.bodyAsClass(Message.class);
        Integer post_id = message.getPosted_by();
        String text = message.getMessage_text();

        if(accountService.checkByID(post_id) == null) {
            ctx.status(400);
            return;
        }

        if(text == null || text.isEmpty() || text.length() >= 255) {
            ctx.status(400);
            return;
        }

        Message created = messageService.CreateMessage(message);
        ctx.status(200).json(created);
    }

    public void getAllMessages(Context ctx) throws JsonProcessingException {
        ctx.json(messageService.GetAllMessages()).status(200);
    }

    public void getMessageByID(Context ctx) throws JsonProcessingException {
        int id = Integer.parseInt(ctx.pathParam("message_id"));
        Message found = messageService.GetMessageByID(id);
        if(found == null) {
            ctx.status(200);
        } else {
            ctx.json(found).status(200);
        }
    }
    
    public void postDeleteMessage(Context ctx) throws JsonProcessingException {
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message exists = messageService.GetMessageByID(message_id);
        if(exists == null) {
            ctx.status(200);
            return;
        }
        messageService.DeleteMessageByID(message_id);
        ctx.json(exists).status(200);
    }

    public void postUpdateMessageByID(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        String message_text = message.getMessage_text();
        if(message_text == null || message_text.isEmpty() || message_text.length() >= 255) {
            ctx.status(400);
            return;
        }
        Message exists = messageService.GetMessageByID(message_id);
        if(exists == null) {
            ctx.status(400);
            return;
        }
        Message updatedMessage = messageService.UpdateMessage(message_id, message_text);
        ctx.status(200).json(updatedMessage);
        }

    public void getMessagesByAccountID(Context ctx) throws JsonProcessingException {
        int account_id = Integer.parseInt(ctx.pathParam("account_id"));
        ctx.json(messageService.GetMessagesByAccountID(account_id));
    }
}