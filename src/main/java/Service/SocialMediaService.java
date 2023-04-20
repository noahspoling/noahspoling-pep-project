package Service;

import java.util.List;
import DAO.SocialMediaDAO;
import Model.*;

/**
 * Service that connects controller endpoints to database access objects
 * method naming terminology uses CRUD verbs for what action is being taken
 */
public class SocialMediaService {
    SocialMediaDAO socialMediaDAO;
    
    public SocialMediaService(){
        this.socialMediaDAO = new SocialMediaDAO();
    }

    /*
     * Overloaded Contructor for testing Service Independent of DAO
     */
    public SocialMediaService(SocialMediaDAO socialMediaDAO) {
        this.socialMediaDAO = socialMediaDAO;
    }
    /**
     * Used to add account record to database
     * 
     * @param account account object using the constructor without the id as a parameter
     * @return returns account with id value or null if there was an error
     */
    public Account register(Account account) {
        return this.socialMediaDAO.insertAccount(account);
    }
    /**
     * Used to add account record to database
     * 
     * @param account account object using the constructor without the id as a parameter
     * @return returns account with id value or null if there was an error or account doesn't exist
     */
    public Account login(Account account) {
        account = this.socialMediaDAO.selectAccount(account);
        if(account.getAccount_id() == 0 || account == null) {
            return null;
        }
        else {
            return account;
        }
    }
    /**
     * Returns message from a given message id
     * @param account_id integer - id value of an account
     * @return List<Message> - List of Message object for that account, null on fail or none
     */
    public Message createMessage(Message message) {
        return this.socialMediaDAO.insertMessage(message);
    }
    /**
     * Returns message from a given message id
     * @param account_id integer - id value of an account
     * @return List<Message> - List of Message object for that account, null on fail or none
     */
    public Message readMessage(int message_id){
        return this.socialMediaDAO.selectMessage(message_id);
    }
    /**
     * Returns all Messages
     * @param account_id integer - id value of an account
     * @return List<Message> - List of Message object for that account, null on fail or none
     */
    public List<Message> readAllMessages(){
        return this.socialMediaDAO.selectAllMessages();
    }
    /**
     * Returns all Messages from a given account id
     * @param account_id integer - id value of an account
     * @return List<Message> - List of Message object for that account, null on fail or none
     */
    public List<Message> readAllMessagesById(int account_id){
        return this.socialMediaDAO.selectAllMessagesFromAccountId(account_id);
    }
    /**
     * 
     * @param message_id integer - id value of the message
     * @param message_text String - text value of the message
     * @return Updated Message object
     */
    public Message updateMessage(int message_id, String message_text){
        return this.socialMediaDAO.updateMessage(message_id, message_text);
    }
    /**
     * 
     * @param message_id integer: id value of the message
     * @return returns the deleted message object on successful deletion
     */
    public Message deleteMessage(int message_id){
        return this.socialMediaDAO.deleteMessage(message_id);
    }
}
