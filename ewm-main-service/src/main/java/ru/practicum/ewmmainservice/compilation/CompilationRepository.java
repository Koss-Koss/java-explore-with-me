package ru.practicum.ewmmainservice.compilation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewmmainservice.compilation.model.Compilation;
import ru.practicum.ewmmainservice.exception.NotFoundException;

import static ru.practicum.ewmmainservice.exception.errormessage.ErrorMessageConstants.COMPILATION_NOT_FOUND_MESSAGE;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {

    default Compilation extract(long id) {
        return findById(id).orElseThrow(
                () -> new NotFoundException(COMPILATION_NOT_FOUND_MESSAGE + id));
    }

    Page<Compilation> findAllByPinned(Boolean pinned, Pageable pageable);
}
