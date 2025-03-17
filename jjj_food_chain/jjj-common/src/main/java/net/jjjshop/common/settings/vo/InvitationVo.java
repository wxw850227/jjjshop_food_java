package net.jjjshop.common.settings.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@ApiModel("邀请有礼Vo")
public class InvitationVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer isOpen;
    private String image;
    private Integer invitationId;

    public InvitationVo() {
        this.isOpen = 0;
        this.image = "";
        this.invitationId = 0;
    }
}
