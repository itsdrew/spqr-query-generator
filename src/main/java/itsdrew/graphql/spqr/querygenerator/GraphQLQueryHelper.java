package itsdrew.graphql.spqr.querygenerator;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

import static java.beans.Introspector.decapitalize;

public class GraphQLQueryHelper {

    public static String createGraphQLQuery(IGraphQLSerializable pojo, String operationName, Class serviceClass, Class returnObjectClass) {

        GraphQLMethodInfo info = getParameterNamesAndTypesFromMethod(operationName, serviceClass);

        LinkedHashMap<String, Class> queryParameters = info.getParamNamesAndTypes();
        String operationType = info.getMethodType();

        StringBuilder sb = new StringBuilder();
        sb.append(operationType).append("{").append(operationName);

        if (queryParameters!=null && !queryParameters.isEmpty() && pojo!=null) {

            sb.append("(");

            Iterator<Map.Entry<String, Class>> qpIterator = queryParameters.entrySet().iterator();

            while (qpIterator.hasNext()) {

                Map.Entry<String, Class> entry = qpIterator.next();

                sb.append(entry.getKey())
                        .append(": ")
                        .append(pojo.createGraphQLInput());

                if (qpIterator.hasNext()) {
                    sb.append(",");
                }
            }

            sb.append(")");
        }

        sb.append("{");

        List<String> allFields = getFieldNamesFromClass(returnObjectClass);

        Iterator<String> allFieldsIterator = allFields.iterator();
        while (allFieldsIterator.hasNext()) {
            sb.append(allFieldsIterator.next()).append(" ");
        }

        sb.append("}").append("}");

        return sb.toString();
    }

    private static List<String> getFieldNamesFromClass(Class clazz) {

        List<String> fieldNames = new ArrayList<>();

        Method[] declaredMethods = clazz.getDeclaredMethods();

        for (Method method : declaredMethods) {
            String methodName = method.getName();
            if (methodName.startsWith("get")) {
                String fieldName = methodName.replace("get", "");
                fieldName = decapitalize(fieldName);
                fieldNames.add(fieldName);
            }
        }

        return fieldNames;
    }


    private static GraphQLMethodInfo getParameterNamesAndTypesFromMethod(String operationName, Class clazz) {

        Method[] declaredMethods = clazz.getDeclaredMethods();

        GraphQLMethodInfo info = new GraphQLMethodInfo();

        for (Method method : declaredMethods) {
            if (method.getName().equals(operationName)) {

                info.setParamNamesAndTypes(getParameterNamesAndTypes(method));

                //Seems kind of inneficient, but doing it this way in case method
                //is annotated with anything other than @GraphQLQuery or @GraphQLMutation
                //Doing string compare beacuse I don't want to include SPQR as a dependency
                Arrays.stream(method.getDeclaredAnnotations()).forEach(annotation -> {
                    String annotationName = annotation.annotationType().getSimpleName();
                    if (annotationName.equals("GraphQLQuery")) {
                        info.setMethodType("query");
                    } else if (annotationName.equals("GraphQLMutation")) {
                        info.setMethodType("mutation");
                    }
                });

                break;
            }
        }

        return info;
    }

    private static LinkedHashMap<String, Class> getParameterNamesAndTypes(Method method) {
        Parameter[] parameters = method.getParameters();
        LinkedHashMap<String, Class> parameterNames = new LinkedHashMap<>();

        for (Parameter parameter : parameters) {
            if (!parameter.isNamePresent()) {
                throw new IllegalArgumentException("Parameter names are not present! Compile with -parameters");
            }

            parameterNames.put(parameter.getName(), parameter.getType());
        }

        return parameterNames;
    }


}
