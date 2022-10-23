package fit.fancyday.model.wemedia.dtos;

import fit.fancyday.model.common.dtos.PageRequestDto;
import lombok.Data;

/**
 * Whether the picture is collected or not
 *
 * @author 喵酱不熬夜
 * @date 2022/10/15
 */
@Data
public class WmMaterialDto extends PageRequestDto {

    /**
     * 1 collected
     * 0 not
     */
    private Short isCollection;
}
