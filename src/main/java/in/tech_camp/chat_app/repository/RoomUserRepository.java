package in.tech_camp.chat_app.repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

import in.tech_camp.chat_app.entity.RoomUserEntity;

@Mapper
public interface RoomUserRepository {
//RoomUserEntityの情報を受け取り、room_usersテーブルに保存するメソッド
@Insert("INSERT INTO room_users(user_id, room_id) VALUES(#{user.id}, #{room.id})")
@Options(useGeneratedKeys=true, keyProperty="id")
void insert(RoomUserEntity roomUserEntity);
}
