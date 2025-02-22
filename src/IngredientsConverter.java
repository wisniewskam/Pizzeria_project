import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Arrays;
import java.util.List;

@Converter
public class IngredientsConverter implements AttributeConverter<List<String>, String> {

    private static final String DELIMITER = ", ";

    @Override
    public String convertToDatabaseColumn(List<String> ingredients) {
        return (ingredients != null && !ingredients.isEmpty())
                ? String.join(DELIMITER, ingredients)
                : "";
    }

    @Override
    public List<String> convertToEntityAttribute(String dbData) {
        return (dbData != null && !dbData.isEmpty())
                ? Arrays.asList(dbData.split(DELIMITER))
                : null;
    }
}
