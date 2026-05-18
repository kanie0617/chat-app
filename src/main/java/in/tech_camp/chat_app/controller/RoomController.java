package in.tech_camp.chat_app.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.support.DefaultMessageSourceResolvable;
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
import in.tech_camp.chat_app.entity.RoomEntity;
import in.tech_camp.chat_app.entity.RoomUserEntity;
import in.tech_camp.chat_app.entity.UserEntity;
import in.tech_camp.chat_app.form.RoomForm;
import in.tech_camp.chat_app.repository.RoomRepository;
import in.tech_camp.chat_app.repository.RoomUserRepository;
import in.tech_camp.chat_app.repository.UserRepository;
import in.tech_camp.chat_app.validation.ValidationOrder;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class RoomController {
  private final UserRepository userRepository;

  private final RoomRepository roomRepository;

  private final RoomUserRepository roomUserRepository;

  //トップページ表示
  @GetMapping("/")
  public String index(@AuthenticationPrincipal CustomUserDetail currentUser, Model model){
    //現在ログインしているユーザーの情報をビューに渡す
    UserEntity user = userRepository.findById(currentUser.getId());
    model.addAttribute("user", user);

    //サイドバーに現在ログインしているユーザーが所属しているチャットルーム名を表示する
    List<RoomUserEntity> roomUserEntities = roomUserRepository.findByUserId(currentUser.getId());
    // 中間テーブルのリストから部屋の名前のリストを抽出して取り出す
    List<RoomEntity> roomList = roomUserEntities.stream()//1．streamAPIを開始
      .map(RoomUserEntity::getRoom)// 2. 中間Entityから「Room」だけを取り出す
      .collect(Collectors.toList());// 3. 取り出したRoomをリストにまとめる
    // 取り出した部屋の名前リスト（roomList）を"rooms"という名前でModelに入れる
    model.addAttribute("rooms", roomList);
    return "rooms/index";
  }

  //新規チャットルーム作成ページ表示
  @GetMapping("/rooms/new")
  public String showRoomNew (@AuthenticationPrincipal CustomUserDetail currentUser, Model model){
    List<UserEntity> users = userRepository.findAllExcept(currentUser.getId());
    model.addAttribute("users",users);
    model.addAttribute("roomForm", new RoomForm());
    return "rooms/new";
  }
  //チャットルーム保存機能
  @PostMapping("/rooms")
  public String createRoom(@ModelAttribute("roomForm") @Validated(ValidationOrder.class) RoomForm roomForm, BindingResult bindingResult, @AuthenticationPrincipal CustomUserDetail currentUser, Model model) {
    // System.out.println("roomForm:" + roomForm);
    //チャットルーム自体の保存（入力フォームから届いたルーム名をroomsテーブルに保存）
    //バリデーションエラーチェック
    if (bindingResult.hasErrors()) {
      List<String> errorMessages = bindingResult.getAllErrors().stream()
        .map(DefaultMessageSourceResolvable::getDefaultMessage)
        .collect(Collectors.toList());

      //選択できるユーザーがいなくなってしまわないようにもう一度DBから最新の状態で取り直してモデルに詰めてビューに返す
      List<UserEntity> users = userRepository.findAllExcept(currentUser.getId());
      model.addAttribute("users", users);  
      model.addAttribute("roomForm", roomForm);
      model.addAttribute("errorMessages", errorMessages);

      return "/rooms/new";
    }
    
    RoomEntity roomEntity = new RoomEntity();
    roomEntity.setName(roomForm.getName());
    try {
        roomRepository.insert(roomEntity);
    } catch (Exception e) {
        System.out.println("エラー：" + e);
        List<UserEntity> users = userRepository.findAllExcept(currentUser.getId());
        model.addAttribute("users", users);
        model.addAttribute("roomForm", new RoomForm());
        return "rooms/new";
    }

    //フォームから送られてきたmemberIdsを使ってroom_usersテーブルにデータを保存
    List<Integer> memberIds = roomForm.getMemberIds();
    //ループで1人ずつユーザー情報を取得する
    for(Integer userId : memberIds) {
      UserEntity userEntity = userRepository.findById(userId);
      RoomUserEntity roomUserEntity = new RoomUserEntity();
      roomUserEntity.setRoom(roomEntity);
      roomUserEntity.setUser(userEntity);
      try {
          roomUserRepository.insert(roomUserEntity);
      } catch (Exception e) {
          System.out.println("エラー：" + e);
          List<UserEntity> users = userRepository.findAllExcept(currentUser.getId());
          model.addAttribute("users", users);
          model.addAttribute("roomForm", new RoomForm());
          return "rooms/new";
      }
    }
    return "redirect:/";
  }

//チャットルーム削除
  @PostMapping("/rooms/{roomId}/delete")
  public String deleteRoom(@PathVariable("roomId") Integer roomId){
    try {
      roomRepository.deleteById(roomId);
    } catch (Exception e) {
      System.out.println("エラー: " + e);
      return "redirect:/";
    }
    return "redirect:/";
  }

}
