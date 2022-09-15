package com.os.upload.service;

import com.os.entity.Article;
import com.os.result.ResultEntity;
import com.os.upload.util.ImageParser;
import com.os.upload.util.word.HandleWord;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class WordService {

    public ResultEntity<Article> createArticleByWordDoc(XWPFDocument document){
        ImageParser imageParser = new ImageParser("classpath:/img", "");
        // chuyển đổi toàn bộ nội dụng file Word thành một List<String>
        // mỗi dòng là 1 phần tử trong List.
        List<String> lines = convertWordDocToList(document, imageParser);

        // convert List<String> thành body trong entity Article.
        Article article = covertListToBody(lines);
        return ResultEntity.success().entity(article).build();
    }

    private List<String> convertWordDocToList(XWPFDocument document, ImageParser imageParser){
        List<String> lines = new ArrayList<>();
        List<IBodyElement> elements = document.getBodyElements();
        for (IBodyElement element : elements) {
            StringBuilder line = new StringBuilder();

            if (element.getElementType().equals(BodyElementType.PARAGRAPH))
                HandleWord.handleParagraph(line, element, imageParser);
            else if (element.getElementType().equals(BodyElementType.TABLE))
                HandleWord.handleTable(line, element, imageParser);

            // mỗi thẻ <p></p> là đã là 1 dòng rồi => không cần <br/>
            lines.add(line.toString());
        }
        return lines;
    }

    private Article covertListToBody(List<String> lines){
        Article article = new Article();
        StringBuilder content = new StringBuilder();
        for (String line : lines)
            content.append(line);
        article.setContent(content.toString());
        return article;
    }
}
