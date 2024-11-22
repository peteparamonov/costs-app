package pp.dev.costsapp.mapper;

import org.mapstruct.Mapper;
import pp.dev.costsapp.model.dto.OperationSaveDto;
import pp.dev.costsapp.model.entity.Operation;

@Mapper(componentModel = "spring")
public interface OperationMapper {

    Operation mapFromSaveDto(OperationSaveDto operationSaveDto);
}
