package example;

import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLQuery;

import java.util.HashMap;
import java.util.Map;

public class ExampleBeanService {

    private Map<String, ExampleBean> testBeanRepository = new HashMap<>();

    @GraphQLMutation
    public ExampleBean doMutation(ExampleBean exampleBean) {
        testBeanRepository.put(exampleBean.getId(), exampleBean);
        return exampleBean;
    }

    @GraphQLQuery
    public ExampleBean doQuery(ExampleBean exampleBean) {
        return testBeanRepository.get(exampleBean.getId());
    }


}
