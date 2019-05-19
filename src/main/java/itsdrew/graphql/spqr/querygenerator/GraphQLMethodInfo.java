package itsdrew.graphql.spqr.querygenerator;

import java.util.LinkedHashMap;


public class GraphQLMethodInfo {

    private LinkedHashMap<String, Class> paramNamesAndTypes;
    private String methodType;

    public void setParamNamesAndTypes(LinkedHashMap<String, Class> paramNamesAndTypes) {
        this.paramNamesAndTypes = paramNamesAndTypes;
    }
    public LinkedHashMap<String, Class> getParamNamesAndTypes() {
        return this.paramNamesAndTypes;
    }

    public void setMethodType(String methodType) {
        this.methodType = methodType;
    }
    public String getMethodType() {
        return this.methodType;
    }


}
