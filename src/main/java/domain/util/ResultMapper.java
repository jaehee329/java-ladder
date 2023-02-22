package domain.util;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import domain.end.End;
import domain.user.User;

public class ResultMapper {
	private static final String KEY_NAME = "all";
	private static final String RESULT_TARGETS_INVALID_ERROR_MSG = "중복되거나 존재하지 않는 결과를 출력할 수 없습니다.";

	public static Map<User, End> map(final List<User> users,
		final List<End> ends,
		final List<Integer> indicies) {

		Map<User, End> map = new LinkedHashMap<>();
		int size = users.size();
		for (int i = 0; i < size; i++) {
			map.put(users.get(i), ends.get(indicies.get(i)));
		}
		return map;
	}

	public static Map<User, End> toResult(final Map<User, End> results, final List<String> targetUserNames) {
		validateDuplication(targetUserNames);
		validatePresence(results.keySet(), targetUserNames);
		if (hasKeyName(targetUserNames)) {
			return results;
		}
		return selectTargets(results, targetUserNames);
	}

	private static void validateDuplication(final List<String> targetUserNames) {
		if (targetUserNames.stream().distinct().count() != targetUserNames.size()) {
			throw new IllegalArgumentException(RESULT_TARGETS_INVALID_ERROR_MSG);
		}
	}

	private static void validatePresence(final Set<User> users, final List<String> targetUserNames) {
		for (String target : targetUserNames) {
			throwIfNotInUsers(users, target);
		}
	}

	private static void throwIfNotInUsers(final Set<User> users, final String target) {
		if (!target.equals(KEY_NAME) && !users.contains(new User(target))) {
			throw new IllegalArgumentException(RESULT_TARGETS_INVALID_ERROR_MSG);
		}
	}

	private static boolean hasKeyName(final List<String> targetUserNames) {
		return targetUserNames.stream().anyMatch(name -> name.equals(KEY_NAME));
	}

	private static Map<User, End> selectTargets(final Map<User, End> results, final List<String> targetUserNames) {
		Map<User, End> selected = new LinkedHashMap<>();
		for (String name : targetUserNames) {
			User user = new User(name);
			selected.put(user, results.get(user));
		}
		return Collections.unmodifiableMap(selected);
	}
}
