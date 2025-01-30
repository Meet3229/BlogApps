package com.mycompany.myapp.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mycompany.myapp.json.ObjectIdToStringDeserializer;
import com.mycompany.myapp.json.StringToObjectIdSerializer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "post")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(Include.NON_NULL)
public class Post implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = StringToObjectIdSerializer.class)
    @JsonDeserialize(using = ObjectIdToStringDeserializer.class)
    @JsonProperty("_id")
    @Field("_id")
    private String id;

    @Field("title")
    private String title;

    @Field("contant")
    private String contant;

    @Field("comments")
    List<RefType> comments = new ArrayList<>();

    @Field("create_info")
    private CreateInfo createInfo;

    @Field("update_info")
    private UpdateInfo updateInfo;

    public Post addComments(final RefType refType) {
        this.comments.add(refType);
        return this;
    }

    public List<RefType> getComments() {
        return comments;
    }

    public void setComments(final List<RefType> comments) {
        this.comments = comments;
    }

   
}
