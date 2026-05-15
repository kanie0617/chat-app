package in.tech_camp.chat_app.entity;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class MessageEntity {
  private Integer id;
  private String content;
  private String image;
  private Timestamp createdAt;

  //外部キー
  private UserEntity user;
  private RoomEntity room;
}
