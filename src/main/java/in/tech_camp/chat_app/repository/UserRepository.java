package in.tech_camp.chat_app.repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import in.tech_camp.chat_app.entity.UserEntity;

@Mapper
public interface UserRepository {
  @Insert("INSERT INTO users (name,email,password) VALUES (#{name},#{email},#{password})")
  @Options(useGeneratedKeys=true,keyProperty="id")
  void insert(UserEntity user);

  //UserAuthenticationServiceで使用するemailでユーザー情報を取得するメソッドを定義
  @Select("SELECT * FROM users WHERE email = #{email}")
  UserEntity findByEmail(String email);

  //idでユーザー情報を取得するメソッドを定義
  @Select("SELECT * FROM users WHERE id = #{id}")
  UserEntity findById(Integer id);

  //DBに保存されているユーザーの情報を更新
  @Update("UPDATE users SET name = #{name}, email = #{email} WHERE id = #{id}")
  void update(UserEntity user);

  //メールアドレスが使用済みであるかどうかを確認するメソッドを定義
  @Select("SELECT EXISTS(SELECT 1 FROM users WHERE email = #{email})")
  boolean existsByEmail(String email);

  //指定のユーザーID以外でメールアドレスが使われていないかを確認するメソッドを定義
  // @Select("SELECT EXISTS(SELECT 1 FROM users WHERE email = #{email} AND id != #{userId})")
  // boolean existsByEmailAndIdNot(String email, Integer userId);
  @Select("SELECT COUNT (*) > 0 FROM users WHERE email = #{email} AND id != #{userId}")
  boolean existsByEmailExcludingCurrent(String email, Integer userId);
}
