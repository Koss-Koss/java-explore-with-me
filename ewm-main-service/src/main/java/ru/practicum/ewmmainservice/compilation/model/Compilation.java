package ru.practicum.ewmmainservice.compilation.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.Hibernate;
import ru.practicum.ewmmainservice.event.model.Event;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Objects;
import java.util.Set;

import static ru.practicum.ewmmainservice.EwmMainServiceConstants.COMPILATION_TITLE_LENGTH_MAX;

@Entity
@Table(name = "compilations")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    Boolean pinned;
    @Size(max = COMPILATION_TITLE_LENGTH_MAX)
    String title;
    @ManyToMany(cascade = {CascadeType.PERSIST})
    @JoinTable(
            name = "compilation_events",
            joinColumns = @JoinColumn(name = "compilation_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id")
    )
    Set<Event> events;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Compilation that = (Compilation) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
