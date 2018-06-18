package kr.ac.cnu.web.games.blackjack;

import kr.ac.cnu.web.controller.api.BlackApiController;
import kr.ac.cnu.web.model.User;
import kr.ac.cnu.web.repository.UserRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;


import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by rokim on 2018. 5. 26..
 */


public class GameRoom {

    @Getter
    private final String roomId;
    @Getter
    private final Dealer dealer;
    @Getter
    private final Map<String, Player> playerList;
    @Getter
    private final Deck deck;
    @Getter
    private boolean isFinished;
    private final Evaluator evaluator;



    public GameRoom(Deck deck) {
        this.roomId = UUID.randomUUID().toString();
        this.deck = deck;
        this.dealer = new Dealer(new Hand(deck));
        this.playerList = new HashMap<>();
        this.evaluator = new Evaluator(playerList, dealer);
        this.isFinished = true;
    }

    public void addPlayer(String playerName, long seedMoney) {
        Player player = new Player(seedMoney, new Hand(deck));

        playerList.put(playerName, player);

    }

    public void removePlayer(String playerName) {
        playerList.remove(playerName);
    }

    public void reset() {
        dealer.reset();
        playerList.forEach((s, player) -> player.reset());
    }

    public void bet(String name, long bet) {
        Player player = playerList.get(name);

        player.placeBet(bet);
    }

    public void deal() {
        this.isFinished = false;
        dealer.deal();
        playerList.forEach((s, player) -> player.deal());
    }

    public Card hit(String name, GameRoom gameRoom, UserRepository userRepository) {
        Player player = playerList.get(name);
        Card hitCard = player.hitCard();
        if(player.getHand().getCardSum() > 21) {
            this.isFinished = true;
            gameRoom.playDealer(userRepository);
        }
        return hitCard;
    }

    public void stand(String name) {
        Player player = playerList.get(name);

        player.stand();
    }

    public void playDealer(UserRepository userRepository) {
        dealer.play();
        evaluator.evaluate();

        playerList.forEach((string,player)->{

            User user = new User (string,player.getBalance());
            userRepository.save(user);
        });

        this.isFinished = true;
    }
    public Card doubleDown(String name) {
        Player player = playerList.get(name);

        return player.doubleDown();
    }
}
