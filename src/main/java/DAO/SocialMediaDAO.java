package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Model.*;
import Util.ConnectionUtil;

/**
 * Database Access Object create sql statements and executes them to the database.
 * Methods have names based on the action being taken on the database (e.x select, drop, update, insert, etc.)
 */
public class SocialMediaDAO {
    //put connection here so it doesn't declare it in each method scope.
    private Connection connection = ConnectionUtil.getConnection();

    /**
     * 
     * @param account handles username and password parameters in the accout object
     * @return returns account with account_id field on success, null on failure
     */
    public Account selectAccount(Account account) {
        try {
            String sql = "SELECT * FROM account WHERE username = ? AND password = ?";
            PreparedStatement ps = this.connection.prepareStatement(sql);
            ps.setString(1, account.getUsername());
            ps.setString(2, account.getPassword());
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                Account returnAccount = new Account(rs.getInt("account_id"),
                    rs.getString("username"),
                    rs.getString("password"));
                return returnAccount;
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        //if not returned in the try block then it failed or doesn't exist
        return null;
    }
    /**
     * Adds an account to the database. Identical accounts are protected via the UNIQUE column constraint
     * 
     * @param account takes an account model as parameter using
     * the overloaded constructor that doesn't take an account id
     * @return returns the account object with the assigned id now. Returns null on error
     * */
    public Account insertAccount(Account account) {
        try {
            String sql = "INSERT INTO account (username, password) VALUES (?, ?);";
            PreparedStatement ps = this.connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, account.getUsername());
            ps.setString(2, account.getPassword());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next()){
                int account_id = (int) rs.getLong(1);
                account.setAccount_id(account_id);
                return account;
            }
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Deletes account record from database not in requirements
     * @param account
     * @return
     */
    public Account deleteAccount(Account account) {
        try {
            String sql = "DELETE FROM account WHERE account_id = ?";
            PreparedStatement ps = this.connection.prepareStatement(sql);
            ps.setInt(1, account.getAccount_id());
            int deleted = ps.executeUpdate();
            if(deleted > 0){
                System.out.println("Account Deleted");
                return account;
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    //
    public Message insertMessage(Message message) {
        try {
            String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";
            PreparedStatement ps = this.connection.prepareStatement(sql);
            ps.setInt(1, message.getPosted_by());
            ps.setString(2, message.getMessage_text());
            ps.setLong(3, message.getTime_posted_epoch());
            int updated = ps.executeUpdate();
            if(updated > 0){
                System.out.println("Message Inserted");
                return message;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
    public Message selectMessage(int message_id) {
        try {
            String sql = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement ps = this.connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Message mesage = new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch"));
                    return mesage;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
    public List<Message> selectAllMessages() {
        List<Message> messages = new ArrayList<>();
        try {
            String sql = "SELECT * FROM message";
            PreparedStatement ps = this.connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Message mesage = new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch"));
                messages.add(mesage);
            }
            return messages;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
    public List<Message> selectAllMessagesFromAccountId(int account_id) {
        List<Message> messages = new ArrayList<>();
        try {
            String sql = "SELECT * FROM message WHERE posted_by = ?";
            PreparedStatement ps = this.connection.prepareStatement(sql);
            ps.setInt(1, account_id);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Message mesage = new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch"));
                messages.add(mesage);
            }
            return messages;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
    public Message deleteMessage(int message_id){
        try {
            Message message = selectMessage(message_id);
            String sql = "DELETE FROM message WHERE message_id = ?;";
            PreparedStatement ps = this.connection.prepareStatement(sql);
            ps.setInt(1, message_id);
            int deleted = ps.executeUpdate();
            if(deleted > 0){
                System.out.println("Message Deleted");
                return message;
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
    /**
     * 
     * @param message_id id value of the message provided from the pathParam
     * @param message_text
     * @return
     */
    public Message updateMessage(int message_id, String message_text){
        try {
            String sql = "UPDATE message SET message_text = ? WHERE message_id = ?";
            PreparedStatement ps = this.connection.prepareStatement(sql);
            ps.setString(1, message_text);
            int updated = ps.executeUpdate();
            if(updated > 0){
                System.out.println("Message Updated");
                return selectMessage(message_id);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
    
    
}