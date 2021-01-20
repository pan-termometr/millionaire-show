package pl.maciejbadziak.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.maciejbadziak.data.QuestionRepository;
import pl.maciejbadziak.model.Lifeline;
import pl.maciejbadziak.model.Question;

import java.util.List;
import java.util.Random;

@Controller
public class GameController {

    @Autowired
    private Random random;
    private final QuestionRepository questionRepository;

    @Autowired
    public GameController(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    String[] levelAmounts = {"12 - 1 000 000 zł", "11 - 500 000 zł", "10 - 250 000 zł", "9 - 125 000 zł", "8 - 75 000 zł",
            "7 - 40 000 zł", "6 - 20 000 zł", "5 - 10 000 zł", "4 - 5000 zł", "3 - 2000 zł", "2 - 1000 zł", "1 - 500 zł"};
    String[] amounts = {"0 zł", "500 zł", "1000 zł", "2000 zł", "5000 zł", "10 000 zł", "20 000 zł",
            "40 000 zł", "75 000 zł", "125 000 zł", "250 000 zł", "500 000 zł", "1 000 000 zł"};
    Lifeline lifeline = new Lifeline(false, false, false);
    // @Controller jak i inne springowe beany są domyslnie tworzone jako singletony. Tzn. jak odpalasz aplikacje tworzy TYLKO JEDEN taki objekt i on obsługuje
    // wszystkie requesty przychodzące do aplikacji. Wiec jak sie domyslasz zapisywanie tu stanu (czyli jakichkolwiek zmiennych) jest bardzo złym pomysłem.
    // Kiody dwoch uzytkownikow na raz bedzie korzystac z tej aplikacji beda oni dzielili ponizsze zmienne. Powinienes to przechowywac albo w tym objekcie model
    // ktory otrzymujesz w kazdej metodzie, albo wogole w ciasteczkach jakis. Wiecej o tym jak sa twoprzone takie beany tutaj: https://www.baeldung.com/spring-bean-scopes
    int currentLevel = 0;
    int lastLevel = 11;
    int guaranteedLevel1 = 2;
    int guaranteedLevel2 = 8;
    int guaranteedMillion = 12;

    @PostMapping("/game")
    String startGame(@ModelAttribute Question question, Model model,
                     @RequestParam(name = "newGame", defaultValue = "false") String ifNewGame,
                     @RequestParam(value = "lifelineFiftyStatus", defaultValue = "noUsed") String lifelineFiftyStatus,
                     @RequestParam(value = "lifelinePhoneStatus", defaultValue = "noUsed") String lifelinePhoneStatus,
                     @RequestParam(value = "lifelineAudienceStatus", defaultValue = "noUsed") String lifelineAudienceStatus) {
        setNewGame(ifNewGame);
        int i = currentLevel;
        while (i <= lastLevel) {
            setLifelines(lifelineFiftyStatus, lifelinePhoneStatus, lifelineAudienceStatus);
            Question questionToSend = getQuestion();
            setModel(model, questionToSend);
            currentLevel++;
            return "game";
        }
        return "endGame";
    }

    @PostMapping("/end")
    String showPrize(@ModelAttribute Question question, Model model) {
        String amount = amounts[currentLevel - 1];
        model.addAttribute("prize", amount);
        currentLevel = 0;
        return "reward";
    }

    @PostMapping("/badAnswer")
    String showGuaranteedPrize(@ModelAttribute Question question, Model model) {
        if (currentLevel <= guaranteedLevel1)
            model.addAttribute("prize", "0 zł");
        if (currentLevel > guaranteedLevel1 && currentLevel <= guaranteedLevel2)
            model.addAttribute("prize", "1000 zł");
        if (currentLevel > guaranteedLevel2 && currentLevel < guaranteedMillion)
            model.addAttribute("prize", "40 000 zł");
        currentLevel = 0;
        return "reward";
    }

    // Ogolnie controler nie powinien zawierac innych metod niż typu Get, Post itd. Ta klasa powinna obslugiwac tylko przetwarzanie zapytan + ewentualna
    // autoryzacje. Ponizsze metody powinienes wywalic do jakiegos serwisu. Na ten moment jest to niemozliwe bo dodales stan w tym controlerze (zmienne) co jest bledem o czym pisze wyzej :)
    private void setNewGame(String ifTrue) {
        if (ifTrue.equals("true")) {
            currentLevel = 0;
            lifeline.setFifty(true);
            lifeline.setPhone(true);
            lifeline.setAudience(true);
        }
    }

    private void setLifelines(String lifelineFiftyStatus, String lifelinePhoneStatus, String lifelineAudienceStatus) {
        if (lifelineFiftyStatus.equals("used"))
            lifeline.setFifty(false);
        if (lifelinePhoneStatus.equals("used"))
            lifeline.setPhone(false);
        if (lifelineAudienceStatus.equals("used"))
            lifeline.setAudience(false);
    }

    private Question getQuestion() {
        List<Question> list = questionRepository.findAllByLevel(currentLevel + 1);
        // Krocej
        return list.get(random.nextInt(list.size()));
    }

    private void setModel(Model model, Question questionToAdd) {
        model.addAttribute("question", questionToAdd);
        model.addAttribute("levels", levelAmounts);
        model.addAttribute("lifeline", lifeline);
    }

    // Nie powineienes tworzyc nowych beanow w controlerach. Od tego sa klasy konfiguracyjne. Stworzylem taka i to tam przeniosłem.
}
