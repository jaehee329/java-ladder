package domain.ladder;

import java.util.ArrayList;
import java.util.List;

import domain.util.LinePointsGenerator;
import domain.util.Point;
import domain.util.PointGenerator;

public class LadderBuilder {
	public Ladder build(final LadderHeight ladderHeight,
		final LadderWidth ladderWidth,
		final PointGenerator pointGenerator) {

		LinePointsGenerator linePointsGenerator = new LinePointsGenerator(ladderWidth.getWidth(), pointGenerator);
		List<Line> lines = new ArrayList<>();
		int height = ladderHeight.getHeight();
		for (int i = 0; i < height; i++) {
			List<Point> points = linePointsGenerator.generateLine();
			Line line = new Line(points);
			lines.add(line);
		}
		return new Ladder(lines);
	}
}
