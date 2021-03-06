package com.ampaiva.mcsheep.detection.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.ampaiva.mcsheep.detection.model.Call;
import com.ampaiva.mcsheep.detection.model.Method;
import com.ampaiva.mcsheep.detection.model.Repository;
import com.ampaiva.mcsheep.detection.model.Sequence;
import com.ampaiva.mcsheep.detection.model.Unit;
import com.ampaiva.mcsheep.detection.util.MatchesData;
import com.ampaiva.mcsheep.detection.util.SequenceMatch;
import com.ampaiva.mcsheep.detection.util.SequencesInt;
import com.ampaiva.mcsheep.detection.util.SequencesMap;
import com.ampaiva.mcsheep.parser.cm.ConcernCollection;
import com.ampaiva.mcsheep.parser.cm.ConcernMetricNode;
import com.ampaiva.mcsheep.parser.cm.ConcernMetricNodes;
import com.ampaiva.mcsheep.parser.cm.ICodeSource;
import com.ampaiva.mcsheep.parser.cm.IConcernMetric;
import com.ampaiva.mcsheep.parser.cm.IMethodCalls;
import com.ampaiva.mcsheep.parser.cm.IMetricsSource;
import com.ampaiva.mcsheep.parser.cm.MetricsColector;
import com.ampaiva.mcsheep.parser.util.SourceHandler;
import com.ampaiva.mcsheep.parser.util.view.IProgressUpdate;
import com.ampaiva.mcsheep.parser.util.view.ProgressUpdate;
import com.github.javaparser.ParseException;

public class MethodCallsManager {
    public static final String SEPARATOR = "#";
    private SequencesMap sequencesMap;
    private final SequencesInt sequencesInt;

    public MethodCallsManager(SequencesInt sequencesInt) {
        this.sequencesInt = sequencesInt;
    }

    public MethodCallsManager() {
        this(null);
    }

    private void persistConcernCollection(MetricsColector metricsColector, Map<String, String> codeSourceMap,
            List<IMethodCalls> methodCalls) throws ParseException, IOException {
        Map<String, List<IConcernMetric>> hash = metricsColector.getMetrics(codeSourceMap).getHash();
        for (Entry<String, List<IConcernMetric>> entry : hash.entrySet()) {
            for (IConcernMetric concernMetric : entry.getValue()) {
                if (concernMetric instanceof ConcernCollection) {
                    methodCalls.add((ConcernCollection) concernMetric);
                }
            }
        }
    }

    private List<IMethodCalls> getConcernCollectionofAllFiles(IMetricsSource metricsSource,
            List<Map<String, String>> codeSourcesMaps) throws ParseException, IOException {
        final List<IMethodCalls> methodCalls = new ArrayList<IMethodCalls>();
        IProgressUpdate update = ProgressUpdate.start("Processing code source", codeSourcesMaps.size());
        for (Map<String, String> codeSource : codeSourcesMaps) {
            update.beginIndex(codeSource);
            MetricsColector metricsColector = new MetricsColector(metricsSource);
            persistConcernCollection(metricsColector, codeSource, methodCalls);
        }

        return methodCalls;
    }

    public Repository createRepository(List<ICodeSource> codeSources, String location,
            Map<String, Sequence> sequencesMap) throws FileNotFoundException, IOException, ParseException {
        Repository repository = new Repository();
        repository.setLocation(location);
        List<Unit> units = new ArrayList<>();
        List<Map<String, String>> codeSourceMaps = new ArrayList<>();
        for (ICodeSource codeSource : codeSources) {
            Map<String, String> codeSourceMap = codeSource.getCodeSource();
            codeSourceMaps.add(codeSourceMap);
        }
        for (Map<String, String> codeSourceMap : codeSourceMaps) {
            IProgressUpdate update = ProgressUpdate.start("Parsing file", codeSourceMap.entrySet().size());
            for (Entry<String, String> entry : codeSourceMap.entrySet()) {
                update.beginIndex(entry.getKey());
                Unit unit = new Unit();
                unit.setRepositoryBean(repository);
                unit.setName(entry.getKey());
                unit.setSource(entry.getValue());
                units.add(unit);
                IConcernMetric concernMetric = new ConcernCollection();
                concernMetric.parse(unit.getSource());
                List<Method> methods = getMethods(unit, sequencesMap, (IMethodCalls) concernMetric);
                unit.setMethods(methods);
            }
        }
        repository.setUnits(units);
        repository.setAnalysis(new ArrayList<>());
        return repository;
    }

    public List<Method> getMethodCodes(Map<String, Sequence> sequencesMap, List<Map<String, String>> codeSourcesMaps)
            throws IOException, ParseException {
        IMetricsSource metricsSource = new IMetricsSource() {

            @Override
            public List<IConcernMetric> getConcernMetrics() {
                return Arrays.asList((IConcernMetric) new ConcernCollection());
            }
        };
        List<IMethodCalls> allMethodCalls = getConcernCollectionofAllFiles(metricsSource, codeSourcesMaps);
        return getMethods(sequencesMap, allMethodCalls);
    }

