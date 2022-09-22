package com.os.controller.syslog;

import com.os.entity.UserBlogNum;
import com.os.service.syslog.UserBlogNumBiz;
import com.os.util.xssf.ExcelExportUtil;
import lombok.RequiredArgsConstructor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@RestController
@RequestMapping("/sys/log")
@RequiredArgsConstructor
public class SysLogController {

    private final UserBlogNumBiz userBlogNumBiz;

    @RequestMapping("/downloadUserBlogExcel")
    public void downloadUserBlogExcel(HttpServletResponse response) throws IOException {
        List<String> captions = this.getUserBlogCaptionList();
        XSSFWorkbook workbook = ExcelExportUtil.genExcelSheet("thống kê", captions);

        // lấy ra danh sách UserBlogNum
        List<UserBlogNum> userBlogNumList = userBlogNumBiz.getAllUserBlogNum();

        // todo chuyển đổi các thuộc tính của đối tượng sang danh sách.
        int index = 1;
        userBlogNumList.stream().forEach(e -> {
            List<String> cellValue = this.userBlogNumToList(e);
            ExcelExportUtil.addDataCell(workbook.getSheet());
        });

        workbook.write(response.getOutputStream());
    }

    private List<String> getUserBlogCaptionList(){
        List<String> captions = new ArrayList<>();
        captions.add("Người viết");
        captions.add("Số lượng bài viết");
        captions.add("Tổng số lượt xem");
        captions.add("Bài viết được yêu thích nhất");
        captions.add("Key bài viết");
        captions.add("Lượt xem của bài viết được yêu thích nhất");
        return captions;
    }

    private List<String> userBlogNumToList(UserBlogNum userBlogNum){
        List<String> cellValues = new ArrayList<>();
        cellValues.add("" + userBlogNum.getUserId());
        cellValues.add("" + userBlogNum.getNum());
        cellValues.add("" + userBlogNum.getViews_interestest());
        cellValues.add(userBlogNum.getInterestest());
        cellValues.add(userBlogNum.getKey_interestest());
        cellValues.add("" + userBlogNum.getViews_interestest());
        return cellValues;
    }
}
