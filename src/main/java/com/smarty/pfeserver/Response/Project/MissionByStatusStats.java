package com.smarty.pfeserver.Response.Project;

import com.smarty.pfeserver.Enum.Project.MissionStatusEnum;
import com.smarty.pfeserver.Models.Project.Mission;

public class MissionByStatusStats {
    private MissionStatusEnum status;
    private Integer total;

    public MissionByStatusStats() {
    }

    public MissionByStatusStats(MissionStatusEnum status, Integer total) {
        this.status = status;
        this.total = total;
    }

    public MissionStatusEnum getStatus() {
        return status;
    }

    public void setStatus(MissionStatusEnum status) {
        this.status = status;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}
