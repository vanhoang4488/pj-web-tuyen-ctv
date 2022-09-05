package com.os.upload.util.word;

import com.os.upload.util.word.item.IWordNumber;
import com.os.util.StringUtils;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import java.math.BigInteger;
import java.util.Map;

public abstract class HandleWord {

    public static void handleWordNumber(StringBuilder sb,
                                        Map<BigInteger, IWordNumber> wordNumberMap,
                                        XWPFParagraph paragraph){
        String prefix = null;
        if (wordNumberMap != null
                && paragraph.getNumID() != null
                && paragraph.getNumFmt() != null){
            IWordNumber wn = wordNumberMap.get(paragraph.getNumID());
            if(wn != null){
                prefix = wn.nextNum();
            }
            else {
                IWordNumber newWordNumber = WordNumberFactory.getWordNumber(paragraph.getNumFmt(), paragraph.getNumLevelText());
                if(newWordNumber != null){
                    wordNumberMap.put(paragraph.getNumID(), newWordNumber);
                    prefix = newWordNumber.nextNum();
                }
            }
        }

        if(StringUtils.isNotBlank(prefix)) sb.append(prefix);
    }
}
