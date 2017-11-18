package product.mapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

@Component
public class FilterMapMapper {

    private static final ObjectMapper jsonMapper = new ObjectMapper();

    public Map<String, List<String>> getValue(String filterJsonStr) {
        final Map<String, List<String>> filterMap = new HashMap<>();
        try{
            final JsonNode filters = jsonMapper.readTree(filterJsonStr);
            final Iterator<String> fieldNameIterator = filters.fieldNames();
            while(fieldNameIterator.hasNext()){
                final String filedName = fieldNameIterator.next().toLowerCase();
                final List<String> fieldValues = new ArrayList<>();
                final Iterator<JsonNode> fieldValueIterator = filters.get(filedName).elements();
                while(fieldValueIterator.hasNext()){
                    fieldValues.add(fieldValueIterator.next().asText().toLowerCase());
                }
                filterMap.put(filedName, fieldValues);
            }
        } catch (IOException exception){

        }
        return filterMap;
    }

}
