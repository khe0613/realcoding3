package kr.ac.cnu.web.games.blackjack;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rokim on 2018. 5. 26..
 */
public class Hand {
    private Deck deck;
    @Getter
    private List<Card> cardList = new ArrayList<>();

    public Hand(Deck deck) {
        this.deck = deck;
    }

    public Card drawCard() {
        Card card = deck.drawCard();
        cardList.add(card);
        return card;
    }

    public int getCardSum() {
        int sum = 0;
        int ace_count = 0;
        for(Card card : cardList) {
            int input = card.getRank();
            if(input == 1){
                ace_count++;
            }
            if(input > 10) {
                sum += 10;
                if(sum == 11 && ace_count != 0){
                    sum += 10;
                    if(sum > 21){
                        sum -= 10;
                    }
                }
            }else {
                sum += input;
                if(sum == 11 && ace_count != 0){
                    sum += 10;
                    if(sum > 21){
                        sum -= 10;
                    }
                }
            }
        }
        return sum;
    }


    public void reset() {
        cardList.clear();
    }
}
