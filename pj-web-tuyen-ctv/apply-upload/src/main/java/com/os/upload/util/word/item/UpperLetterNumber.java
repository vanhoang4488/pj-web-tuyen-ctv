package com.os.upload.util.word.item;

public class UpperLetterNumber extends BaseNumber implements IWordNumber {

    char number = 'A';

    public UpperLetterNumber(String splitChar) {
        super(splitChar);
    }

    @Override
    public String nextNum() {
        String value = number + this.splitChar;
        number++;
        return value;
    }
}
