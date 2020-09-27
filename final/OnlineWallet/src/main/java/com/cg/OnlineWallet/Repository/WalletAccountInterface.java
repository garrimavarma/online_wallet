package com.cg.OnlineWallet.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cg.OnlineWallet.Entity.WalletAccount;

@Repository
public interface WalletAccountInterface extends JpaRepository<WalletAccount, Integer> {

}
