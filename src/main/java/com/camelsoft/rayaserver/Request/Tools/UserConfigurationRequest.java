package com.camelsoft.rayaserver.Request.Tools;

import com.camelsoft.rayaserver.Enum.Tools.Language;
import lombok.Data;

import javax.persistence.Column;

@Data
public class UserConfigurationRequest {
    private Boolean notificationEmail;
    private Boolean notificationFcm;
    private Language language;
}
