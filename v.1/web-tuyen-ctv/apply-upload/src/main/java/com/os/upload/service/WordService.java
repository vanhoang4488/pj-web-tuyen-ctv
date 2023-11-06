package com.os.upload.service;

import com.os.entity.Article;
import com.os.result.ResultEntity;
import com.os.upload.util.word.HandleWord;
import com.os.upload.util.word.ParagraphChildOrderManager;
import com.os.upload.util.word.WordNumberFactory;
import com.os.upload.util.word.item.IWordNumber;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.IBodyElement;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.openxmlformats.schemas.officeDocument.x2006.math.CTOMath;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class WordService {

    public ResultEntity<Article> createArticleByWordDoc(MultipartFile multipartFile){
        Article article = new Article();
        Map<String, byte[]> impMap = new HashMap<>();

        return ResultEntity.success().entity(article).build();
    }

}
