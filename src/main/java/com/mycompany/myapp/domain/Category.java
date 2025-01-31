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

@Document(collection = "category")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(Include.NON_NULL)
public class Category implements Serializable {

    private static final long serialVersionUID = 1L;

    
    
    @JsonSerialize(using = StringToObjectIdSerializer.class)
    @JsonDeserialize(using = ObjectIdToStringDeserializer.class )
    @JsonProperty("_id")
    @Field("_id")
    private String id;

    @Field("name")
    private String name;

    @Field("description")
    private String description;

    @Field("posts")
    @Builder.Default
    private List<RefType> posts = new ArrayList<>();

    public Category addPost(final RefType refType) {
        this.posts.add(refType);
        return this;
    }

    public List<RefType> getPosts() {
        return posts;
    }

    public void setPosts(final List<RefType> posts) {
        this.posts = posts;
    }


}
