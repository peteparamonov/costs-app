package pp.dev.costsapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pp.dev.costsapp.model.entity.Operation;

@Repository
public interface OperationRepository extends JpaRepository<Operation, Long>{
    void deleteAllByWalletId(long walletId);
}
