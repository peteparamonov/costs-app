package pp.dev.costsapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pp.dev.costsapp.model.entity.WalletInfo;
import pp.dev.costsapp.repository.WalletRepository;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
@Service
public class WalletService {
    private final WalletRepository walletRepository;

    public WalletInfo create(String name){
        return create(name, BigDecimal.ZERO);
    }

    public WalletInfo create(String name, BigDecimal amount){
        WalletInfo walletInfo = new WalletInfo();
        walletInfo.setName(name);
        walletInfo.setAmount(amount);
        return walletRepository.save(walletInfo);
    }

    public WalletInfo get(String walletName) {
        return walletRepository.findByName(walletName);
    }

    public List<WalletInfo> getAll(){
        return walletRepository.findAll();
    }

    public void deleteAllByChatId(String chatId){
        walletRepository.deleteAllByChatId(Long.parseLong(chatId));
    }

    public List<WalletInfo> findAllByChatId(String chatId){
       return walletRepository.findAllByChatId(Long.parseLong(chatId));
    }
}
