package com.stelpolvo.video.domain.dto;

import com.stelpolvo.video.domain.Page;
import com.stelpolvo.video.domain.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserCriteria extends Page<UserInfo> {
    private String phone;
    private String email;
    private String username;

    public UserCriteria(Integer page, Integer pageSize) {
        super(page, pageSize);
    }

    public UserCriteria(Integer page, Integer pageSize, String username) {
        super(page, pageSize);
        this.username = username;
    }

    public UserCriteria(String phone, String email, String username, Integer page, Integer pageSize) {
        super(page, pageSize);
        this.phone = phone;
        this.email = email;
        this.username = username;
    }

    @Override
    public String toString() {
        return "UserCriteria{" +
                "phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", page=" + super.getPage() +
                ", pageSize=" + super.getPageSize() +
                ", list=" + super.getList() +
                ", total=" + super.getTotal() +
                '}';
    }
}
