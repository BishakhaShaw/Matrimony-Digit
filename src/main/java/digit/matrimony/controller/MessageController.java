package digit.matrimony.controller;

import digit.matrimony.dto.MessageDTO;
import digit.matrimony.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @PostMapping("/send")
    public ResponseEntity<MessageDTO> sendMessage(
            @RequestParam Long senderId,
            @RequestParam Long receiverId,
            @RequestParam String content) {
        return ResponseEntity.ok(messageService.sendMessage(senderId, receiverId, content));
    }

    @GetMapping("/between")
    public ResponseEntity<List<MessageDTO>> getMessagesBetweenUsers(
            @RequestParam Long senderId,
            @RequestParam Long receiverId) {
        return ResponseEntity.ok(messageService.getMessagesBetweenUsers(senderId, receiverId));
    }

    @GetMapping("/received/{receiverId}")
    public ResponseEntity<List<MessageDTO>> getMessagesReceived(@PathVariable Long receiverId) {
        return ResponseEntity.ok(messageService.getMessagesReceivedByUser(receiverId));
    }
}

//InterestController (/api/interests)
//Method	Endpoint	Description
//POST	/send	Send an interest from one user to another
//GET	/sent/{senderId}	Get interests sent by a user
//GET	/received/{receiverId}	Get interests received by a user
//PUT	/{interestId}/status	Update the status of an interest
//DELETE	/{interestId}	Delete an interest


//🔹 MessageController (/api/messages)
//Method	Endpoint	Description
//POST	/send	Send a message from one user to another
//GET	/between	Get messages exchanged between two users
//GET	/received/{receiverId}	Get messages received by a user



//🔹 SuggestionController (/api/suggestions)
//Method	Endpoint	Description
//POST	/create	Create a suggestion for a user
//GET	/user/{userId}	Get suggestions for a user
//DELETE	/{suggestionId}	Delete a suggestion


//🔹 PreferenceController (/api/preferences)
//Method	Endpoint	Description
//GET	/user/{userId}	Get preference by user ID
//POST	/create	Create a new preference
//PUT	/update/{id}	Update an existing preference
//DELETE	/{id}	Delete a preference


//🔹 MatchController (/api/matches)
//Method	Endpoint	Description
//POST	/create	Create a match between users
//GET	/user/{userId}	Get matches for a user
//PUT	/{matchId}/deactivate	Deactivate a match
//DELETE	/{matchId}	Delete a match