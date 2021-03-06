package pl.maciejbadziak.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.context.WebApplicationContext;
import pl.maciejbadziak.data.QuestionRepository;
import pl.maciejbadziak.model.Lifeline;
import pl.maciejbadziak.model.Question;

import java.util.List;
import java.util.Random;

@Service
@Scope(scopeName= WebApplicationContext.SCOPE_SESSION, proxyMode= ScopedProxyMode.TARGET_CLASS)
public class GameService {

    @Autowired
    private Random random;
    private QuestionRepository questionRepository;

    public GameService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    String[] levelAmounts = {"12 - 1 000 000 zł", "11 - 500 000 zł", "10 - 250 000 zł", "9 - 125 000 zł", "8 - 75 000 zł",
            "7 - 40 000 zł", "6 - 20 000 zł", "5 - 10 000 zł", "4 - 5000 zł", "3 - 2000 zł", "2 - 1000 zł", "1 - 500 zł"};
    String[] amounts = {"0 zł", "500 zł", "1000 zł", "2000 zł", "5000 zł", "10 000 zł", "20 000 zł",
            "40 000 zł", "75 000 zł", "125 000 zł", "250 000 zł", "500 000 zł", "1 000 000 zł"};
    Lifeline lifeline = new Lifeline(false, false, false);
    int currentLevel = 0;
    int lastLevel = 11;
    int guaranteedLevel1 = 2;
    int guaranteedLevel2 = 8;
    int guaranteedMillion = 12;


    public String startNewGame(String ifNewGame, String lifelineFiftyStatus, String lifelinePhoneStatus, String lifelineAudienceStatus, Model model) {
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

    public String getPrice(Model model) {
        String amount = amounts[currentLevel - 1];
        model.addAttribute("prize", amount);
        currentLevel = 0;
        return "reward";
    }

    public String getGuaranteedPrice(Model model) {
        if (currentLevel <= guaranteedLevel1)
            model.addAttribute("prize", "0 zł");
        if (currentLevel > guaranteedLevel1 && currentLevel <= guaranteedLevel2)
            model.addAttribute("prize", "1000 zł");
        if (currentLevel > guaranteedLevel2 && currentLevel < guaranteedMillion)
            model.addAttribute("prize", "40 000 zł");
        currentLevel = 0;
        return "reward";
    }


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
        return list.get(random.nextInt(list.size()));
    }

    private void setModel(Model model, Question questionToAdd) {
        model.addAttribute("question", questionToAdd);
        model.addAttribute("levels", levelAmounts);
        model.addAttribute("lifeline", lifeline);
    }
}
