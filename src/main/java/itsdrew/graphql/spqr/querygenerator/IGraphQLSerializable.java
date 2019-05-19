package itsdrew.graphql.spqr.querygenerator;


import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Map;

import static java.beans.Introspector.decapitalize;

public interface IGraphQLSerializable {

    default String createGraphQLInput() {

        StringBuilder sb = new StringBuilder();
        sb.append("{");

        Arrays.stream(this.getClass().getDeclaredMethods())
                .filter(method -> method.getName().startsWith("get"))
                .forEach(method -> {

                    sb.append(decapitalize(method.getName().replace("get", "")));

                    sb.append(": ");

                    try {

                        Object result = method.invoke(this);

                        if (result == null) {

                            sb.append("null");

                        } else if (String.class.isAssignableFrom(method.getReturnType())) {

                            sb.append("\"");

                            //escape special chars before sending the string
                            sb.append(((String) result)
                                    .replace("\\", "\\\\")
                                    .replace("\n", "\\n")
                                    .replace("\r", "\\r")
                                    .replace("\"", "\\\"")
                            );

                            sb.append("\"");

                        } else if (IGraphQLSerializable.class.isAssignableFrom(method.getReturnType())) {

                            sb.append(((IGraphQLSerializable) result).createGraphQLInput());

                        } else if (Map.class.isAssignableFrom(method.getReturnType())) {

                            sb.append("{");

                            Map<String, String> map = (Map<String, String>) result;
                            for (Map.Entry e : map.entrySet()) {
                                sb.append(" ")
                                        .append(e.getKey())
                                        .append(": ")
                                        .append("\"")
                                        .append(e.getValue())
                                        .append("\"")
                                        .append(",");
                            }

                            //delete the last comma
                            sb.deleteCharAt(sb.length() - 1);

                            sb.append("}");

                        } else {
                            sb.append(result.toString());
                        }

                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }

                    sb.append(", ");

                });

        //delete the last comma and space
        sb.setLength(sb.length() - 2);

        sb.append("}");

        return sb.toString();
    }
}
