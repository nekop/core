package org.jboss.weld.examples.numberguess;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Instance;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@SessionScoped
public class Game implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 991300443278089016L;

    private static final int DEFAULT_REMAINING_GUESSES = 10;

    private int number;

    private int guess;
    private int smallest;

    @Inject
    @MaxNumber
    private int maxNumber;

    private int biggest;
    private int remainingGuesses;

    @Inject
    @Random
    Instance<Integer> randomNumber;

    public Game() {
    }

    public int getNumber() {
        return number;
    }

    public int getGuess() {
        return guess;
    }

    public void setGuess(int guess) {
        this.guess = guess;
    }

    public int getSmallest() {
        return smallest;
    }

    public int getBiggest() {
        return biggest;
    }

    public int getRemainingGuesses() {
        return remainingGuesses;
    }

    public void check() {
        if (isGuessHigher()) {
            biggest = guess - 1;
        } else if (isGuessLower()) {
            smallest = guess + 1;
        } else if (isGuessCorrect()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Correct!"));
        }
        remainingGuesses--;
    }

    @PostConstruct
    public void reset() {
        this.smallest = 0;
        this.guess = 0;
        this.remainingGuesses = DEFAULT_REMAINING_GUESSES;
        this.biggest = maxNumber;
        this.number = randomNumber.get();
    }

    public void validateNumberRange(FacesContext context, UIComponent toValidate, Object value) {
        if (remainingGuesses <= 0) {
            FacesMessage message = new FacesMessage("No guesses left!");
            context.addMessage(toValidate.getClientId(context), message);
            ((UIInput) toValidate).setValid(false);
            return;
        }
        int input = (Integer) value;

        if (input < smallest || input > biggest) {
            ((UIInput) toValidate).setValid(false);

            FacesMessage message = new FacesMessage("Invalid guess");
            context.addMessage(toValidate.getClientId(context), message);
        }
    }

    public boolean isGuessHigher() {
        return guess != 0 && guess > number;
    }

    public boolean isGuessLower() {
        return guess != 0 && guess < number;
    }

    public boolean isGuessCorrect() {
        return guess == number;
    }
}
