package com.ampaiva.mcsheep.parser.cm;

import java.util.List;

import com.ampaiva.mcsheep.parser.util.SourceHandler;

public interface IMethodCalls {

    public abstract List<SourceHandler> getMethodSources();

    public abstract List<String> getMethodNames();

    public abstract List<List<Integer>> getMethodPositions();

    public abstract List<List<String>> getSequences();

}