package com.mycompany.sales_system.factory;

public enum StateFactory {

    AC("ACRE"), 
    AL("AL"), 
    AP("AP"), AM("AM"), BA("BA"), CE("CE"), DF("DF"), ES("ES"), GO("GO"), MA("MA"), MT("MT"), MS("MS"), MG("MG"), PA("PA"), PB("PB"), PR("PR"), PE("PE"), PI("PI"), RJ("RJ"), RN("RN"), RS("RS"), RO("RO"), RR("RR"), SC("SC"), SP("SP"), SE("SE"), TO("TO");

    private final String description;
    
    StateFactory(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}
