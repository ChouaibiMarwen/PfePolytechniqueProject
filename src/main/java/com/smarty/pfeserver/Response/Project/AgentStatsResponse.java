package com.smarty.pfeserver.Response.Project;

import lombok.Data;

@Data
public class AgentStatsResponse {
    private Long totalAgent=0L;
    private Long paidnvoice=0L;
    private Long unpaidinvoice = 0L;
    private Long totallinvoice = 0L;
    private Long totallinvoiceinprogress = 0L;
}
