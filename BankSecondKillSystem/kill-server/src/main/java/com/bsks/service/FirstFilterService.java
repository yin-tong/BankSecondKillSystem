package com.bsks.service;

import com.bsks.api.result.Result;
import com.bsks.service.impl.FirstFilterFallbackService;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Service
@FeignClient(value = "first-filter-server",fallback = FirstFilterFallbackService.class)
public interface FirstFilterService {

    @PostMapping("/service/isRejected")
    Result isRejected(@RequestParam("identityId") String identityId,@RequestParam("age") int age);
}
