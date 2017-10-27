package com.h3xstream.findsecbugs.graph.model;

import org.neo4j.graphdb.Label;

public class GraphLabels {

    public static final Label LABEL_FUNCTION  = Label.label("Function");
    public static final Label LABEL_CLASS     = Label.label("Class");
    public static final Label LABEL_INTERFACE = Label.label("Interface");
    public static final Label LABEL_VARIABLE  = Label.label("Variable");

}
