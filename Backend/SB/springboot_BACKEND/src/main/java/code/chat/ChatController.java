package code.chat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {

    @Autowired
    MessageRepository messageRepository;

    @DeleteMapping("/deleteAllMessage")
    public String deleteAllMessage(){
        if(!messageRepository.findAll().isEmpty()){
            messageRepository.deleteAll();
            return "success";
        }
        return "failure";
    }

}
