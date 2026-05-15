package in.tech_camp.chat_app.repository;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Select;

import in.tech_camp.chat_app.entity.RoomUserEntity;

@Mapper
public interface RoomUserRepository {
//RoomUserEntityの情報を受け取り、room_usersテーブルに保存するメソッド
@Insert("INSERT INTO room_users(user_id, room_id) VALUES(#{user.id}, #{room.id})")
@Options(useGeneratedKeys=true, keyProperty="id")
void insert(RoomUserEntity roomUserEntity);

// ユーザーidでroom_usersテーブルを検索するメソッドを定義する
@Select("SELECT * FROM room_users WHERE user_id = #{userId}")
//アソシエーションを使用してRoomEntityを取得できるようにする
@Result(property="room", column="room_id",
        one=@One(select="in.tech_camp.chat_app.repository.RoomRepository.findById"))
List<RoomUserEntity> findByUserId(Integer userId);
}
