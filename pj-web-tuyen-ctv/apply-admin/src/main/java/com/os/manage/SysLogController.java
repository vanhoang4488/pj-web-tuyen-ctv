package com.os.manage;

import com.os.manage.syslog.SysLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/sys/log")
@RequiredArgsConstructor
public class SysLogController {

    private final SysLogService sysLogService;

    @RequestMapping("/downloadUserBlogExcel")
    public void downloadUserBlogExcel(HttpServletResponse response) {
        sysLogService.downloadUserBlogExcel();
    }
}
