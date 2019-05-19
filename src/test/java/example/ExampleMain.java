package example;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static itsdrew.graphql.spqr.querygenerator.GraphQLQueryHelper.createGraphQLQuery;

public class ExampleMain {


    public static void main(String[] args) {

        /**
         * Generate 3 ExampleBeans
         * Nest them: Enterprise contains mekong which contains rubicon
         */

        ExampleBean rubicon = ExampleBean.builder()
                .id("Rubicon")
                .someInteger(72936)
                .build();

        ExampleBean mekong = ExampleBean.builder()
                .id("Mekong")
                .someInteger(72617)
                .someMap(Collections.singletonMap("captain", "Sisko"))
                .someExampleBean(rubicon)
                .build();


        Map<String, String> enterpriseMap = new HashMap<>();
        enterpriseMap.put("captain", "Picard");
        enterpriseMap.put("commander", "Riker");

        ExampleBean enterprise = ExampleBean.builder()
                .id("Enterprise")
                .someInteger(1701)
                .someMap(enterpriseMap)
                .someExampleBean(mekong)
                .build();

        String mutationString = createGraphQLQuery(enterprise, "doMutation", ExampleBeanService.class, ExampleBean.class);

        System.out.println("/********/\n");
        System.out.println(mutationString);
        System.out.println("\n/********/\n");

        String queryString = createGraphQLQuery(enterprise, "doQuery", ExampleBeanService.class, ExampleBean.class);

        System.out.println("/********/\n");
        System.out.println(queryString);
        System.out.println("\n/********/\n");

    }

}
