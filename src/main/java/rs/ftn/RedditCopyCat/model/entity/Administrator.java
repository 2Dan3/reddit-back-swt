package rs.ftn.RedditCopyCat.model.entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

// TODO Class, pitati
@Entity
@DiscriminatorValue("ADMIN")
public class Administrator extends User {
    @Override
    @Transient
    public String getRole() {
        return this.getClass().getAnnotation(DiscriminatorValue.class).toString();
    }
}
