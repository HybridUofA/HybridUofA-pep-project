package Service;
import Model.Message;
import DAO.MessageDAO;

import java.util.List;

public class MessageService {
    MessageDAO messageDAO;

    public MessageService() {
        messageDAO = new MessageDAO();
    }

    public MessageService(MessageDAO messageDAO) {
        this.messageDAO = messageDAO;
    }

    public Message CreateMessage(Message message) {
        return messageDAO.createMessage(message);
    }

    public Message UpdateMessage(int id, String message_text) {
        messageDAO.updateMessage(id, message_text);
        return messageDAO.getMessageByID(id);
    }

    public List<Message> GetAllMessages() {
        return messageDAO.getAllMessages();
    }

    public List<Message> GetMessagesByAccountID(int id) {
        return messageDAO.getMessagesByAccountID(id);
    }

    public Message DeleteMessageByID(int id) {
        return messageDAO.deleteMessageByID(id);
    }

    public Message GetMessageByID(int id) {
        return messageDAO.getMessageByID(id);
    }
}
