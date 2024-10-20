package com.smarty.pfeserver.Request.Tools;

import com.smarty.pfeserver.Enum.Tools.Language;
import lombok.Data;

@Data
public class UserConfigurationRequest {
    private Boolean notificationEmail;
    private Boolean notificationFcm;
    private Language language;
}
