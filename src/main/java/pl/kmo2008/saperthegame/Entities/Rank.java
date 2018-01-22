package pl.kmo2008.saperthegame.Entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class Rank implements Serializable{
    /**
     * Id of record
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    /**
     * Type of rekord
     * 0 - easy
     * 1 - medium
     * 2 - hard
     */
    int type;
    /**
     * Time of record (in seconds)
     */
    Long time;

    /**
     * Nickname of player
     */
    String nickname;

    public Rank() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
