package com.ampaiva.mcsheep.detection.tools.pmd;

import java.util.List;

public class PmdClone {
    public int lines;
    public int tokens;
    public List<PmdOccurrence> ocurrencies;

    @Override
    public String toString() {
        return "PmdClone [lines=" + lines + ", tokens=" + tokens + "]";
    }
}