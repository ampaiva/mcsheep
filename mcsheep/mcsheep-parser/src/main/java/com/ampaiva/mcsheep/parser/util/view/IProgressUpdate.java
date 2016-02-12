package com.ampaiva.mcsheep.parser.util.view;

public interface IProgressUpdate {
    void beginIndex(Object... info);

    void endIndex(Object... info);
}
