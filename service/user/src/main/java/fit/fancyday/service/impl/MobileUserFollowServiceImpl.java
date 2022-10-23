package fit.fancyday.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import fit.fancyday.mapper.MobileUserFollowMapper;
import fit.fancyday.service.MobileUserFollowService;
import fit.fancyday.model.user.pojos.MobileUserFollow;
import org.springframework.stereotype.Service;

@Service
public class MobileUserFollowServiceImpl extends ServiceImpl<MobileUserFollowMapper, MobileUserFollow>
    implements MobileUserFollowService {
}




