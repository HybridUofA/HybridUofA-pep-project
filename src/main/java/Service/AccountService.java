package Service;

import Model.Account;
import DAO.AccountDAO;

public class AccountService {
    AccountDAO accountDAO;

    public AccountService() {
        accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    public Account registerAccount(String username, String password) {
        return accountDAO.registerUser(username, password);
    }

    public Account loginAccount(Account account) {
        return accountDAO.checkCredentials(account);
    }

    public Account checkExists(String username) {
        return accountDAO.getAccountByUsername(username);
    }

    public Account checkByID(int id) {
        return accountDAO.checkAccountByID(id);
    }
}
