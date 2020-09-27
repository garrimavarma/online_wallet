package com.cg.OnlineWallet.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cg.OnlineWallet.Entity.AccountTransactions;

@Repository
public interface AccountTransactionsInterface extends JpaRepository<AccountTransactions, Integer> {

}
