# GraphQL SPQR Query Generator

If you create a graphql back-end using this:
https://github.com/leangen/graphql-spqr And want to automatically generate client side queries from java beans, this is for you!

##### Examples speak louder than words so clone this repo and run src/main/test/java/example/ExampleMain to see it in action.

Here's the general idea (using lombok to generate getters and setters https://projectlombok.org):
```
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExampleBean implements IGraphQLSerializable {

    private String id;
    private Integer someInteger;
    private Map<String, String> someMap;
    private ExampleBean someExampleBean;
}

```

```
//A service using GraphQL SPQR annotations
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
```

```
//Usage

        ExampleBean exampleBean = new ExampleBean(...);

        String mutationString = createGraphQLQuery(exampleBean, "doMutation", ExampleBeanService.class, ExampleBean.class);
        
        /*
        *
        * mutation  {
        *  doMutation(exampleBean: {id: "Enterprise", someInteger: 1701, someMap: {commander: "Riker", captain: "Picard"}, someExampleBean: {id: "Mekong", someInteger: 72617, someMap: {captain: "Sisko"}, someExampleBean: {id: "Rubicon", someInteger: 72936, someMap: null, someExampleBean: null}}}) {
        *    id
        *    someInteger
        *    someMap
        *    someExampleBean
        *  }
        * }
        *
        */
        
        
        String queryString = createGraphQLQuery(enterprise, "doQuery", ExampleBeanService.class, ExampleBean.class);

        /*
        *
        * query {
        *  doQuery(exampleBean: {id: "Enterprise", someInteger: 1701, someMap: {commander: "Riker", captain: "Picard"}, someExampleBean: {id: "Mekong", someInteger: 72617, someMap: {captain: "Sisko"}, someExampleBean: {id: "Rubicon", someInteger: 72936, someMap: null, someExampleBean: null}}}) {
        *    id
        *    someInteger
        *    someMap
        *    someExampleBean
        *  }
        * }
        *
        */


```




## Instructions
- This project is not in maven central. If you want to use it, copy the 3 java classes in the main package. There are no dependencies.
- Implement IGraphQLSerializable on a bean. (Make sure it has getters and setters for fields)
- Use that bean as an input to a SPQR annotated service class.
- Call the method: createGraphQLQuery(\<Bean\>, \<Operation name\>, \<Service class\>, \<Return class\>);
