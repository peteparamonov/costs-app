package pp.dev.costsapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pp.dev.costsapp.model.entity.Operation;
import pp.dev.costsapp.model.entity.WalletInfo;

import java.util.List;

@Repository
public interface WalletRepository extends JpaRepository<WalletInfo, Long>{

    WalletInfo findByName(String name);
    void deleteAllByChatId(Long chatId);
    List<WalletInfo> findAllByChatId(Long chatId);
}
