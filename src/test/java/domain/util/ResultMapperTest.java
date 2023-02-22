package domain.util;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import domain.end.End;
import domain.end.Ends;
import domain.user.User;
import domain.user.Users;

class ResultMapperTest {

	@Test
	@DisplayName("원하는 인덱스로 user-end쌍이 매핑되어야 한다.")
	void indexMatchTest() {
		List<User> users = new Users(List.of("a", "b", "c", "d")).getUsers();
		List<End> ends = new Ends(List.of("가", "나", "다", "라"), 4).getEnds();
		List<Integer> changeIdx = List.of(1, 0, 3, 2);

		Map<User, End> expected = new LinkedHashMap<>();
		int size = users.size();
		for (int i = 0; i < size; i++) {
			expected.put(users.get(i), ends.get(changeIdx.get(i)));
		}
		Map<User, End> mappedResult = ResultMapper.map(users, ends, changeIdx);

		Assertions.assertThat(mappedResult).containsExactlyEntriesOf(expected);
	}

	@Nested
	static class ToResultTest {
		static List<User> users = new Users(List.of("a", "b", "c", "d")).getUsers();
		static List<End> ends = new Ends(List.of("가", "나", "다", "라"), 4).getEnds();
		static Map<User, End> result = new LinkedHashMap<>();

		@BeforeAll
		static void beforeAll() {
			for (int i = 0; i < 4; i++) {
				result.put(users.get(i), ends.get(i));
			}
		}

		@ParameterizedTest
		@DisplayName("한 명의 결과만을 반환해야 한다.")
		@CsvSource({"a,가", "b,나", "c,다", "d,라"})
		void toResultSingleTest(String name, String end) {
			Map<User, End> selectedResult = ResultMapper.toResult(result, List.of(name));
			Assertions.assertThat(selectedResult.size()).isEqualTo(1);
			Assertions.assertThat(selectedResult.get(new User(name)).getName()).isEqualTo(end);
		}

		@Test
		@DisplayName("여러 명의 결과들을 요청된 순서대로 반환해야 한다.")
		void toResultMultiTest() {
			List<String> targetUserNames = List.of("c", "b", "a");
			Map<User, End> expected = new LinkedHashMap<>();
			expected.put(new User("c"), new End("다"));
			expected.put(new User("b"), new End("나"));
			expected.put(new User("a"), new End("가"));

			Map<User, End> selectedResult = ResultMapper.toResult(result, targetUserNames);
			Assertions.assertThat(selectedResult)
				.containsExactlyEntriesOf(expected);
		}

		@Test
		@DisplayName("'all'이 포함된 요청은 저장된 순서대로 값을 반환해야 한다.")
		void toResultAllTest() {
			List<String> targetUserNames = List.of("c", "b", "all");
			Map<User, End> expected = new LinkedHashMap<>();
			expected.put(new User("a"), new End("가"));
			expected.put(new User("b"), new End("나"));
			expected.put(new User("c"), new End("다"));
			expected.put(new User("d"), new End("라"));

			Map<User, End> selectedResult = ResultMapper.toResult(result, targetUserNames);
			Assertions.assertThat(selectedResult)
				.containsExactlyEntriesOf(expected);
		}

		@Test
		@DisplayName("존재하지 않는 사용자에 대한 결과 요청은 오류를 반환해야 한다.")
		void userNotExistTest() {
			List<String> targetUserNames = List.of("a", "b", "null");

			Assertions.assertThatThrownBy(() -> ResultMapper.toResult(result, targetUserNames))
				.isInstanceOf(IllegalArgumentException.class);
		}

		@Test
		@DisplayName("중복된 사용자에 대한 결과 요청은 오류를 반환해야 한다.")
		void userDuplicatedTest() {
			List<String> targetUserNames = List.of("a", "b", "b");

			Assertions.assertThatThrownBy(() -> ResultMapper.toResult(result, targetUserNames))
				.isInstanceOf(IllegalArgumentException.class);
		}
	}
}
