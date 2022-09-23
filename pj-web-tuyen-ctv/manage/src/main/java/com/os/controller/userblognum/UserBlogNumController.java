package com.os.controller.userblognum;

import com.os.config.BeanUtils;
import com.os.entity.UserBlogNum;
import com.os.i18n.I18nFilter;
import com.os.service.syslog.UserBlogNumBiz;
import com.os.util.xssf.ExcelExportUtil;
import lombok.RequiredArgsConstructor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/sys/log")
@RequiredArgsConstructor
public class UserBlogNumController {

    private final UserBlogNumBiz userBlogNumBiz;

    @RequestMapping("/downloadUserBlogExcel")
    public void downloadUserBlogExcel(HttpServletResponse response) throws IOException {
        // cài đặt mã hóa ký tự utf-8
        response.setCharacterEncoding("utf-8");

        // lấy ra danh sách UserBlogNum
        List<UserBlogNum> userBlogNumList = userBlogNumBiz.getAllUserBlogNum();

        // chuyển đổi danh sách đối tượng sang danh sách chuỗi String
        List<String[]> dataList = this.convertDataList(userBlogNumList);

        // chèn header vào đầu danh sách giá trị.
        I18nFilter i18nFilter = BeanUtils.getBean(I18nFilter.class);
        String sheetName = i18nFilter.getMessage("admin.export.excel.sheetName");
        String[] captions = this.getUserBlogCaptionList(i18nFilter);
        dataList.add(0, captions);

        // tạo file excel, sheet và tiêu đề của bảng
        XSSFWorkbook workbook = ExcelExportUtil.genericExcel(sheetName, dataList);

        workbook.write(response.getOutputStream());
    }

    private String[] getUserBlogCaptionList(I18nFilter i18n){
        String userId = i18n.getMessage("admin.userblognum.export.excel.userId");
        String userName = i18n.getMessage("admin.userblognum.export.excel.userName");
        String num = i18n.getMessage("admin.userblognum.export.excel.num");
        String total_view = i18n.getMessage("admin.userblognum.export.excel.totalViews");
        String interestest = i18n.getMessage("admin.userblognum.export.excel.interestest");
        String key_interestest = i18n.getMessage("admin.userblognum.export.excel.keyInterestest");
        String views_interestest = i18n.getMessage("admin.userblognum.export.excel.viewsInterestest");
        String[] headerNames = new String[] {userId, userName, num, total_view, interestest, key_interestest, views_interestest};
        return headerNames;
    }

    private List<String[]> convertDataList(List<UserBlogNum> userBlogNumList){
        List<String[]> dataList = new ArrayList<>();
        for(UserBlogNum userBlogNum : userBlogNumList){
            String[] stringList = this.convertStringArr(userBlogNum);
            dataList.add(stringList);
        }
        return dataList;
    }

    private String[] convertStringArr(UserBlogNum userBlogNum){
        String userId = "" + userBlogNum.getUserId();
        String userName = userBlogNum.getUserName();
        String num = "" + userBlogNum.getNum();
        String total_views = "" + userBlogNum.getTotal_views();
        String interestest = userBlogNum.getInterestest();
        String key_interestest = userBlogNum.getKey_interestest();
        String views_interestest = "" + userBlogNum.getViews_interestest();
        return new String[] {userId, userName, num, total_views, interestest, key_interestest, views_interestest};
    }
}
