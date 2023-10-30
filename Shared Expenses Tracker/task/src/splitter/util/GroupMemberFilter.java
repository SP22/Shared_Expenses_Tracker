package splitter.util;

import splitter.GroupHolder;
import splitter.model.User;

import java.util.*;
import java.util.function.Consumer;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GroupMemberFilter {
    public static final Pattern PLUS_PATTERN = Pattern.compile("(?:(?<=\\+)|(?<![-\\w]))[A-Z]+(?!\\w)", Pattern.CASE_INSENSITIVE);
    public static final Pattern MINUS_PATTERN = Pattern.compile("(?:(?<=-))[A-Z]+(?!\\w)", Pattern.CASE_INSENSITIVE);
    public static final Pattern GROUP_PATTERN = Pattern.compile("[A-Z]+\\b");

    public static Set<String> filter(String members) {
        Set<String> filteredNames = new HashSet<>();
        process(members, PLUS_PATTERN, filteredNames::addAll);
        process(members, MINUS_PATTERN, filteredNames::removeAll);
        return filteredNames;
    }

    private static void process(String members, Pattern PATTERN, Consumer<List<String>> action) {
        Stream<MatchResult> stream = PATTERN.matcher(members).results();
        Map<Boolean, List<String>> map = stream
                .map(MatchResult::group)
                .collect(Collectors.partitioningBy(it -> GROUP_PATTERN.matcher(it).matches()));

        List<String> namesFromGroup = map.getOrDefault(true, Collections.emptyList())
                .stream()
                .flatMap(it -> GroupHolder.getInstance().getGroup(it).stream())
                .map(User::getName)
                .toList();

        List<String> names = map.getOrDefault(false, Collections.emptyList());

        List<String> finalNames = new ArrayList<>();
        finalNames.addAll(names);
        finalNames.addAll(namesFromGroup);

        action.accept(finalNames);
    }
}
