package in.tech_camp.chat_app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import in.tech_camp.chat_app.entity.UserEntity;
import in.tech_camp.chat_app.form.LoginForm;
import in.tech_camp.chat_app.form.UserEditForm;
import in.tech_camp.chat_app.form.UserForm;
import in.tech_camp.chat_app.repository.UserRepository;
import in.tech_camp.chat_app.service.UserService;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class UserController {

  private final UserRepository userRepository;

  private final UserService userService;

  // 新規登録ページ表示
  @GetMapping("/users/sign_up")
  public String showSignUp(Model model) {
    model.addAttribute("userForm", new UserForm());
    return "users/signUp";
  }

  // 新規登録ページでフォームに入力して登録
  @PostMapping("/user")
  public String createUser(@ModelAttribute("userForm") UserForm userForm, Model model) {

    UserEntity userEntity = new UserEntity();
    userEntity.setName(userForm.getName());
    userEntity.setEmail(userForm.getEmail());
    userEntity.setPassword(userForm.getPassword());

    try {
        // userRepository.insert(userEntity);
        userService.createUserWithEncryptedPassword(userEntity);
    } catch (Exception e) {
      System.out.println("エラー: "+e);
      model.addAttribute("userForm", userForm);
      return "users/signUp";
    }

    return "redirect:/";
  }

  // ログインページの表示
  @GetMapping("/users/login")
  public String loginForm(Model model) {
    model.addAttribute("loginForm",new LoginForm());//LoginFormのインスタンスを生成
    return "users/login";//ビューファイルを返す
  }
  //ログインの処理
  @GetMapping("/login")
  public String login(@RequestParam(value="error", required=false) String error, @ModelAttribute("loginForm") LoginForm loginForm,Model model) {
    if(error != null) {
      model.addAttribute("loginError","メールアドレスかパスワードが間違っています。");
    }
    return "users/login";
  }

  //ユーザー編集機能
  @GetMapping("/users/{userId}/edit")
  public String editUserForm(@PathVariable("userId") Integer userId, Model model){
    UserEntity user = userRepository.findById(userId);

    UserEditForm userForm = new UserEditForm();
    userForm.setId(user.getId());
    userForm.setName(user.getName());
    userForm.setEmail(user.getEmail());

    // model.addAttribute("userEditForm",userEditForm);
    model.addAttribute("user", userForm);
    return "users/edit";
  }

  //ユーザー編集画面で入力したものでユーザー情報を更新する
  @PostMapping("/users/{userId}")
  public String updateUser(@PathVariable("userId")Integer userId,@ModelAttribute("user")UserEditForm userEditForm, Model model) {
    UserEntity user = userRepository.findById(userId);

    user.setName(userEditForm.getName());
    user.setEmail(userEditForm.getEmail());

    try {
        userRepository.update(user);
    } catch (Exception e) {
        System.out.println("エラー: "+ e);
        model.addAttribute("user",userEditForm);
        return "users/edit";
    }

    return "redirect:/";
  }

}
