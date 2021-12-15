package ru.croccode.hypernull.bot.move;

import ru.croccode.hypernull.geometry.Offset;
import ru.croccode.hypernull.geometry.Point;

import java.util.Random;
import java.util.Set;

public class AvoidingBarriers {
    private static final Random rnd = new Random(System.currentTimeMillis());

    public Offset avoidBarrier(Offset potentialOffset, Set<Point> blocks, Point currentPos) {
//        if (!blocks.contains(new Point(
//                currentPos.x() + potentialOffset.dx(),
//                currentPos.y() + potentialOffset.dy())))
//            return potentialOffset;

        int px;
        int py;

        do {
            px = rnd.nextInt(3) - 1;
            py = rnd.nextInt(3) - 1;
        } while (blocks.contains(new Point(
                currentPos.x() + px,
                currentPos.y() + py)));
        System.out.println(blocks.contains(new Point(
                currentPos.x() + px,
                currentPos.y() + py)));
        potentialOffset = new Offset(px, py);
        return potentialOffset;
    }

}