    private List<Method> getMethods(Map<String, Sequence> sequencesMap, List<IMethodCalls> allMethodCalls) {
        List<Method> methodCodes = new ArrayList<Method>();
        for (IMethodCalls methodCall : allMethodCalls) {
            int callPosition = 0;
            for (int i = 0; i < methodCall.getMethodNames().size(); i++) {
                Method method = getMethod(sequencesMap, methodCall, i, callPosition);
                methodCodes.add(method);
                callPosition += method.getCalls().size();
            }
        }
        return methodCodes;
    }

    private List<Method> getMethods(Unit unit, Map<String, Sequence> sequencesMap, IMethodCalls methodCall) {
        List<Method> methodCodes = new ArrayList<Method>();
        int callPosition = 0;
        for (int i = 0; i < methodCall.getMethodNames().size(); i++) {
            Method method = getMethod(sequencesMap, methodCall, i, callPosition);
            method.setUnitBean(unit);
            methodCodes.add(method);
            callPosition += method.getCalls().size();
        }
        return methodCodes;
    }

    private Method getMethod(Map<String, Sequence> sequencesMap, IMethodCalls methodCall, int i, int callPosition) {
        Method method = new Method();
        method.setName(methodCall.getMethodNames().get(i));
        method.setSource(methodCall.getMethodSources().get(i).getSource());
        method.setBeglin(methodCall.getMethodPositions().get(i).get(0));
        method.setBegcol(methodCall.getMethodPositions().get(i).get(1));
        method.setEndlin(methodCall.getMethodPositions().get(i).get(2));
        method.setEndcol(methodCall.getMethodPositions().get(i).get(3));

        method.setCalls(new ArrayList<Call>());
        ConcernMetricNodes nodes = ((ConcernCollection) methodCall).getNodes();
        List<String> seq = methodCall.getSequences().get(i);
        for (int order = 0; order < seq.size(); order++) {
            String sequenceName = seq.get(order);
            //            if (sequenceName.length() > 255) {
            //                continue;
            //            }
            Call call = new Call();
            call.setPosition(order);
            ConcernMetricNode concernMetricNode = nodes.get(callPosition + order);
            call.setBeglin(concernMetricNode.getBeginLine());
            call.setEndlin(concernMetricNode.getEndLine());
            call.setBegcol(concernMetricNode.getBeginColumn());
            call.setEndcol(concernMetricNode.getEndColumn());
            Sequence sequence = sequencesMap.get(sequenceName);
            if (sequence == null) {
                sequence = new Sequence();
                sequence.setName(sequenceName);
                sequencesMap.put(sequenceName, sequence);
            }
            call.setSequenceBean(sequence);
            call.setMethodBean(method);
            method.getCalls().add(call);
        }
        return method;
    }

    public List<MatchesData> getSequenceMatches() {
        SequenceMatch sequenceMatch = new SequenceMatch(sequencesInt.getSequencesInt());
        if (sequencesMap == null) {
            sequencesMap = new SequencesMap(sequencesInt.getSequencesInt());
        }
        return sequenceMatch.getMatches(sequencesMap.getMap());
    }

    public List<ConcernClone> getConcernClones(List<MatchesData> sequenceMatches, List<Unit> units) {
        List<Method> methodCodes = new ArrayList<>();
        for (Unit unit : units) {
            methodCodes.addAll(unit.getMethods());
        }
        List<ConcernClone> concernClones = new ArrayList<ConcernClone>();
        for (MatchesData matchesData : sequenceMatches) {
            for (int i = 0; i < matchesData.methodsMatched.size(); i++) {
                int matchedIndex = matchesData.methodsMatched.get(i);
                ConcernClone clone = new ConcernClone();
                clone.methods = Arrays.asList(methodCodes.get(matchesData.methodIndex).getName(),
                        methodCodes.get(matchedIndex).getName());
                clone.sources = Arrays.asList(new SourceHandler(methodCodes.get(matchesData.methodIndex).getSource()),
                        new SourceHandler(methodCodes.get(matchedIndex).getSource()));
                clone.sequences = Arrays.asList(
                        SequencesInt.callsToStringList(methodCodes.get(matchesData.methodIndex).getCalls()),
                        SequencesInt.callsToStringList(methodCodes.get(matchedIndex).getCalls()));
                clone.duplications = matchesData.callsMatched.get(i);
                concernClones.add(clone);
            }
        }
        return concernClones;
    }

    public ConcernClone getMCClone(IMethodCalls methodCallsA, IMethodCalls methodCallsB, int methodAIndex,
            int methodBIndex, int[] indexes) {
        ConcernClone clone = new ConcernClone();
        clone.methods = Arrays.asList(methodCallsA.getMethodNames().get(methodAIndex),
                methodCallsB.getMethodNames().get(methodBIndex));
        clone.sources = Arrays.asList(methodCallsA.getMethodSources().get(methodAIndex),
                methodCallsB.getMethodSources().get(methodBIndex));
        clone.sequences = Arrays.asList(methodCallsA.getSequences().get(methodAIndex),
                methodCallsB.getSequences().get(methodBIndex));
        clone.duplications = null; //indexes;
        return clone;
    }

}
