package net.jjjshop.common.settings.vo.agent;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@ApiModel("申请协议")
public class LicenseVo implements Serializable {
    private static final long serialVersionUID = 1L;
    private String license;

    public LicenseVo() {
        this.license = "";
    }
}
