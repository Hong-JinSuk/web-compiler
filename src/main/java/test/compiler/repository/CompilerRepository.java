package test.compiler.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import test.compiler.testpackage.Member;

@Repository
public interface CompilerRepository extends JpaRepository<Member, Long> {
}
