package pl.maciejbadziak.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.maciejbadziak.model.Question;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    //Ta anotacja Quesry jest tu nie potrzebna. Jej się używa tylko jak robisz jakieś super skomplikowane kwerendy. Spring Data jest na tyle madry, że
    // umie wywnioskować po nazwie metody jaką kwerende stworzyć i puścic :)
    List<Question> findAllByLevel(@Param("levelNumber") int levelNumber);

}
