package org.aptech.metube.videoservice.controller.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetVideoByCategoryIdRequest {
    private Long categoryId;
    private Long videoId;
}
