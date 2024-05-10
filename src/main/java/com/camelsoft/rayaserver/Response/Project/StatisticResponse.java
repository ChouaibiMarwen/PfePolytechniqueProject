package com.camelsoft.rayaserver.Response.Project;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class StatisticResponse {
    private Long totalsupplier=0L;
    private Long totalusers=0L;
    private Double totalrevenue = 0D;
    private Long totalloanissued = 0L;
    private Long totalloaninprogress = 0L;
    private Long totalloandone = 0L;

}
