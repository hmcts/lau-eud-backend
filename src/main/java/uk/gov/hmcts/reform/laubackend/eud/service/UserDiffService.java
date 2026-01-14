package uk.gov.hmcts.reform.laubackend.eud.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.laubackend.eud.dto.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

@Service
@RequiredArgsConstructor
public class UserDiffService {

    @Value("${lau.idam.ignore-changes-in-fields}")
    private Set<String> ignoreChangesInFields;

    private final ObjectMapper objectMapper;

    public record FieldChange(String fieldName, String previousValue, String currentValue) {}

    public List<FieldChange> diffUsers(User previousUser, User currentUser) {
        JsonNode prev = objectMapper.valueToTree(previousUser);
        JsonNode curr = objectMapper.valueToTree(currentUser);

        Set<String> keys = new TreeSet<>();
        prev.fieldNames().forEachRemaining(keys::add);
        curr.fieldNames().forEachRemaining(keys::add);

        List<FieldChange> changes = new ArrayList<>();

        for (String key: keys) {
            if (ignoreChangesInFields.contains(key)) {
                continue;
            }
            String prevValue = valueAsString(key, prev.get(key));
            String currValue = valueAsString(key, curr.get(key));

            if (!Objects.equals(prevValue, currValue)) {
                changes.add(new FieldChange(key, prevValue, currValue));
            }
        }
        return changes;
    }

    private String valueAsString(String fieldName, JsonNode node) {
        if (node.isValueNode()) {
            return node.asText();
        }
        if ("roleNames".equals(fieldName) && node.isArray()) {
            List<String> roles = new ArrayList<>();
            node.forEach(item -> roles.add(item.asText()));
            roles.sort(String::compareTo);
            return String.join(",", roles);
        }

        return node.toString();
    }

}
