package in.tech_camp.chat_app.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import in.tech_camp.chat_app.custom_user.CustomUserDetail;
import in.tech_camp.chat_app.entity.MessageEntity;
import in.tech_camp.chat_app.entity.RoomEntity;
import in.tech_camp.chat_app.entity.RoomUserEntity;
import in.tech_camp.chat_app.entity.UserEntity;
import in.tech_camp.chat_app.form.MessageForm;
import in.tech_camp.chat_app.repository.MessageRepository;
import in.tech_camp.chat_app.repository.RoomRepository;
import in.tech_camp.chat_app.repository.RoomUserRepository;
import in.tech_camp.chat_app.repository.UserRepository;
import in.tech_camp.chat_app.validation.ValidationOrder;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class MessageController {
  private final UserRepository userRepository;

  private final RoomUserRepository roomUserRepository;

  private final RoomRepository roomRepository;

  private final MessageRepository messageRepository;

  @GetMapping("/rooms/{roomId}/messages")
  public String showMessages(@PathVariable("roomId") Integer roomId, @AuthenticationPrincipal CustomUserDetail currentUser, Model model){
    // 1. 現在ログインしている人のIDを、セッション（Security）から取り出す
    // 2. そのIDを使って、最新のユーザー情報をDBから検索する
    UserEntity user = userRepository.findById(currentUser.getId());
    // 3. 検索結果を "user" という名前で画面に渡す
    model.addAttribute("user",user);

    //サイドバーに現在ログインしているユーザーが所属しているチャットルーム名を表示する
    // 1.現在ログインしているユーザーのidから部屋の名前を取り出す所属しているチャットルームのエンティティを取り出す
    //リポジトリを使って、自分が所属する「中間データ（ペア）」を全部持ってくる。
    // ログインユーザーが登録されているレコードをすべて取得
    List<RoomUserEntity> roomUserEntities = roomUserRepository.findByUserId(currentUser.getId());
    // 中間テーブルのリストから部屋の名前のリストを抽出して取り出す
    List<RoomEntity> roomList = roomUserEntities.stream()//1．streamAPIを開始
      .map(RoomUserEntity::getRoom)// 2. 中間Entityから「Room」だけを取り出す
      .collect(Collectors.toList());// 3. 取り出したRoomをリストにまとめる
    // 取り出した部屋の名前リスト（roomList）を"rooms"という名前でModelに入れる
    model.addAttribute("rooms", roomList);

    //メッセージ投稿フォームのビューを渡す
    model.addAttribute("messageForm", new MessageForm());
    //パスのroomIdをモデルに渡す
    // model.addAttribute("roomId",roomId);
    //roomIdをRoomEntityで渡す
    RoomEntity room = roomRepository.findById(roomId);
    model.addAttribute("room", room);

    //ルームに投稿された投稿を表示
    List<MessageEntity> messages = messageRepository.findByRoomId(roomId);
    model.addAttribute("messages", messages);


    return "messages/index";
  }

  //メッセージフォームからのポストリクエストを受け取る
  @PostMapping("/rooms/{roomId}/messages")
  public String saveMessage(@PathVariable("roomId") Integer roomId, @ModelAttribute("messageForm") @Validated(ValidationOrder.class) MessageForm messageForm, BindingResult bindingResult, @AuthenticationPrincipal CustomUserDetail currentUser) {

    // バリデーションエラーのチェック
    if(bindingResult.hasErrors()){
      return "redirect:/rooms/" + roomId +"/messages";
    }

    MessageEntity message = new MessageEntity();
    message.setContent(messageForm.getContent());

    UserEntity user = userRepository.findById(currentUser.getId());
    RoomEntity room = roomRepository.findById(roomId);
    message.setUser(user);
    message.setRoom(room);

    try {
        messageRepository.insert(message);
    } catch (Exception e) {
        System.out.println("エラー：" + e);
    }

    return "redirect:/rooms/" + roomId + "/messages";

  }
}
