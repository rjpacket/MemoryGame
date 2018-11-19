package com.rjp.memorygame.guessMovie;

public class Card {

    /**
     * id : 01
     * image : https://img3.doubanio.com/view/photo/l/public/p480747492.webp
     * answer : 肖申克的救赎
     * cards : 肖申克的救赎
     */

    private String id;
    private String image;
    private String answer;
    private String cards;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getCards() {
        return cards;
    }

    public void setCards(String cards) {
        this.cards = cards;
    }
}
