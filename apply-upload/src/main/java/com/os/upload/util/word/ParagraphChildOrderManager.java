package com.os.upload.util.word;

import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.xmlbeans.XmlCursor;
import org.openxmlformats.schemas.officeDocument.x2006.math.CTOMath;
import org.openxmlformats.schemas.officeDocument.x2006.math.CTOMathPara;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class ParagraphChildOrderManager {

    private static final int TYPE_RUN = 1;
    private static final int TYPE_OMATH = 2;

    private List<Integer> typeList = new ArrayList<>();
    XWPFParagraph paragraph;

    public ParagraphChildOrderManager(XWPFParagraph paragraph){
        this.paragraph = paragraph;
        List<CTOMathPara> ctoMathParaList = paragraph.getCTP().getOMathParaList();

        XmlCursor cursor = paragraph.getCTP().newCursor();
        while (cursor.hasNextToken()){
            XmlCursor.TokenType tokenType = cursor.toNextToken();
            if(tokenType.isStart()){
                // todo đoạn này cần bổ sung thêm
                if(cursor.getName().getPrefix().equalsIgnoreCase("w")
                        && cursor.getName().getLocalPart().equals("r"))
                    typeList.add(TYPE_RUN);
                else if (cursor.getName().getLocalPart().equalsIgnoreCase("oMath"))
                    typeList.add(TYPE_OMATH);
            }
            else if (tokenType.isEnd()){
                cursor.push();
                cursor.toParent();
                if (cursor.getName().getLocalPart().equalsIgnoreCase("p")) break;
                cursor.pop();
            }
        }
    }

    public List<Object> getChildList(){
        List<Object> runOrMathList = new ArrayList<>();
        List<XWPFRun> runs = paragraph.getRuns();
        List<CTOMath> ctoMathList = paragraph.getCTP().getOMathList();
        int totalRuns = runs.size() + ctoMathList.size();
        if(typeList.size() != totalRuns)
            throw new RuntimeException("so khớp sai số lượng XWPFRun và CTOMath");

        Queue<XWPFRun> runsQueue = new LinkedList<>(runs);
        Queue<CTOMath> mathQueue = new LinkedList<>(ctoMathList);
        for(int i = 0; i < typeList.size(); i++){
            Integer type = typeList.get(i);
            if(type.equals(TYPE_RUN) && runs.size() > 0)
                runOrMathList.add(runsQueue.poll());
            else if(type.equals(TYPE_OMATH) && mathQueue.size() > 0)
                runOrMathList.add(mathQueue.poll());
        }
        return runOrMathList;
    }
}
