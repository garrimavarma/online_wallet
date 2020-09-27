package com.cg.OnlineWallet.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cg.OnlineWallet.Entity.WalletUser;

@Repository
public interface WalletUserInterface extends JpaRepository<WalletUser, Integer> {

}
