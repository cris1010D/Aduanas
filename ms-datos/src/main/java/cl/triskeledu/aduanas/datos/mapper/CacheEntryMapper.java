package cl.triskeledu.aduanas.datos.mapper;

import cl.triskeledu.aduanas.datos.dto.CacheEntryRequest;
import cl.triskeledu.aduanas.datos.dto.CacheEntryResponse;
import cl.triskeledu.aduanas.datos.model.CacheEntry;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface CacheEntryMapper {
    CacheEntryResponse toResponse(CacheEntry cacheEntry);
    List<CacheEntryResponse> toResponseList(List<CacheEntry> cacheEntries);
    @Mapping(target = "id", ignore = true)
    CacheEntry toEntity(CacheEntryRequest request);
    @Mapping(target = "id", ignore = true)
    void updateEntity(CacheEntryRequest request, @MappingTarget CacheEntry cacheEntry);
}
