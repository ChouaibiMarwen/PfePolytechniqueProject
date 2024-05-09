package com.camelsoft.rayaserver.Response.Project;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class StatisticResponse {
    private Integer totalsupplier=0;
    private Integer totalusers=0;
    private Double totalrevenue = 0D;
    private Integer totalloanissued = 0;
    private Integer totalloaninprogress = 0;
    private Integer totalloandone = 0;
}
