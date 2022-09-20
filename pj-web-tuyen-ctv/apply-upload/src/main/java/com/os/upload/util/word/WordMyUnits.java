package com.os.upload.util.word;

public class WordMyUnits {

    public static final int EMU_PER_PX = 9525;
    public static final int emuToPx(double emu){
        return (int) (emu/EMU_PER_PX);
    }
}
