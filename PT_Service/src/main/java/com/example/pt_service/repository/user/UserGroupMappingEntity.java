package com.example.pt_service.repository.user;

import com.example.pt_service.repository.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "user_group_mapping")
@IdClass(UserGroupMappingId.class) //복합기 이기때문에 따로 IdClass를 뺴줌
public class UserGroupMappingEntity extends BaseEntity {
    @Id
    private String UserGroupId;

    @Id
    private String userId;

    private String userGroupName;
    private String description;
}
