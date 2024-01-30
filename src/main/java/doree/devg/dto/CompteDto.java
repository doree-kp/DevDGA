package doree.devg.dto;

import doree.devg.entity.TypeCompte;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompteDto {
    private String type;
    private Long idClient;
}
