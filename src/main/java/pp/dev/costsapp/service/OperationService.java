package pp.dev.costsapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pp.dev.costsapp.mapper.OperationMapper;
import pp.dev.costsapp.model.dto.OperationSaveDto;
import pp.dev.costsapp.model.entity.Operation;
import pp.dev.costsapp.repository.OperationRepository;

@RequiredArgsConstructor
@Service
public class OperationService {
    private final OperationMapper operationMapper;
    private final OperationRepository  operationRepository;


    public Operation save(OperationSaveDto operationDto) {
        Operation newOperation = operationMapper.mapFromSaveDto(operationDto);
        return operationRepository.save(newOperation);
    }

    public void deleteAllByChatId(String walletId){
        operationRepository.deleteAllByWalletId(Long.parseLong(walletId));
    }

}
