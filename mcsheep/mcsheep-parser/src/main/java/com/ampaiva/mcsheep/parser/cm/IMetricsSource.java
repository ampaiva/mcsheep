package com.ampaiva.mcsheep.parser.cm;

import java.util.List;

public interface IMetricsSource {

    public abstract List<IConcernMetric> getConcernMetrics();

}