package com.os.upload.util.word;

import com.os.upload.util.word.item.DecimalNumber;
import com.os.upload.util.word.item.IWordNumber;
import com.os.upload.util.word.item.UpperLetterNumber;

public abstract class WordNumberFactory {

    public static IWordNumber getWordNumber(String numFmt, String numLevelText){
        if("decimal".equals(numFmt)){
            return new DecimalNumber(numLevelText);
        }
        else if("upperLetter".equals(numFmt)){
            return new UpperLetterNumber(numLevelText);
        }
        return null;
    }
}

