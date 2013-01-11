package de.niles;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
@NamedQueries({
        @NamedQuery(name = "findAll", query = "select posts from MicroPost posts order by posts.id desc "),
        @NamedQuery(name = "clear", query = "delete from MicroPost")
})
public class MicroPost {
    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Size(min = 1, max = 25)
    @Pattern(regexp = "[A-Za-z]*", message = "only letters allowed")
    private String author;

    @NotNull
    @Size(min = 1, max = 140)
    private String content;

    public MicroPost(String author, String content) {
        this.author = author;
        this.content = content;
    }

    public MicroPost() {
    }

    @XmlElement
    public Long getId() {
        return id;
    }

    @XmlElement
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String name) {
        this.author = name;
    }

    @XmlElement
    public String getContent() {
        return content;
    }

    public void setContent(String post) {
        this.content = post;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MicroPost microPost = (MicroPost) o;

        return !(id != null ? !id.equals(microPost.id) : microPost.id != null);

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
