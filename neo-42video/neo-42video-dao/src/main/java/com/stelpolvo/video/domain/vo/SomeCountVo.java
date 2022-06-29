package com.stelpolvo.video.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "点赞|收藏|投币信息展示实体")
public class SomeCountVo {
    @ApiModelProperty(value = "点赞|收藏|投币数量")
    private Long count;
    @ApiModelProperty(value = "是否已点赞|收藏|投币")
    private boolean like;
}
