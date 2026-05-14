package in.tech_camp.chat_app.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import in.tech_camp.chat_app.custom_user.CustomUserDetail;
import in.tech_camp.chat_app.entity.UserEntity;
import in.tech_camp.chat_app.repository.UserRepository;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class MessageController {
  private final UserRepository userRepository;

  @GetMapping("/")
  public String showMessages(@AuthenticationPrincipal CustomUserDetail currentUser, Model model){
    // 1. 現在ログインしている人のIDを、セッション（Security）から取り出す
    // 2. そのIDを使って、最新のユーザー情報をDBから検索する
    UserEntity user = userRepository.findById(currentUser.getId());
    // 3. 検索結果を "user" という名前で画面に渡す
    model.addAttribute("user",user);
    return "messages/index";
  }
}
