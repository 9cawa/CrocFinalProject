package ru.croccode.hypernull.bot;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.*;

import ru.croccode.hypernull.bot.move.AvoidingBarriers;
import ru.croccode.hypernull.bot.move.PickingCoins;
import ru.croccode.hypernull.domain.MatchMode;
import ru.croccode.hypernull.geometry.Offset;
import ru.croccode.hypernull.geometry.Point;
import ru.croccode.hypernull.geometry.Size;
import ru.croccode.hypernull.io.SocketSession;
import ru.croccode.hypernull.message.Hello;
import ru.croccode.hypernull.message.MatchOver;
import ru.croccode.hypernull.message.MatchStarted;
import ru.croccode.hypernull.message.Move;
import ru.croccode.hypernull.message.Register;
import ru.croccode.hypernull.message.Update;

public class StarterBot implements Bot {

	private static final Random rnd = new Random(System.currentTimeMillis());

	private final MatchMode mode;

	private Offset moveOffset;

	private int moveCounter = 0;

	private Integer myID;

	private Size mapSize;

	Set<Point> blocks;

	public StarterBot(MatchMode mode) {
		this.mode = mode;
	}

	@Override
	public Register onHello(Hello hello) {
		Register register = new Register();
		register.setMode(mode);
		register.setBotName("cawok");
		return register;
	}

	@Override
	public void onMatchStarted(MatchStarted matchStarted) {
		mapSize = matchStarted.getMapSize();
		myID = matchStarted.getYourId();
	}

	@Override
	public Move onUpdate(Update update) {
		try {
			Thread.sleep(900);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		Point currentPosition = update.getBots().get(myID); //Текущая позиция
		blocks = update.getBlocks();
//		System.out.println(currentPosition);
//		System.out.println("blocks: " + blocks);

		Set<Point> coinsInRange = update.getCoins(); //Координаты монет по близости
//		System.out.println(coinsInRange);

		if (coinsInRange != null) {
			PickingCoins p = new PickingCoins();
			Point closestCoin = p.getClosestCoin(coinsInRange, currentPosition, mapSize);
//			System.out.println("closest coin: " + closestCoin);
			moveOffset = p.goToClosestCoin(currentPosition, closestCoin, blocks);
		}
		if (moveOffset == null) {
			Offset potentialOffset = new Offset(
					rnd.nextInt(3) - 1,
					rnd.nextInt(3) - 1
			);
			AvoidingBarriers avoid = new AvoidingBarriers();
			moveOffset = avoid.avoidBarrier(potentialOffset,blocks,currentPosition);
			moveCounter = 0;
		}

		moveCounter++;
		Move move = new Move();
		move.setOffset(moveOffset);
		System.out.println(move.getOffset());
		return move;
	}

	@Override
	public void onMatchOver(MatchOver matchOver) {
	}

	public static void main(String[] args) throws IOException {
		Socket socket = new Socket();
		socket.setTcpNoDelay(true);
		socket.setSoTimeout(300_000);
		socket.connect(new InetSocketAddress("localhost", 2021));

		SocketSession session = new SocketSession(socket);
		StarterBot bot = new StarterBot(MatchMode.FRIENDLY);
		new BotMatchRunner(bot, session).run();
	}
}
