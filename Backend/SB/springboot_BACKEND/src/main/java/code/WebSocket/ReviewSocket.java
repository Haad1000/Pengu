package code.WebSocket;

import code.Users.User;
import code.Users.UserRepository;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import code.chat.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

@Controller
@ServerEndpoint(value = "/review/{fromEmail}/{toEmail}")  // Updated WebSocket URL pattern
public class ReviewSocket {

	private static UserRepository userRepo; // Assuming you have a UserRepository
	private static LiveReviewRepository msgRepo;

	@Autowired
	public void setUserRepository(UserRepository repo) {
		ReviewSocket.userRepo = repo;
	}

	@Autowired
	public void setMessageRepository(LiveReviewRepository repo) {
		ReviewSocket.msgRepo = repo;
	}

	private static Map<Session, User> sessionUserMap = new Hashtable<>();
	private static Map<String, Session> userEmailSessionMap = new Hashtable<>();

	private final Logger logger = LoggerFactory.getLogger(ReviewSocket.class);

	@OnOpen
	public void onOpen(Session session, @PathParam("fromEmail") String fromEmail, @PathParam("toEmail") String toEmail) throws IOException {
		logger.info("Entered into Open");

		// Retrieve User entities by email
		User fromUser = userRepo.findByEmailId(fromEmail);
		User toUser = userRepo.findByEmailId(toEmail);

		if (fromUser == null || toUser == null) {
			session.close();
			return;
		}

		sessionUserMap.put(session, fromUser);
		userEmailSessionMap.put(fromEmail, session);

		String broadcastMessage = "The user " + fromUser.getName() + " has connected";
		broadcast(broadcastMessage); // Broadcast to all users when someone connects

		logger.info("Connection established between " + fromEmail + " and " + toEmail);
	}

	@OnMessage
	public void onMessage(Session session, String message, @PathParam("fromEmail") String fromEmail, @PathParam("toEmail") String toEmail) throws IOException {

		logger.info("Entered into Message: Got Message:" + message);
		User fromUser = sessionUserMap.get(session);

		// Retrieve User entities by email
		User fromUser1 = userRepo.findByEmailId(fromEmail);
		User toUser1 = userRepo.findByEmailId(toEmail);

		// Broadcasting every message is a privacy concern! Use with caution.
		String broadcastMessage = "From " + fromUser.getEmailId() + ": " + message;
		broadcast(broadcastMessage); // Broadcast the message to all users

		// Rest of the code for handling direct messages...
		// ...

		LiveReview msg = new LiveReview(fromUser1, toUser1, message);
		msgRepo.save(msg);
	}

	@OnClose
	public void onClose(Session session) throws IOException {
		logger.info("Entered into Close");

		User user = sessionUserMap.get(session);
		if (user != null) {
			sessionUserMap.remove(session);
			userEmailSessionMap.remove(user.getEmailId());
			String broadcastMessage = "User " + user.getEmailId() + " has disconnected.";
			broadcast(broadcastMessage); // Broadcast to all users when someone disconnects

			logger.info(user.getEmailId() + " disconnected");
		}
	}

	@OnError
	public void onError(Session session, Throwable throwable) {
		logger.info("Entered into Error");
		throwable.printStackTrace();
	}

	private void broadcast(String message) {
		sessionUserMap.keySet().forEach(session -> {
			if (session.isOpen()) {
				try {
					session.getBasicRemote().sendText(message);
				} catch (IOException e) {
					logger.info("Exception in broadcast: " + e.getMessage().toString());
					e.printStackTrace();
				}
			}
		});
	}

	// Utility method to find User by email. You'll need to implement this in your UserRepository.
	// Example:
	// User findByEmail(String email);
	// ... Other methods and definitions ...
}
