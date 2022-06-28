package com.stelpolvo.video.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("分页实体")
public class Page<T> {
    @ApiModelProperty("分页数据")
    private List<T> list;
    @ApiModelProperty("总条数")
    private Integer total;
    @ApiModelProperty("当前页")
    private Integer page;
    @ApiModelProperty("总页数")
    private Integer pageSize;

    public Page(Integer page, Integer pageSize) {
        this.page = page;
        this.pageSize = pageSize;
    }

    public Integer getPage() {
        return (page - 1) * pageSize;
    }
}
