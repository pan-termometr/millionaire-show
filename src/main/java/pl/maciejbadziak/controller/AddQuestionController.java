package pl.maciejbadziak.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import pl.maciejbadziak.data.QuestionRepository;
import pl.maciejbadziak.model.Question;

@Controller
public class AddQuestionController {

    private QuestionRepository questionRepository;

    @Autowired
    public AddQuestionController(QuestionRepository questionRepository){
        this.questionRepository = questionRepository;
    }
//Po co tu są dwie metody które robią dokładnie to samo ?
    @GetMapping
    public String add(Model model) {
        model.addAttribute("question", new Question());
        return "addQuestion";
    }

    @PostMapping("/add")
    public String addQuestion(Model model) {
        model.addAttribute("question", new Question());
        return "addQuestion";
    }

    @PostMapping("/add-new-question")
    public String addNewQuestion(@ModelAttribute Question question) {
        questionRepository.save(question);
        return "index";
    }
}
