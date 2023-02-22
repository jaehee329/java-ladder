package domain.util;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import domain.ladder.Ladder;
import domain.ladder.Line;

public class IndexMover {

	public static List<Integer> getMovedIndex(final Ladder ladder) {
		List<Integer> indicies = getInitialIdx(ladder.getWidth());
		List<Line> lines = ladder.getLines();
		for (Line line : lines) {
			moveOnce(indicies, line);
		}
		return indicies;
	}

	private static List<Integer> getInitialIdx(final int size) {
		return IntStream.rangeClosed(0, size).boxed().collect(Collectors.toList());
	}

	private static void moveOnce(final List<Integer> indicies, final Line line) {
		List<Point> points = line.getPoints();
		int size = points.size();
		for (int i = 0; i < size; i++) {
			Point point = points.get(i);
			swapIfPointPresent(indicies, i, point);
		}
	}

	private static void swapIfPointPresent(final List<Integer> indicies, final int i, final Point point) {
		if (point.isPresent()) {
			swap(indicies, i, i + 1);
		}
	}

	private static void swap(final List<Integer> indicies, final int swapIdx1, final int swapIdx2) {
		int temp = indicies.get(swapIdx1);
		indicies.set(swapIdx1, indicies.get(swapIdx2));
		indicies.set(swapIdx2, temp);
	}
}
