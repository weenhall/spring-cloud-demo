package com.ween.cloud.handler;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.ween.cloud.domain.CommonResult;

public class CustomBlockHandler {

    public CommonResult handleException(BlockException e){
        return new CommonResult("自定义限流信息",200);
    }
}
