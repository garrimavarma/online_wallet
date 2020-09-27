package com.cg.OnlineWallet.Service;

import java.time.LocalDateTime;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cg.OnlineWallet.Entity.AccountTransactions;
import com.cg.OnlineWallet.Entity.WalletAccount;
import com.cg.OnlineWallet.Entity.WalletUser;
import com.cg.OnlineWallet.Repository.AccountTransactionsInterface;
import com.cg.OnlineWallet.Repository.WalletAccountInterface;
import com.cg.OnlineWallet.Repository.WalletUserInterface;

@Service
@Transactional
public class ServiceClass {
	@Autowired
	AccountTransactionsInterface transactionsDao;
	@Autowired
	WalletAccountInterface walletAccountDao;
	@Autowired
	WalletUserInterface walletUserDao;


	public WalletUser addUser(WalletUser user) {
		return walletUserDao.save(user);
	}

	
	public WalletUser userLogin(int userId, String password) {
		if (walletUserDao.existsById(userId)) {
			WalletUser user = walletUserDao.getOne(userId);
			String pass = user.getPassword();
			if (pass.equals(password)) {
				return user;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	
	public WalletAccount addAccount(int userId, WalletAccount account) {
		walletAccountDao.save(account);
		WalletUser user = walletUserDao.getOne(userId);
		account.setWalletUser(user);
		user.setWalletAccount(account);
		walletUserDao.save(user);
		return walletAccountDao.save(account);
	}

	
	public WalletAccount addMoney(WalletAccount walletAccount) {
		int accountId = walletAccount.getAccountId();
		if (walletAccountDao.existsById(accountId)) {
			WalletAccount account = walletAccountDao.getOne(accountId);
			double previousBalance = account.getAccountBalance();
			account.setAccountBalance(walletAccount.getAccountBalance() + previousBalance);
			return walletAccountDao.save(account);
		} else {
			return null;
		}
	}

	
	public double retriveBalance(int accountId) {
		WalletAccount account = walletAccountDao.getOne(accountId);
		return account.getAccountBalance();
	}

	
	public String transferFunds(int senderAccountId, int receiverAccountId, double amount) {
		if (walletAccountDao.existsById(receiverAccountId)) {
			WalletAccount senderAccount = walletAccountDao.getOne(senderAccountId);
			if (senderAccount.getAccountBalance() > amount) {
				
				double money = senderAccount.getAccountBalance() - amount;
				senderAccount.setAccountBalance(money);
				walletAccountDao.save(senderAccount);
				
				WalletAccount receiverAccount = walletAccountDao.getOne(receiverAccountId);
				double money1 = receiverAccount.getAccountBalance() + amount;
				receiverAccount.setAccountBalance(money1);
				walletAccountDao.save(receiverAccount);
				
				LocalDateTime now = LocalDateTime.now();
				AccountTransactions senderTransaction = new AccountTransactions();
				senderTransaction.setAccountBalance(senderAccount.getAccountBalance());
				senderTransaction.setAmount(amount);
				senderTransaction.setDateOfTransaction(now);
				senderTransaction.setDiscription("Amount debited");
				senderTransaction.setWalletAccount(senderAccount);
				transactionsDao.save(senderTransaction);
				senderAccount.getWalletTransactions().add(senderTransaction);
				walletAccountDao.save(senderAccount);
				
				AccountTransactions receiverTransaction = new AccountTransactions();
				receiverTransaction.setAccountBalance(receiverAccount.getAccountBalance());
				receiverTransaction.setAmount(amount);
				receiverTransaction.setDateOfTransaction(now);
				receiverTransaction.setDiscription("Amount credited");
				receiverTransaction.setWalletAccount(receiverAccount);
				transactionsDao.save(receiverTransaction);
				receiverAccount.getWalletTransactions().add(receiverTransaction);
				walletAccountDao.save(receiverAccount);
				return "SuccessFully Transfered";
			} else {
				return "Unsuccessful Transfer";
			}
		} else {
			return "Receiver AccountId does not exist";
		}
	}


	public Set<AccountTransactions> transactionDetails(int accountId) {
		if (walletAccountDao.existsById(accountId)) {
			WalletAccount account = walletAccountDao.getOne(accountId);
			return account.getWalletTransactions();
		} else {
			return null;
		}
	}

}
