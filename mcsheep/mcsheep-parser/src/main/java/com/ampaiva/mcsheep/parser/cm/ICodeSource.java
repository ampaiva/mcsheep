package com.ampaiva.mcsheep.parser.cm;

import java.io.IOException;
import java.util.Map;

public interface ICodeSource {

    public abstract Map<String, String> getCodeSource() throws IOException;

}