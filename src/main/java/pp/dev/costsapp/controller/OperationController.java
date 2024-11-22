package pp.dev.costsapp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pp.dev.costsapp.model.dto.OperationSaveDto;
import pp.dev.costsapp.model.entity.Operation;
import pp.dev.costsapp.service.OperationService;

@RestController
@RequestMapping("/api/v1/operations")
@RequiredArgsConstructor
public class OperationController {

    private final OperationService operationService;

    @PostMapping
    public ResponseEntity<Long> saveOperation(OperationSaveDto newOperation) {
        Operation operation = operationService.save(newOperation);
        return ResponseEntity.ok().body(operation.getId());
    }

}
