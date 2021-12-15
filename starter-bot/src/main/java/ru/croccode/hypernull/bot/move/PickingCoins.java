package ru.croccode.hypernull.bot.move;

import ru.croccode.hypernull.geometry.Offset;
import ru.croccode.hypernull.geometry.Point;
import ru.croccode.hypernull.geometry.Size;

import java.util.Set;

public class PickingCoins {

    /** Метод поиска ближайшей монеты **/
    public Point getClosestCoin(Set<Point> coinsInRange, Point currentPosition, Size mapSize) {
        double[] distances = new double[coinsInRange.size()]; //массив всех расстояний от позиции бота до монеты
        int i = 0;
        Point closestCoin = coinsInRange.iterator().next();
        distances[0] = currentPosition.offsetTo(coinsInRange
                .iterator()
                .next(), mapSize).length();
        //System.out.println("distances: " + distances[0]);
        for (Point coin : coinsInRange) {
            if (i == 0) {
                i++;
                continue;
            }

            distances[i] = currentPosition.offsetTo(coin, mapSize).length();
            //System.out.println("distances: " + distances[i]);
            if (distances[i] < distances[i-1])
                closestCoin = coin;
            i++;
        }
        return closestCoin;
    }

    /** Логика бота для нахождения пути к монете, без учета препятствий **/
    public Offset goToClosestCoin(Point currentPos, Point closestCoin, Set<Point> blocks) {
        Offset potentialOffset = null;

        //Если монета ровно над/под ботом
        if (currentPos.x() == closestCoin.x()) {
            //Если монета под ботом
            if (currentPos.y() - closestCoin.y() > 0)
                potentialOffset = new Offset(0, -1);
            //Если монета над ботом
            else
                potentialOffset = new Offset(0, 1);
        }

        //Если монета ровно слева/справа от бота
        else if (currentPos.y() == closestCoin.y()) {
            //Если монета слева
            if (currentPos.x() - closestCoin.x() > 0)
                potentialOffset = new Offset(-1,0);
            //Если монета справа
            else
                potentialOffset = new Offset(1, 0);
        }

        //Во всех остальных случаях
        else {
            //Монета слева снизу
            if ((currentPos.x() - closestCoin.x() > 0) && (currentPos.y() - closestCoin.y() > 0) )
                potentialOffset = new Offset(-1,-1);
            //Монета слева сверху
            else if ((currentPos.x() - closestCoin.x() > 0) && (currentPos.y() - closestCoin.y() < 0))
                potentialOffset = new Offset(-1,1);
            //Монета справа снизу
            else if ((currentPos.x() - closestCoin.x() < 0) && (currentPos.x() - closestCoin.x() > 0))
                potentialOffset = new Offset(1,-1);
            //Монета справа сверху
            else
                potentialOffset = new Offset(1,1);
        }

        AvoidingBarriers avoid = new AvoidingBarriers();
        return avoid.avoidBarrier(potentialOffset,blocks,currentPos); //Обход препятствий если они есть
    }
}
