package example;

import itsdrew.graphql.spqr.querygenerator.IGraphQLSerializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExampleBean implements IGraphQLSerializable {

    /**
     * These beans should be considered data-transfer objects.
     * Fields will be graphql serialized only if they have a getter.
     * Don't create a 'get' method for something that doesn't have an associated field.
     */

    private String id;
    private Integer someInteger;
    private Map<String, String> someMap;
    private ExampleBean someExampleBean;
}
