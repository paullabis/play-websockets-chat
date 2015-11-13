package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.WebSocket;
import views.html.chatRoom;
import views.html.index;

import com.fasterxml.jackson.databind.JsonNode;

public class Application extends Controller {
  
    /**
     * Display the home page.
     */
    public Result index() {
        return ok(index.render());
    }
  
    /**
     * Display the chat room.
     */
    public Result chatRoom(String username, String roomName) {
        if(username == null || username.trim().equals("")) {
            flash("error", "Please choose a valid username.");
            return redirect(controllers.routes.Application.index());
        }

        return ok(chatRoom.render(username, roomName));
    }

    public Result chatRoomJs(String username, final String roomName) {
        return ok(views.js.chatRoom.render(username, roomName));
    }
    
    /**
     * Handle the chat websocket.
     */
    public WebSocket<JsonNode> chat(final String username, final String roomName) {
        return new WebSocket<JsonNode>() {
            public void onReady(WebSocket.In<JsonNode> in, WebSocket.Out<JsonNode> out){
                try {
                    components.chat.ChatRoom.join(roomName, username, in, out);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };
    }
  
}
